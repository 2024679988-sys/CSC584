package servlet;

import bean.Equipment;
import dao.EquipmentDAO;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "EquipmentServlet", urlPatterns = {"/EquipmentServlet"})
public class EquipmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EquipmentDAO dao = new EquipmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        if ("add".equalsIgnoreCase(action)) {
            request.getRequestDispatcher("equipment/addEquipment.jsp").forward(request, response);
            return;
        }

        if ("edit".equalsIgnoreCase(action)) {
            String id = request.getParameter("id");
            Equipment equipment = dao.getEquipmentById(id);
            request.setAttribute("equipment", equipment);
            request.getRequestDispatcher("equipment/editEquipment.jsp").forward(request, response);
            return;
        }

        if ("delete".equalsIgnoreCase(action)) {
            String id = request.getParameter("id");
            boolean deleted = dao.deleteEquipment(id);
            String result = deleted ? "deleted" : "blocked";
            response.sendRedirect(request.getContextPath()
                    + "/EquipmentServlet?action=list&deleteResult=" + result);
            return;
        }

        List<Equipment> list = dao.getAllEquipment();
        request.setAttribute("equipmentList", list);
        request.getRequestDispatcher("equipment/listEquipment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String name = request.getParameter("equipmentName");
        String category = request.getParameter("category");
        String status = request.getParameter("equStatus");
        String dateStr = request.getParameter("purchaseDate");

        Date purchaseDate = null;
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            purchaseDate = Date.valueOf(dateStr);
        }

        Equipment equipment = new Equipment();
        equipment.setEquipmentName(name);
        equipment.setCategory(category);
        equipment.setPurchaseDate(purchaseDate);
        equipment.setEquStatus(status);

        if ("add".equalsIgnoreCase(action)) {
            dao.addEquipment(equipment);
        } else if ("edit".equalsIgnoreCase(action)) {
            equipment.setEquipmentID(request.getParameter("equipmentID"));
            dao.updateEquipment(equipment);
        }

        response.sendRedirect(request.getContextPath() + "/EquipmentServlet?action=list");
    }
}
