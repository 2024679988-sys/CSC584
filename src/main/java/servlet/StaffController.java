package servlet;

import java.io.IOException;
import java.sql.Date;

import bean.Staff;
import dao.StaffDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/StaffServlet")
public class StaffController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private boolean isManager(HttpSession session) {
        String role = (String) session.getAttribute("staffRole");
        return role != null && role.equalsIgnoreCase("Manager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("staffID") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String loginStaffID = (String) session.getAttribute("staffID");
        boolean manager = isManager(session);

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        if ("add".equalsIgnoreCase(action)) {

            if (!manager) {
                response.sendRedirect(request.getContextPath() + "/StaffServlet?action=list");
                return;
            }

            request.getRequestDispatcher("staff/addStaff.jsp").forward(request, response);
        }

        else if ("edit".equalsIgnoreCase(action)) {

            String staffID = request.getParameter("staffID");

            if (!manager && !loginStaffID.equals(staffID)) {
                response.sendRedirect(request.getContextPath() + "/StaffServlet?action=list&accessDenied=true");
                return;
            }

            Staff staff = StaffDAO.getStaffById(staffID);
            request.setAttribute("staff", staff);

            request.getRequestDispatcher("staff/editStaff.jsp").forward(request, response);
        }

        else if ("delete".equalsIgnoreCase(action)) {
            // Account deletion changes data, so it must be submitted using POST.
            response.sendRedirect(request.getContextPath() + "/StaffServlet?action=list&invalidDeleteRequest=true");
        }

        else {
            request.setAttribute("staffList", StaffDAO.getAllStaff());
            request.setAttribute("isManager", manager);
            request.setAttribute("loginStaffID", loginStaffID);

            request.getRequestDispatcher("staff/listStaff.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("staffID") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String loginStaffID = (String) session.getAttribute("staffID");
        boolean manager = isManager(session);
        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {
            handleDelete(request, response, session, loginStaffID, manager);
            return;
        }

        if ("add".equalsIgnoreCase(action) && !manager) {
            response.sendRedirect(request.getContextPath() + "/StaffServlet?action=list&accessDenied=true");
            return;
        }

        if ("update".equalsIgnoreCase(action)) {
            String staffID = request.getParameter("staffID");

            if (!manager && !loginStaffID.equals(staffID)) {
                response.sendRedirect(request.getContextPath() + "/StaffServlet?action=list&accessDenied=true");
                return;
            }
        }

        Staff staff = new Staff();

        staff.setStaffID(request.getParameter("staffID"));
        staff.setStaffName(request.getParameter("staffName"));
        staff.setStaffEmail(request.getParameter("staffEmail"));
        staff.setStaffPhone(request.getParameter("staffPhone"));
        staff.setStaffUsername(request.getParameter("staffUsername"));
        staff.setStaffPassword(request.getParameter("staffPassword"));

        String hireDate = request.getParameter("hireDate");
        if (hireDate != null && !hireDate.trim().isEmpty()) {
            staff.setHireDate(Date.valueOf(hireDate));
        }

        if (manager) {
            staff.setStaffRole(request.getParameter("staffRole"));
        } else {
            Staff oldStaff = StaffDAO.getStaffById(loginStaffID);
            staff.setStaffRole(oldStaff.getStaffRole());
        }

        if ("update".equalsIgnoreCase(action)) {
            StaffDAO.updateStaff(staff);
        } else {
            StaffDAO.addStaff(staff);
        }

        response.sendRedirect(request.getContextPath() + "/StaffServlet?action=list");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, String loginStaffID, boolean manager) throws IOException {

        String targetStaffID = request.getParameter("staffID");

        if (targetStaffID == null || targetStaffID.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath()
                    + "/StaffServlet?action=list&deleteFailed=true");
            return;
        }

        targetStaffID = targetStaffID.trim();
        boolean deletingOwnAccount = loginStaffID.equals(targetStaffID);

        // Normal staff can delete only their own account. Managers can delete other staff too.
        if (!deletingOwnAccount && !manager) {
            response.sendRedirect(request.getContextPath()
                    + "/StaffServlet?action=list&accessDenied=true");
            return;
        }

        // Prevent deletion when this staff account is referenced by another table.
        if (StaffDAO.hasIntegrityConstraint(targetStaffID)) {
            response.sendRedirect(request.getContextPath()
                    + "/StaffServlet?action=list&deleteBlocked=true");
            return;
        }

        boolean deleted = StaffDAO.deleteStaff(targetStaffID);

        if (!deleted) {
            response.sendRedirect(request.getContextPath()
                    + "/StaffServlet?action=list&deleteFailed=true");
            return;
        }

        if (deletingOwnAccount) {
            session.invalidate();
            response.sendRedirect(request.getContextPath()
                    + "/login.jsp?accountDeleted=true");
            return;
        }

        response.sendRedirect(request.getContextPath()
                + "/StaffServlet?action=list&deleteSuccess=true");
    }
}
