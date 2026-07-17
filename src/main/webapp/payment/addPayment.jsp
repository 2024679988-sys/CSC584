<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="bean.Member" %>
<%@ page import="bean.DiscountsAndPromotions" %>

<%
ArrayList<Member> memberList = (ArrayList<Member>) request.getAttribute("memberList");
ArrayList<DiscountsAndPromotions> promoList =
        (ArrayList<DiscountsAndPromotions>) request.getAttribute("promoList");
%>

<%!
public double getMemberTypePrice(String memberType) {
    if (memberType == null) return 0.0;
    if ("Premium".equalsIgnoreCase(memberType)) return 50.0;
    if ("Regular".equalsIgnoreCase(memberType)) return 35.0;
    if ("Student".equalsIgnoreCase(memberType)) return 20.0;
    return 0.0;
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Payment</title>
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
        <h1>Add Payment</h1>
        <p>Create a membership payment and update the member's expired date.</p>
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
            <input type="hidden" name="action" value="add">

            <label for="memberID">Member</label>
            <select id="memberID" name="memberID" required>
                <option value="">-- Select Member --</option>
                <%
                if (memberList != null) {
                    for (Member member : memberList) {
                        double price = getMemberTypePrice(member.getMemberType());
                        String expiry = member.getExpiredDate() == null ? "" : member.getExpiredDate().toString();
                %>
                    <option value="<%= member.getMemberID() %>"
                            data-member-type="<%= member.getMemberType() %>"
                            data-price="<%= String.format("%.2f", price) %>"
                            data-expiry="<%= expiry %>">
                        <%= member.getMemberName() %> (<%= member.getMemberID() %>) - <%= member.getMemberType() %>
                    </option>
                <%
                    }
                }
                %>
            </select>

            <div class="amount-grid three-columns">
                <div class="readonly-field">
                    <label>Member Type</label>
                    <span id="memberTypeDisplay" class="readonly-value muted">Select a member</span>
                </div>
                <div class="readonly-field">
                    <label>Monthly Price</label>
                    <span id="monthlyPrice" class="readonly-value">RM 0.00</span>
                </div>
                <div>
                    <label for="duration">Duration</label>
                    <select id="duration" name="duration" required>
                        <option value="1">1 Month</option>
                        <option value="2">2 Months</option>
                        <option value="3">3 Months</option>
                    </select>
                </div>
            </div>

            <div class="amount-grid">
                <div class="readonly-field">
                    <label>Total Amount</label>
                    <span id="amount" class="readonly-value">RM 0.00</span>
                    <small class="field-note compact-note">Duration × monthly membership price.</small>
                </div>
                <div class="readonly-field">
                    <label>New Expired Date</label>
                    <span id="expiryPreview" class="readonly-value muted">Select a member</span>
                    <small class="field-note compact-note">Existing future membership time is preserved.</small>
                </div>
            </div>

            <label for="paymentMethod">Payment Method</label>
            <select id="paymentMethod" name="paymentMethod" required>
                <option value="">-- Select Method --</option>
                <option value="Cash">Cash</option>
                <option value="Online Banking">Online Banking</option>
                <option value="Card">Card</option>
            </select>

            <label for="promoID">Promotion</label>
            <select id="promoID" name="promoID">
                <option value="" data-discount="0">No Promotion</option>
                <%
                if (promoList != null) {
                    for (DiscountsAndPromotions promo : promoList) {
                %>
                    <option value="<%= promo.getPromoID() %>"
                            data-discount="<%= promo.getDiscountPercent() %>">
                        <%= promo.getPromoName() %> - <%= promo.getDiscountPercent() %>%
                        (Ends: <%= promo.getEndDate() %>)
                    </option>
                <%
                    }
                }
                %>
            </select>
            <small class="field-note">Only promotions active today are shown.</small>

            <div class="payment-summary">
                <div>
                    <span>Amount to Pay</span>
                    <small>After promotion discount</small>
                </div>
                <strong id="payableAmount">RM 0.00</strong>
            </div>

            <button type="submit" class="btn btn-save">Save Payment &amp; Update Expiry</button>
            <a href="<%= request.getContextPath() %>/PaymentController?action=list" class="btn btn-back">Back</a>
        </form>
    </div>
</div>

<script>
(function () {
    const memberSelect = document.getElementById('memberID');
    const memberTypeDisplay = document.getElementById('memberTypeDisplay');
    const monthlyPriceDisplay = document.getElementById('monthlyPrice');
    const durationSelect = document.getElementById('duration');
    const amountDisplay = document.getElementById('amount');
    const promoSelect = document.getElementById('promoID');
    const payableAmount = document.getElementById('payableAmount');
    const expiryPreview = document.getElementById('expiryPreview');
    let totalAmount = 0;

    function formatDate(date) {
        return new Intl.DateTimeFormat('en-GB', {
            day: '2-digit', month: 'short', year: 'numeric'
        }).format(date);
    }

    function addMonthsLikeOracle(date, months) {
        const result = new Date(date.getTime());
        const originalDay = result.getDate();
        const originalLastDay = new Date(result.getFullYear(), result.getMonth() + 1, 0).getDate();
        const wasLastDay = originalDay === originalLastDay;

        result.setDate(1);
        result.setMonth(result.getMonth() + months);
        const targetLastDay = new Date(result.getFullYear(), result.getMonth() + 1, 0).getDate();
        result.setDate(wasLastDay ? targetLastDay : Math.min(originalDay, targetLastDay));
        return result;
    }

    function updatePaymentDetails() {
        const selectedMember = memberSelect.options[memberSelect.selectedIndex];
        const memberType = selectedMember ? selectedMember.dataset.memberType : '';
        const monthlyPrice = selectedMember ? Number(selectedMember.dataset.price || 0) : 0;
        const currentExpiry = selectedMember ? selectedMember.dataset.expiry : '';
        const duration = Number(durationSelect.value || 0);
        totalAmount = monthlyPrice * duration;

        memberTypeDisplay.textContent = memberType || 'Select a member';
        memberTypeDisplay.classList.toggle('muted', !memberType);
        monthlyPriceDisplay.textContent = 'RM ' + monthlyPrice.toFixed(2);
        amountDisplay.textContent = 'RM ' + totalAmount.toFixed(2);

        if (monthlyPrice > 0) {
            const today = new Date();
            today.setHours(0, 0, 0, 0);

            let baseDate = currentExpiry ? new Date(currentExpiry + 'T00:00:00') : today;
            if (Number.isNaN(baseDate.getTime()) || baseDate < today) baseDate = today;

            expiryPreview.textContent = formatDate(addMonthsLikeOracle(baseDate, duration));
            expiryPreview.classList.remove('muted');
        } else {
            expiryPreview.textContent = 'Select a member';
            expiryPreview.classList.add('muted');
        }

        updatePayableAmount();
    }

    function updatePayableAmount() {
        const selectedPromo = promoSelect.options[promoSelect.selectedIndex];
        const discount = selectedPromo ? Number(selectedPromo.dataset.discount || 0) : 0;
        const finalAmount = totalAmount - (totalAmount * discount / 100);
        payableAmount.textContent = 'RM ' + finalAmount.toFixed(2);
    }

    memberSelect.addEventListener('change', updatePaymentDetails);
    durationSelect.addEventListener('change', updatePaymentDetails);
    promoSelect.addEventListener('change', updatePayableAmount);
    updatePaymentDetails();
})();
</script>

</body>
</html>
