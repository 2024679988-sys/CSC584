<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Staff</title>
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
        <a href="<%= request.getContextPath() %>/MaintenanceServlet?action=list">Maintenance</a>
        <a href="<%= request.getContextPath() %>/ReportServlet">Reports</a>

        <a href="<%= request.getContextPath() %>/StaffServlet?action=list" class="active">Staff</a>
    </div>

    <div class="logout">
        <a href="login.jsp">Logout</a>
    </div>
</div>

<div class="main">

    <div class="header">
        <h1>Add Staff</h1>
    </div>

    <div class="form-card">
        <form action="<%= request.getContextPath() %>/StaffServlet" method="post">
            <input type="hidden" name="action" value="add">

            <label>Staff Name</label>
            <input type="text" name="staffName" required>

            <label>Email</label>
            <input type="email" name="staffEmail" required>

            <label>Phone</label>
            <input type="text" name="staffPhone" required>

            <label>Hire Date</label>
            <input type="date" name="hireDate" required>

            <label>Role</label>
            <select name="staffRole" required>
                <option value="">-- Select Role --</option>
                <option value="Staff">Staff</option>
                <option value="Manager">Manager</option>
                <option value="Trainer">Trainer</option>
            </select>

            <label>Username</label>
            <input type="text" name="staffUsername" required>

            <label>Password</label>
            <input type="password" name="staffPassword" required>

            <button type="submit" class="btn btn-save">Add Staff</button>
            <a href="<%= request.getContextPath() %>/StaffServlet?action=list" class="btn btn-back">Back</a>
        </form>
    </div>

</div>

</body>
</html>