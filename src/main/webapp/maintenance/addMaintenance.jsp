<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="bean.Equipment" %>

<%
List<Equipment> equipmentList =
        (List<Equipment>) request.getAttribute("equipmentList");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Maintenance</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/payment.css">
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

    <div class="header">
        <h1>Add Maintenance</h1>
    </div>

    <div class="form-card">
        <form action="<%= request.getContextPath() %>/MaintenanceServlet" method="post">
            <input type="hidden" name="action" value="add">

            <label>Equipment</label>
            <select name="equipmentID" required>
                <option value="">-- Select Equipment --</option>
                <%
                if (equipmentList != null) {
                    for (Equipment e : equipmentList) {
                %>
                    <option value="<%= e.getEquipmentID() %>">
                        <%= e.getEquipmentName() %> (<%= e.getEquipmentID() %>)
                    </option>
                <%
                    }
                }
                %>
            </select>

            <label>Maintenance Date</label>
            <input type="date" name="maintenanceDate" required>

            <label>Description</label>
            <textarea name="maintenanceDesc" rows="4" required></textarea>

            <label>Status</label>
            <select name="maintenanceStatus" required>
                <option value="">-- Select Status --</option>
                <option value="Pending">Pending</option>
                <option value="In Progress">In Progress</option>
                <option value="Completed">Completed</option>
                <option value="Cancelled">Cancelled</option>
            </select>

            <button type="submit" class="btn btn-save">Save Maintenance</button>
            <a href="<%= request.getContextPath() %>/MaintenanceServlet?action=list" class="btn btn-back">Back</a>
        </form>
    </div>

</div>

</body>
</html>