<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Equipment</title>
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
            <h1>Add Equipment</h1>
            <p>Equipment ID will be auto-generated.</p>
        </div>
    </div>

    <div class="form-card">
        <form action="${pageContext.request.contextPath}/EquipmentServlet" method="post">
            <input type="hidden" name="action" value="add">

            <label>Equipment Name *</label>
            <input type="text" name="equipmentName" required>

            <label>Category</label>
            <select name="category">
                <option value="Cardio">Cardio</option>
                <option value="Strength">Strength</option>
                <option value="Weight">Weight</option>
                <option value="Accessories">Accessories</option>
                <option value="Other">Other</option>
            </select>

            <label>Purchase Date</label>
            <input type="date" name="purchaseDate" required>

            <label>Status</label>
            <select name="equStatus">
                <option value="Available">Available</option>
                <option value="Under Maintenance">Under Maintenance</option>
                <option value="Retired">Retired</option>
            </select>

            <button type="submit" class="btn-save">Save Equipment</button>
            <a href="${pageContext.request.contextPath}/EquipmentServlet" class="btn-back">Back</a>
        </form>
    </div>

</div>

</body>
</html>