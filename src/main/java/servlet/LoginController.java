package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.PasswordValidator;

import java.io.IOException;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("staffUsername");
        String password = request.getParameter("staffPassword");
        username = username == null ? "" : username.trim();
        request.setAttribute("enteredUsername", username);

        if (username.isEmpty()) {
            request.setAttribute("errorMessage", "Username is required.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (!PasswordValidator.isValid(password)) {
            request.setAttribute("errorMessage", PasswordValidator.getRequirementMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            bean.Staff user = new bean.Staff();
            user.setStaffUsername(username);
            user.setStaffPassword(password);
            user = dao.LoginDAO.login(user);

            if (user.isLoggedIn()) {
                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }

                HttpSession session = request.getSession(true);
                session.setAttribute("staffID", user.getStaffID());
                session.setAttribute("staffName", user.getStaffName());
                session.setAttribute("staffRole", user.getStaffRole());
                session.setAttribute("staffUsername", user.getStaffUsername());
                session.setMaxInactiveInterval(30 * 60);

                response.sendRedirect(request.getContextPath() + "/DashboardServlet");
            } else {
                request.setAttribute("errorMessage", "Invalid username or password. Please try again.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
