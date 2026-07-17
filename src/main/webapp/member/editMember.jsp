<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bean.Member" %>

<%
Member m = (Member) request.getAttribute("member");
String expiredDate = m.getExpiredDate() == null ? "Not active yet" : m.getExpiredDate().toString();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Member</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payment.css">
</head>
<body>

<div class="sidebar">
    <div class="logo">FitCore</div>
    <div class="menu">
        <a href="<%= request.getContextPath() %>/DashboardServlet">Dashboard</a>
        <a href="<%= request.getContextPath() %>/MemberServlet?action=list" class="active">Members</a>
        <a href="<%= request.getContextPath() %>/PaymentController?action=list">Payments</a>
        <a href="<%= request.getContextPath() %>/PromotionServlet?action=list">Promotions</a>
        <a href="<%= request.getContextPath() %>/EquipmentServlet">Equipment</a>
        <a href="<%= request.getContextPath() %>/MaintenanceServlet?action=list">Maintenance</a>
        <a href="<%= request.getContextPath() %>/ReportServlet">Reports</a>
        <a href="<%= request.getContextPath() %>/StaffServlet?action=list">Staff</a>
    </div>
    <div class="logout"><a href="login.jsp">Logout</a></div>
</div>

<div class="main">
    <div class="header">
        <h1>Edit Member</h1>
        <p>Membership expiry is managed automatically through successful payments.</p>
    </div>

    <div class="form-card">
        <form action="${pageContext.request.contextPath}/MemberServlet" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="memberID" value="<%= m.getMemberID() %>">

            <label>Member ID</label>
            <input type="text" value="<%= m.getMemberID() %>" readonly>

            <label>Member Name</label>
            <input type="text" name="memberName" value="<%= m.getMemberName() %>" required>

            <label>Email</label>
            <input type="email" name="memberEmail" value="<%= m.getMemberEmail() %>" required>

            <label>Phone</label>
            <input type="text" name="memberPhone" value="<%= m.getMemberPhone() %>" required>

            <label>Member Type</label>
            <select name="memberType" required>
                <option value="Regular" <%= "Regular".equals(m.getMemberType()) ? "selected" : "" %>>Regular - RM35/month</option>
                <option value="Premium" <%= "Premium".equals(m.getMemberType()) ? "selected" : "" %>>Premium - RM50/month</option>
                <option value="Student" <%= "Student".equals(m.getMemberType()) ? "selected" : "" %>>Student - RM20/month</option>
            </select>

            <label>Expired Date</label>
            <input type="text" value="<%= expiredDate %>" readonly>
            <small class="field-note">Create a new payment to extend this date.</small>

            <button type="submit" class="btn btn-update">Update Member</button>
            <a href="${pageContext.request.contextPath}/MemberServlet?action=list" class="btn btn-back">Back</a>
        </form>
    </div>
</div>

</body>
</html>
