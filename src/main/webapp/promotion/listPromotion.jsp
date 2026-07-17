<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="bean.DiscountsAndPromotions" %>

<%
List<DiscountsAndPromotions> promoList =
    (List<DiscountsAndPromotions>) request.getAttribute("promoList");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Promotion Management</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/payment.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/js/sweetalert-helper.js" defer></script>

<style>
.delete-form {
    display: inline;
    margin: 0;
}

.delete-form .action-btn {
    border: none;
    font: inherit;
    cursor: pointer;
}

.action-btn.delete-disabled,
.action-btn.delete-disabled:hover {
    background: #9ca3af;
    color: #f3f4f6;
    cursor: not-allowed;
    opacity: 0.75;
}
</style>
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
        <a href="<%= request.getContextPath() %>/LogoutServlet">Logout</a>
    </div>
</div>

<div class="main">

    <div class="header header-list">
        <div>
            <h1>Promotion Management</h1>
            <p>Total Promotions: <%= promoList == null ? 0 : promoList.size() %></p>
        </div>

        <a class="btn-add" href="<%= request.getContextPath() %>/PromotionServlet?action=add">
            + Add Promotion
        </a>
    </div>

    <div class="section">
        <table>
            <tr>
                <th>ID</th>
                <th>Promotion Name</th>
                <th>Discount</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Description</th>
                <th>Action</th>
            </tr>

            <%
            if (promoList == null || promoList.isEmpty()) {
            %>
                <tr>
                    <td colspan="7" class="empty">No promotion found.</td>
                </tr>
            <%
            } else {
                for (DiscountsAndPromotions p : promoList) {
            %>
                <tr>
                    <td><%= p.getPromoID() %></td>
                    <td><%= p.getPromoName() %></td>
                    <td><%= p.getDiscountPercent() %>%</td>
                    <td><%= p.getStartDate() %></td>
                    <td><%= p.getEndDate() %></td>
                    <td><%= p.getPromoDesc() %></td>
                    <td>
                        <a class="action-btn edit"
                           href="<%= request.getContextPath() %>/PromotionServlet?action=edit&promoID=<%= p.getPromoID() %>">
                            Edit
                        </a>

                        <% if (p.isDeletionBlocked()) { %>
                            <button type="button"
                                    class="action-btn delete-disabled"
                                    disabled
                                    title="Delete is disabled because this promotion is connected to payment records.">
                                Delete
                            </button>
                        <% } else { %>
                            <form class="delete-form"
                                  action="<%= request.getContextPath() %>/PromotionServlet"
                                  method="post"
                                  data-swal-delete
                                  data-swal-title="Confirm Deletion"
                                  data-swal-text="Delete this promotion? This action cannot be undone.">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="promoID" value="<%= p.getPromoID() %>">
                                <button type="submit" class="action-btn delete">Delete</button>
                            </form>
                        <% } %>
                    </td>
                </tr>
            <%
                }
            }
            %>
        </table>
    </div>

</div>

<script>
const promotionPageParams = new URLSearchParams(window.location.search);

if (promotionPageParams.get('deleteSuccess') === 'true') {
    Swal.fire({
        icon: 'success',
        title: 'Promotion Deleted',
        text: 'The promotion was deleted successfully.',
        confirmButtonColor: '#2563eb'
    });
} else if (promotionPageParams.get('deleteBlocked') === 'true') {
    Swal.fire({
        icon: 'warning',
        title: 'Delete Disabled',
        text: 'This promotion is connected to payment records and cannot be deleted.',
        confirmButtonColor: '#2563eb'
    });
} else if (promotionPageParams.get('deleteFailed') === 'true') {
    Swal.fire({
        icon: 'error',
        title: 'Unable to Delete Promotion',
        text: 'The promotion could not be deleted.',
        confirmButtonColor: '#2563eb'
    });
} else if (promotionPageParams.get('invalidDeleteRequest') === 'true') {
    Swal.fire({
        icon: 'warning',
        title: 'Invalid Request',
        text: 'Please use the Delete button to remove a promotion.',
        confirmButtonColor: '#2563eb'
    });
}
</script>

</body>
</html>
