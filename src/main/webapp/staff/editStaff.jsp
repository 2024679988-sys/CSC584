<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bean.Staff" %>

<%
Staff staff = (Staff) request.getAttribute("staff");
String sessionRole = (String) session.getAttribute("staffRole");
boolean isManager = sessionRole != null && sessionRole.equalsIgnoreCase("Manager");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Staff</title>
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
        <h1>Edit Staff</h1>
    </div>

    <div class="form-card">
        <form action="<%= request.getContextPath() %>/StaffServlet" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="staffID" value="<%= staff.getStaffID() %>">

            <label>Staff ID</label>
            <input type="text" value="<%= staff.getStaffID() %>" readonly>

            <label>Staff Name</label>
            <input type="text" name="staffName" value="<%= staff.getStaffName() %>" required>

            <label>Email</label>
            <input type="email" name="staffEmail" value="<%= staff.getStaffEmail() %>" required>

            <label>Phone</label>
            <input type="text" name="staffPhone" value="<%= staff.getStaffPhone() %>" required>

            <label>Hire Date</label>
            <input type="date" name="hireDate" value="<%= staff.getHireDate() %>" required>

            <label>Role</label>
            <% if (isManager) { %>
                <select name="staffRole" required>
                    <option value="Staff" <%= "Staff".equals(staff.getStaffRole()) ? "selected" : "" %>>Staff</option>
                    <option value="Manager" <%= "Manager".equals(staff.getStaffRole()) ? "selected" : "" %>>Manager</option>
                    <option value="Trainer" <%= "Trainer".equals(staff.getStaffRole()) ? "selected" : "" %>>Trainer</option>
                </select>
            <% } else { %>
                <input type="text" value="<%= staff.getStaffRole() %>" readonly>
            <% } %>

            <label>Username</label>
            <input type="text" name="staffUsername" value="<%= staff.getStaffUsername() %>" required>

            <label>New Password</label>
            <input type="password" name="staffPassword" placeholder="Leave empty to keep old password">

            <button type="submit" class="btn btn-update">Update Staff</button>
            <a href="<%= request.getContextPath() %>/StaffServlet?action=list" class="btn btn-back">Back</a>
        </form>
    </div>

</div>

</body>
</html>