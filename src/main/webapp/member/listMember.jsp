<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="bean.Member" %>

<%
List<Member> memberList = (List<Member>) request.getAttribute("memberList");
Date today = new Date();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Member List</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payment.css">
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
        <a href="<%= request.getContextPath() %>/MemberServlet?action=list" class="active">Members</a>
        <a href="<%= request.getContextPath() %>/PaymentController?action=list">Payments</a>
        <a href="<%= request.getContextPath() %>/PromotionServlet?action=list">Promotions</a>
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
            <h1>Member Management</h1>
            <p>Total Members: <%= memberList == null ? 0 : memberList.size() %></p>
        </div>
        <a class="btn-add" href="${pageContext.request.contextPath}/MemberServlet?action=add">+ Add Member</a>
    </div>

    <div class="section table-wrapper">
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Member Type</th>
                <th>Expired Date</th>
                <th>Staff ID</th>
                <th>Action</th>
            </tr>

            <%
            if (memberList == null || memberList.isEmpty()) {
            %>
                <tr><td colspan="8" class="empty">No member found.</td></tr>
            <%
            } else {
                for (Member m : memberList) {
                    boolean notActive = m.getExpiredDate() == null;
                    boolean expired = !notActive && m.getExpiredDate().before(today);
            %>
                <tr>
                    <td><strong><%= m.getMemberID() %></strong></td>
                    <td><%= m.getMemberName() %></td>
                    <td><%= m.getMemberEmail() %></td>
                    <td><%= m.getMemberPhone() %></td>
                    <td><span class="type-badge"><%= m.getMemberType() %></span></td>
                    <td>
                        <% if (notActive) { %>
                            <span class="expiry-badge inactive">Not active yet</span>
                        <% } else if (expired) { %>
                            <span class="expiry-date"><%= m.getExpiredDate() %></span>
                            <span class="expiry-badge expired">Expired</span>
                        <% } else { %>
                            <span class="expiry-date"><%= m.getExpiredDate() %></span>
                            <span class="expiry-badge active">Active</span>
                        <% } %>
                    </td>
                    <td><%= m.getStaffID() %></td>
                    <td>
                        <a class="action-btn edit"
                           href="${pageContext.request.contextPath}/MemberServlet?action=edit&memberID=<%= m.getMemberID() %>">
                            Edit
                        </a>

                        <% if (m.isDeletionBlocked()) { %>
                            <button type="button"
                                    class="action-btn delete-disabled"
                                    disabled
                                    title="Delete is disabled because this member is connected to payment records.">
                                Delete
                            </button>
                        <% } else { %>
                            <form class="delete-form"
                                  action="<%= request.getContextPath() %>/MemberServlet"
                                  method="post"
                                  data-swal-delete
                                  data-swal-title="Confirm Deletion"
                                  data-swal-text="Delete this member? This action cannot be undone.">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="memberID" value="<%= m.getMemberID() %>">
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
const memberPageParams = new URLSearchParams(window.location.search);

if (memberPageParams.get('deleteSuccess') === 'true') {
    Swal.fire({
        icon: 'success',
        title: 'Member Deleted',
        text: 'The member was deleted successfully.',
        confirmButtonColor: '#2563eb'
    });
} else if (memberPageParams.get('deleteBlocked') === 'true') {
    Swal.fire({
        icon: 'warning',
        title: 'Delete Disabled',
        text: 'This member is connected to payment records and cannot be deleted.',
        confirmButtonColor: '#2563eb'
    });
} else if (memberPageParams.get('deleteFailed') === 'true') {
    Swal.fire({
        icon: 'error',
        title: 'Unable to Delete Member',
        text: 'The member could not be deleted.',
        confirmButtonColor: '#2563eb'
    });
} else if (memberPageParams.get('invalidDeleteRequest') === 'true') {
    Swal.fire({
        icon: 'warning',
        title: 'Invalid Request',
        text: 'Please use the Delete button to remove a member.',
        confirmButtonColor: '#2563eb'
    });
}
</script>

</body>
</html>
