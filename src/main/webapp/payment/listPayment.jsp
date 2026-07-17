<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="bean.Payment" %>
<%@ page import="bean.Member" %>
<%@ page import="bean.Staff" %>
<%@ page import="bean.DiscountsAndPromotions" %>

<%
ArrayList<Payment> paymentList = (ArrayList<Payment>) request.getAttribute("paymentList");
ArrayList<Member> memberList = (ArrayList<Member>) request.getAttribute("memberList");
ArrayList<Staff> staffList = (ArrayList<Staff>) request.getAttribute("staffList");
ArrayList<DiscountsAndPromotions> promoList =
        (ArrayList<DiscountsAndPromotions>) request.getAttribute("promoList");
String staffName = (String) session.getAttribute("staffName");
if (staffName == null) staffName = "Staff";
%>

<%!
public Member getMember(ArrayList<Member> memberList, String memberID) {
    if (memberList != null) {
        for (Member member : memberList) {
            if (member.getMemberID().equals(memberID)) return member;
        }
    }
    return null;
}

public String getStaffName(ArrayList<Staff> staffList, String staffID) {
    if (staffList != null) {
        for (Staff staff : staffList) {
            if (staff.getStaffID().equals(staffID)) return staff.getStaffName();
        }
    }
    return "-";
}

public String getPromoName(ArrayList<DiscountsAndPromotions> promoList, String promoID) {
    if (promoID == null || promoID.trim().isEmpty()) return "No Promotion";
    if (promoList != null) {
        for (DiscountsAndPromotions promo : promoList) {
            if (promo.getPromoID().equals(promoID)) return promo.getPromoName();
        }
    }
    return "Promotion Unavailable";
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Payment Management</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/payment.css?v=20260715-4">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/js/sweetalert-helper.js" defer></script>
</head>
<body>

<div class="sidebar">
    <div class="logo">FitCore</div>
    <div class="menu">
        <a href="<%= request.getContextPath() %>/DashboardServlet">Dashboard</a>
        <a href="<%= request.getContextPath() %>/MemberServlet?action=list">Members</a>
        <a href="<%= request.getContextPath() %>/PaymentController?action=list" class="active">Payments</a>
        <a href="<%= request.getContextPath() %>/PromotionServlet?action=list">Promotions</a>
        <a href="<%= request.getContextPath() %>/EquipmentServlet">Equipment</a>
        <a href="<%= request.getContextPath() %>/MaintenanceServlet?action=list">Maintenance</a>
        <a href="<%= request.getContextPath() %>/ReportServlet">Reports</a>
        <a href="<%= request.getContextPath() %>/StaffServlet?action=list">Staff</a>
    </div>
    <div class="logout"><a href="login.jsp">Logout</a></div>
</div>

<div class="main">
    <div class="header header-list">
        <div>
            <h1>Payment Management</h1>
            <p>Total Payments: <%= paymentList == null ? 0 : paymentList.size() %></p>
        </div>
        <a class="btn-add" href="<%= request.getContextPath() %>/PaymentController?action=add">+ Add Payment</a>
    </div>

    <div class="section payment-rate-section">
        <div class="payment-rate-header">
            <div>
                <h2>Monthly Membership Rates</h2>
                <p>The selected duration multiplies the member's monthly rate.</p>
            </div>
            <span class="rate-note">Prices per month</span>
        </div>

        <div class="rate-grid">
            <div class="rate-card">
                <div class="rate-card-info">
                    <h3>Premium</h3>
                    <p>Full membership access</p>
                </div>
                <div class="rate-price">RM 50 <small>/ month</small></div>
            </div>

            <div class="rate-card">
                <div class="rate-card-info">
                    <h3>Regular</h3>
                    <p>Standard membership access</p>
                </div>
                <div class="rate-price">RM 35 <small>/ month</small></div>
            </div>

            <div class="rate-card">
                <div class="rate-card-info">
                    <h3>Student</h3>
                    <p>Special student rate</p>
                </div>
                <div class="rate-price">RM 20 <small>/ month</small></div>
            </div>
        </div>
    </div>

    <div class="section table-wrapper">
        <table>
            <tr>
                <th>Payment ID</th>
                <th>Date</th>
                <th>Member</th>
                <th>Member Type</th>
                <th>Total Amount</th>
                <th>After Discount</th>
                <th>Method</th>
                <th>Promotion</th>
                <th>Staff</th>
                <th>Action</th>
            </tr>

            <%
            if (paymentList == null || paymentList.isEmpty()) {
            %>
                <tr><td colspan="10" class="empty">No payment found.</td></tr>
            <%
            } else {
                for (Payment payment : paymentList) {
                    Member member = getMember(memberList, payment.getMemberID());
                    String memberName = member != null ? member.getMemberName() : "-";
                    String memberType = member != null ? member.getMemberType() : "-";
            %>
                <tr>
                    <td><strong><%= payment.getPaymentID() %></strong></td>
                    <td><%= payment.getPaymentDate() %></td>
                    <td><%= memberName %> (<%= payment.getMemberID() %>)</td>
                    <td><span class="type-badge"><%= memberType %></span></td>
                    <td>RM <%= String.format("%.2f", payment.getAmount()) %></td>
                    <td><strong>RM <%= String.format("%.2f", payment.getDiscountedAmount()) %></strong></td>
                    <td><%= payment.getPaymentMethod() %></td>
                    <td><%= getPromoName(promoList, payment.getPromoID()) %></td>
                    <td><%= getStaffName(staffList, payment.getStaffID()) %></td>
                    <td>
                        <a class="action-btn edit" href="<%= request.getContextPath() %>/PaymentController?action=edit&paymentID=<%= payment.getPaymentID() %>">Edit</a>
                        <a class="action-btn delete" href="<%= request.getContextPath() %>/PaymentController?action=delete&paymentID=<%= payment.getPaymentID() %>" data-swal-delete data-swal-title="Confirm Deletion" data-swal-text="Delete this payment? This action cannot be undone.">Delete</a>
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
