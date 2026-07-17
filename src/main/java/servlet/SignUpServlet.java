package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.PasswordValidator;

import java.io.IOException;
import java.util.Set;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Set<String> ALLOWED_ROLES =
            Set.of("Staff", "Trainer", "Receptionist", "Manager");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String staffName = clean(request.getParameter("staffName"));
        String staffPhone = clean(request.getParameter("staffPhone"));
        String staffEmail = clean(request.getParameter("staffEmail"));
        String staffUsername = clean(request.getParameter("staffUsername"));
        String staffPassword = request.getParameter("staffPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        String staffRole = clean(request.getParameter("staffRole"));

        preserveFormValues(request, staffName, staffPhone, staffEmail, staffUsername, staffRole);

        if (staffName.isEmpty() || staffPhone.isEmpty() || staffEmail.isEmpty()
                || staffUsername.isEmpty() || staffRole.isEmpty()) {
            forwardWithError(request, response, "All fields are required.");
            return;
        }

        if (!PasswordValidator.isValid(staffPassword)) {
            forwardWithError(request, response, PasswordValidator.getRequirementMessage());
            return;
        }

        if (!staffPassword.equals(confirmPassword)) {
            forwardWithError(request, response, "Password and confirmation password do not match.");
            return;
        }

        if (!ALLOWED_ROLES.contains(staffRole)) {
            forwardWithError(request, response, "Please select a valid staff role.");
            return;
        }

        try {
            bean.Staff staff = new bean.Staff();
            staff.setStaffName(staffName);
            staff.setStaffPhone(staffPhone);
            staff.setStaffEmail(staffEmail);
            staff.setStaffUsername(staffUsername);
            staff.setStaffPassword(staffPassword);
            staff.setStaffRole(staffRole);

            boolean success = dao.SignUpDAO.signUp(staff);

            if (success) {
                request.setAttribute("successMessage", "Account created successfully. Please log in.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                forwardWithError(request, response,
                        "Sign up failed. The username or email may already be registered.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            forwardWithError(request, response,
                    "Unable to create the account. Please check the entered information.");
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }

    private void preserveFormValues(HttpServletRequest request, String name, String phone,
            String email, String username, String role) {
        request.setAttribute("enteredName", name);
        request.setAttribute("enteredPhone", phone);
        request.setAttribute("enteredEmail", email);
        request.setAttribute("enteredUsername", username);
        request.setAttribute("enteredRole", role);
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
            String message) throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("signup.jsp").forward(request, response);
    }
}
