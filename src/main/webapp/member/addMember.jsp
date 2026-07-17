<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Member</title>
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
        <h1>Add Member</h1>
        <p>Create the member profile first. Membership expiry will be assigned after payment.</p>
    </div>

    <div class="form-card">
        <form action="${pageContext.request.contextPath}/MemberServlet" method="post">
            <input type="hidden" name="action" value="insert">

            <label>Member Name</label>
            <input type="text" name="memberName" placeholder="Michael Jackson" required>

            <label>Email</label>
            <input type="email" name="memberEmail" placeholder="email@gmail.com" required>

            <label>Phone</label>
            <input type="text" name="memberPhone" placeholder="0123456789" required>

            <label>Member Type</label>
            <select name="memberType" required>
                <option value="">-- Select Type --</option>
                <option value="Regular">Regular - RM35/month</option>
                <option value="Premium">Premium - RM50/month</option>
                <option value="Student">Student - RM20/month</option>
            </select>

            <div class="expiry-info-card">
                <span class="expiry-info-icon"></span>
                <div>
                    <strong>Expired Date: Not active yet</strong>
                    <p>The expired date is initially null. Create a payment and choose 1, 2, or 3 months to activate the membership.</p>
                </div>
            </div>

            <button type="submit" class="btn btn-save">Save Member</button>
            <a href="${pageContext.request.contextPath}/MemberServlet?action=list" class="btn btn-back">Back</a>
        </form>
    </div>
</div>

</body>
</html>
