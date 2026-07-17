package servlet;

import java.io.IOException;

import dao.DashboardDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final DashboardDAO dao = new DashboardDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("staffID") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Prevent a previous user's dashboard from being shown after logout.
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        request.setAttribute("totalMembers", dao.getTotalMembers());
        request.setAttribute("activeMembers", dao.getActiveMembers());
        request.setAttribute("totalPayments", dao.getTotalPayments());
        request.setAttribute("totalRevenue", dao.getTotalRevenue());
        request.setAttribute("totalEquipment", dao.getTotalEquipment());
        request.setAttribute("pendingMaintenance", dao.getPendingMaintenance());
        request.setAttribute("currentPromotion", dao.getCurrentPromotion());
        request.setAttribute("recentActivities", dao.getRecentActivities());
        request.setAttribute("upcomingExpiries", dao.getUpcomingExpiries());

        request.getRequestDispatcher("/Dashboard.jsp")
               .forward(request, response);
    }
}
