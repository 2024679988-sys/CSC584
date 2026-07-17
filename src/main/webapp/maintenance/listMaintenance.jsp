<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="bean.EquipmentMaintenance" %>
<%@ page import="bean.Equipment" %>

<%
List<EquipmentMaintenance> maintenanceList =
        (List<EquipmentMaintenance>) request.getAttribute("maintenanceList");

List<Equipment> equipmentList =
        (List<Equipment>) request.getAttribute("equipmentList");
%>

<%!
public String getEquipmentName(List<Equipment> equipmentList, String equipmentID) {
    if (equipmentList != null) {
        for (Equipment e : equipmentList) {
            if (e.getEquipmentID().equals(equipmentID)) {
                return e.getEquipmentName();
            }
        }
    }
    return "-";
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Maintenance Management</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/payment.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/js/sweetalert-helper.js" defer></script>
</head>

<body>

<div class="sidebar">
    <div class="logo">FitCore</div>

    <div class="menu">
        <a href="<%= request.getContextPath() %>/DashboardServlet">Dashboard</a>
        <a href="<%= request.getContextPath() %>/MemberServlet?action=list">Members</a>
        <a href="<%= request.getContextPath() %>/PaymentController?action=list">Payments</a>
        <a href="<%= request.getContextPath() %>/PromotionServlet?action=list">Promotions</a>
        <a href="<%= request.getContextPath() %>/EquipmentServlet">Equipment</a>
        <a href="<%= request.getContextPath() %>/MaintenanceServlet?action=list" class="active">Maintenance</a>
        <a href="<%= request.getContextPath() %>/ReportServlet">Reports</a>

        <a href="<%= request.getContextPath() %>/StaffServlet?action=list">Staff</a>
    </div>

    <div class="logout">
        <a href="login.jsp">Logout</a>
    </div>
</div>

<div class="main">

    <div class="header header-list">
        <div>
            <h1>Maintenance Management</h1>
            <p>Total Maintenance: <%= maintenanceList == null ? 0 : maintenanceList.size() %></p>
        </div>

        <a class="btn-add" href="<%= request.getContextPath() %>/MaintenanceServlet?action=add">
            + Add Maintenance
        </a>
    </div>

    <div class="section">
        <table>
            <tr>
                <th>ID</th>
                <th>Equipment</th>
                <th>Date</th>
                <th>Description</th>
                <th>Status</th>
                <th>Staff ID</th>
                <th>Action</th>
            </tr>

            <%
            if (maintenanceList == null || maintenanceList.isEmpty()) {
            %>
                <tr>
                    <td colspan="7" class="empty">No maintenance found.</td>
                </tr>
            <%
            } else {
                for (EquipmentMaintenance m : maintenanceList) {
            %>
                <tr>
                    <td><%= m.getMaintenanceID() %></td>
                    <td>
                        <%= getEquipmentName(equipmentList, m.getEquipmentID()) %>
                        (<%= m.getEquipmentID() %>)
                    </td>
                    <td><%= m.getMaintenanceDate() %></td>
                    <td><%= m.getMaintenanceDesc() %></td>
                    <td><%= m.getMaintenanceStatus() %></td>
                    <td><%= m.getStaffID() %></td>
                    <td>
                        <a class="action-btn edit"
                           href="<%= request.getContextPath() %>/MaintenanceServlet?action=edit&maintenanceID=<%= m.getMaintenanceID() %>">
                           Edit
                        </a>

                        <a class="action-btn delete"
                           href="<%= request.getContextPath() %>/MaintenanceServlet?action=delete&maintenanceID=<%= m.getMaintenanceID() %>"
                           data-swal-delete data-swal-title="Confirm Deletion" data-swal-text="Delete this maintenance record? This action cannot be undone.">
                           Delete
                        </a>
                    </td>
                </tr>
            <%
                }
            }
            %>
        </table>
    </div>

</div>

</body>
</html>