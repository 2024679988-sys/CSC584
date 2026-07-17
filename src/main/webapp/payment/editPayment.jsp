<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="bean.Payment" %>
<%@ page import="bean.Member" %>
<%@ page import="bean.DiscountsAndPromotions" %>

<%!
public double getMemberTypePrice(String memberType) {
    if (memberType == null) return 0.0;
    if ("Premium".equalsIgnoreCase(memberType)) return 50.0;
    if ("Regular".equalsIgnoreCase(memberType)) return 35.0;
    if ("Student".equalsIgnoreCase(memberType)) return 20.0;
    return 0.0;
}
%>

<%
Payment payment = (Payment) request.getAttribute("payment");
ArrayList<Member> memberList = (ArrayList<Member>) request.getAttribute("memberList");
ArrayList<DiscountsAndPromotions> promoList =
        (ArrayList<DiscountsAndPromotions>) request.getAttribute("promoList");

Member selectedMember = null;
if (memberList != null && payment != null) {
    for (Member member : memberList) {
        if (member.getMemberID().equals(payment.getMemberID())) {
            selectedMember = member;
            break;
        }
    }
}
String selectedMemberName = selectedMember == null ? payment.getMemberID()
        : selectedMember.getMemberName() + " (" + selectedMember.getMemberID() + ")";
String selectedMemberType = selectedMember == null ? "Unknown" : selectedMember.getMemberType();
double selectedMonthlyPrice = getMemberTypePrice(selectedMemberType);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Payment</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/payment.css?v=20260715-4">
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
    <div class="header">
        <h1>Edit Payment</h1>
        <p>Update the payment method or promotion. Membership expiry will not be extended again.</p>
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

    <div class="form-card payment-form-card">
        <form action="<%= request.getContextPath() %>/PaymentController" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="paymentID" value="<%= payment.getPaymentID() %>">

            <div class="readonly-field">
                <label>Member</label>
                <span class="readonly-value"><%= selectedMemberName %></span>
                <small class="field-note compact-note">Member cannot be changed after membership expiry has been updated.</small>
            </div>

            <div class="amount-grid three-columns">
                <div class="readonly-field">
                    <label>Member Type</label>
                    <span class="readonly-value"><%= selectedMemberType %></span>
                </div>
                <div class="readonly-field">
                    <label>Monthly Price</label>
                    <span class="readonly-value">RM <%= String.format("%.2f", selectedMonthlyPrice) %></span>
                </div>
                <div class="readonly-field">
                    <label>Duration</label>
                    <span class="readonly-value"><%= payment.getDuration() %> Month<%= payment.getDuration() == 1 ? "" : "s" %></span>
                </div>
            </div>

            <div class="readonly-field">
                <label>Total Amount</label>
                <span class="readonly-value">RM <%= String.format("%.2f", payment.getAmount()) %></span>
            </div>

            <label for="paymentMethod">Payment Method</label>
            <select id="paymentMethod" name="paymentMethod" required>
                <option value="Cash" <%= "Cash".equals(payment.getPaymentMethod()) ? "selected" : "" %>>Cash</option>
                <option value="Online Banking" <%= "Online Banking".equals(payment.getPaymentMethod()) ? "selected" : "" %>>Online Banking</option>
                <option value="Card" <%= "Card".equals(payment.getPaymentMethod()) ? "selected" : "" %>>Card</option>
            </select>

            <label for="promoID">Promotion</label>
            <select id="promoID" name="promoID">
                <option value="" data-discount="0" <%= payment.getPromoID() == null ? "selected" : "" %>>No Promotion</option>
                <%
                boolean currentPromotionAvailable = false;
                if (promoList != null) {
                    for (DiscountsAndPromotions promo : promoList) {
                        boolean selected = promo.getPromoID().equals(payment.getPromoID());
                        if (selected) currentPromotionAvailable = true;
                %>
                    <option value="<%= promo.getPromoID() %>"
                            data-discount="<%= promo.getDiscountPercent() %>"
                            <%= selected ? "selected" : "" %>>
                        <%= promo.getPromoName() %> - <%= promo.getDiscountPercent() %>%
                        (Ends: <%= promo.getEndDate() %>)
                    </option>
                <%
                    }
                }

                if (payment.getPromoID() != null && !currentPromotionAvailable) {
                %>
                    <option value="" data-discount="0" selected>
                        Previous promotion ended - No Promotion will be used
                    </option>
                <%
                }
                %>
            </select>
            <small class="field-note">Only promotions active today can be applied.</small>

            <div class="payment-summary">
                <div>
                    <span>Updated Amount to Pay</span>
                    <small>After promotion discount</small>
                </div>
                <strong id="payableAmount">RM 0.00</strong>
            </div>

            <div class="warning-note">
                Membership expiry is not extended when editing this record. Create a new payment for a renewal.
            </div>

            <button type="submit" class="btn btn-save">Update Payment</button>
            <a href="<%= request.getContextPath() %>/PaymentController?action=list" class="btn btn-back">Back</a>
        </form>
    </div>
</div>

<script>
(function () {
    const totalAmount = <%= String.format(java.util.Locale.US, "%.2f", payment.getAmount()) %>;
    const promoSelect = document.getElementById('promoID');
    const payableAmount = document.getElementById('payableAmount');

    function updatePayableAmount() {
        const selectedPromo = promoSelect.options[promoSelect.selectedIndex];
        const discount = selectedPromo ? Number(selectedPromo.dataset.discount || 0) : 0;
        const finalAmount = totalAmount - (totalAmount * discount / 100);
        payableAmount.textContent = 'RM ' + finalAmount.toFixed(2);
    }

    promoSelect.addEventListener('change', updatePayableAmount);
    updatePayableAmount();
})();
</script>

</body>
</html>
