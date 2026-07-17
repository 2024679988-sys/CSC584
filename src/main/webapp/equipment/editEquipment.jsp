<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Equipment</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/equipment.css">
</head>

<body>

<div class="sidebar">
    <div class="logo">FitCore</div>

    <div class="menu">
        <a href="<%= request.getContextPath() %>/DashboardServlet">Dashboard</a>
        <a href="<%= request.getContextPath() %>/MemberServlet?action=list">Members</a>
        <a href="<%= request.getContextPath() %>/PaymentController?action=list">Payments</a>
        <a href="<%= request.getContextPath() %>/PromotionServlet?action=list">Promotions</a>
        <a href="<%= request.getContextPath() %>/EquipmentServlet" class="active">Equipment</a>
        <a href="<%= request.getContextPath() %>/MaintenanceServlet?action=list">Maintenance</a>
        <a href="<%= request.getContextPath() %>/ReportServlet">Reports</a>

        <a href="<%= request.getContextPath() %>/StaffServlet?action=list">Staff</a>
    </div>

    <div class="logout">
        <a href="login.jsp">Logout</a>
    </div>
</div>

<div class="main">

    <div class="header">
        <div>
            <h1>Edit Equipment</h1>
            <p>Update equipment details.</p>
        </div>
    </div>

    <div class="form-card">
        <form action="${pageContext.request.contextPath}/EquipmentServlet" method="post">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="equipmentID" value="${equipment.equipmentID}">

            <label>Equipment ID</label>
            <input type="text" value="${equipment.equipmentID}" readonly>

            <label>Equipment Name *</label>
            <input type="text" name="equipmentName" value="${equipment.equipmentName}" required>

            <label>Category</label>
            <select name="category">
                <option value="Cardio" ${equipment.category == 'Cardio' ? 'selected' : ''}>Cardio</option>
                <option value="Strength" ${equipment.category == 'Strength' ? 'selected' : ''}>Strength</option>
                <option value="Weight" ${equipment.category == 'Weight' ? 'selected' : ''}>Weight</option>
                <option value="Accessories" ${equipment.category == 'Accessories' ? 'selected' : ''}>Accessories</option>
                <option value="Other" ${equipment.category == 'Other' ? 'selected' : ''}>Other</option>
            </select>

            <label>Purchase Date</label>
            <input type="date" name="purchaseDate" value="${equipment.purchaseDate}" required>

            <label>Status</label>
            <select name="equStatus">
                <option value="Available" ${equipment.equStatus == 'Available' ? 'selected' : ''}>Available</option>
                <option value="Under Maintenance" ${equipment.equStatus == 'Under Maintenance' ? 'selected' : ''}>Under Maintenance</option>
                <option value="Retired" ${equipment.equStatus == 'Retired' ? 'selected' : ''}>Retired</option>
            </select>

            <button type="submit" class="btn-save">Update Equipment</button>
            <a href="${pageContext.request.contextPath}/EquipmentServlet" class="btn-back">Back</a>
        </form>
    </div>

</div>

</body>
</html>