package servlet;

import java.io.IOException;
import java.sql.Date;

import bean.EquipmentMaintenance;
import dao.MaintenanceDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/MaintenanceServlet")
public class MaintenanceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private MaintenanceDAO dao = new MaintenanceDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("staffID") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        if ("add".equalsIgnoreCase(action)) {
            request.setAttribute("equipmentList", dao.getAllEquipment());
            request.getRequestDispatcher("maintenance/addMaintenance.jsp").forward(request, response);
        }

        else if ("edit".equalsIgnoreCase(action)) {
            String maintenanceID = request.getParameter("maintenanceID");

            request.setAttribute("maintenance", dao.getMaintenanceById(maintenanceID));
            request.setAttribute("equipmentList", dao.getAllEquipment());

            request.getRequestDispatcher("maintenance/editMaintenance.jsp").forward(request, response);
        }

        else if ("delete".equalsIgnoreCase(action)) {
            String maintenanceID = request.getParameter("maintenanceID");

            dao.deleteMaintenance(maintenanceID);
            response.sendRedirect(request.getContextPath() + "/MaintenanceServlet?action=list");
        }

        else {
            request.setAttribute("maintenanceList", dao.getAllMaintenance());
            request.setAttribute("equipmentList", dao.getAllEquipment());

            request.getRequestDispatcher("maintenance/listMaintenance.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("staffID") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String staffID = (String) session.getAttribute("staffID");
        String action = request.getParameter("action");

        EquipmentMaintenance m = new EquipmentMaintenance();

        m.setStaffID(staffID);
        m.setEquipmentID(request.getParameter("equipmentID"));
        m.setMaintenanceDesc(request.getParameter("maintenanceDesc"));
        m.setMaintenanceStatus(request.getParameter("maintenanceStatus"));

        String maintenanceDate = request.getParameter("maintenanceDate");
        if (maintenanceDate != null && !maintenanceDate.trim().isEmpty()) {
            m.setMaintenanceDate(Date.valueOf(maintenanceDate));
        }

        if ("update".equalsIgnoreCase(action)) {
            m.setMaintenanceID(request.getParameter("maintenanceID"));
            dao.updateMaintenance(m);
        } else {
            dao.addMaintenance(m);
        }

        response.sendRedirect(request.getContextPath() + "/MaintenanceServlet?action=list");
    }
}