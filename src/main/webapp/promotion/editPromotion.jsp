<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bean.DiscountsAndPromotions" %>

<%
DiscountsAndPromotions promo =
    (DiscountsAndPromotions) request.getAttribute("promo");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Promotion</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/payment.css">
</head>

<body>

<div class="sidebar">
    <div class="logo">FitCore</div>

    <div class="menu">
        <a href="<%= request.getContextPath() %>/DashboardServlet">Dashboard</a>
        <a href="<%= request.getContextPath() %>/MemberServlet?action=list">Members</a>
        <a href="<%= request.getContextPath() %>/PaymentController?action=list">Payments</a>
        <a href="<%= request.getContextPath() %>/PromotionServlet?action=list" class="active">Promotions</a>
        <a href="<%= request.getContextPath() %>/EquipmentServlet">Equipment</a>
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
        <h1>Edit Promotion</h1>
    </div>

    <div class="form-card">
        <form action="<%= request.getContextPath() %>/PromotionServlet" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="promoID" value="<%= promo.getPromoID() %>">

            <label>Promotion ID</label>
            <input type="text" value="<%= promo.getPromoID() %>" readonly>

            <label>Promotion Name</label>
            <input type="text" name="promoName" value="<%= promo.getPromoName() %>" required>

            <label>Discount Percent</label>
            <input type="number" name="discountPercent" step="0.01" min="0" max="100"
                   value="<%= promo.getDiscountPercent() %>" required>

            <label>Start Date</label>
            <input type="date" name="startDate" value="<%= promo.getStartDate() %>" required>

            <label>End Date</label>
            <input type="date" name="endDate" value="<%= promo.getEndDate() %>" required>

            <label>Description</label>
            <textarea name="promoDesc" rows="4" required><%= promo.getPromoDesc() %></textarea>

            <button type="submit" class="btn btn-update">Update Promotion</button>
            <a href="<%= request.getContextPath() %>/PromotionServlet?action=list" class="btn btn-back">Back</a>
        </form>
    </div>

</div>

</body>
</html>