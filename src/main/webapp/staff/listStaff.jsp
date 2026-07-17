<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="bean.Staff" %>

<%
List<Staff> staffList = (List<Staff>) request.getAttribute("staffList");

Boolean isManagerObj = (Boolean) request.getAttribute("isManager");
boolean isManager = isManagerObj != null && isManagerObj;

String loginStaffID = (String) request.getAttribute("loginStaffID");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Staff Management</title>
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

    <div class="header header-list">
        <div>
            <h1>Staff Management</h1>
            <p>Total Staff: <%= staffList == null ? 0 : staffList.size() %></p>
        </div>

        <% if (isManager) { %>
            <a class="btn-add" href="<%= request.getContextPath() %>/StaffServlet?action=add">
                + Add Staff
            </a>
        <% } %>
    </div>

    <div class="section">
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Hire Date</th>
                <th>Role</th>
                <th>Username</th>
                <th>Action</th>
            </tr>

            <%
            if (staffList == null || staffList.isEmpty()) {
            %>
                <tr>
                    <td colspan="8" class="empty">No staff found.</td>
                </tr>
            <%
            } else {
                for (Staff s : staffList) {
            %>
                <tr>
                    <td><%= s.getStaffID() %></td>
                    <td><%= s.getStaffName() %></td>
                    <td><%= s.getStaffEmail() %></td>
                    <td><%= s.getStaffPhone() %></td>
                    <td><%= s.getHireDate() %></td>
                    <td><%= s.getStaffRole() %></td>
                    <td><%= s.getStaffUsername() %></td>
                    <td>
                        <% if (isManager || s.getStaffID().equals(loginStaffID)) { %>
                            <a class="action-btn edit"
                               href="<%= request.getContextPath() %>/StaffServlet?action=edit&staffID=<%= s.getStaffID() %>">
                               Edit
                            </a>
                        <% } %>

                        <% if (isManager || s.getStaffID().equals(loginStaffID)) {
                               boolean ownAccount = s.getStaffID().equals(loginStaffID);

                               if (s.isDeletionBlocked()) { %>
                                   <button type="button"
                                           class="action-btn delete-disabled"
                                           disabled
                                           title="Delete is disabled because this staff account is connected to member, payment, or maintenance records.">
                                       Delete
                                   </button>
                            <% } else { %>
                                <form class="delete-form"
                                      action="<%= request.getContextPath() %>/StaffServlet"
                                      method="post"
                                      data-swal-delete
                                      data-swal-title="<%= ownAccount ? "Delete Your Account?" : "Confirm Deletion" %>"
                                      data-swal-text="<%= ownAccount
                                              ? "Your account will be permanently deleted and you will be logged out immediately."
                                              : "Delete this staff account? This action cannot be undone." %>">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="staffID" value="<%= s.getStaffID() %>">
                                    <button type="submit" class="action-btn delete">
                                        <%= ownAccount ? "Delete My Account" : "Delete" %>
                                    </button>
                                </form>
                            <% }
                               } %>
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
const staffPageParams = new URLSearchParams(window.location.search);

if (staffPageParams.get('deleteSuccess') === 'true') {
    Swal.fire({
        icon: 'success',
        title: 'Account Deleted',
        text: 'The staff account was deleted successfully.',
        confirmButtonColor: '#2563eb'
    });
} else if (staffPageParams.get('deleteBlocked') === 'true') {
    Swal.fire({
        icon: 'warning',
        title: 'Delete Disabled',
        text: 'This staff account is connected to member, payment, or maintenance records and cannot be deleted.',
        confirmButtonColor: '#2563eb'
    });
} else if (staffPageParams.get('deleteFailed') === 'true') {
    Swal.fire({
        icon: 'error',
        title: 'Unable to Delete Account',
        text: 'The staff account could not be deleted.',
        confirmButtonColor: '#2563eb'
    });
} else if (staffPageParams.get('accessDenied') === 'true') {
    Swal.fire({
        icon: 'error',
        title: 'Access Denied',
        text: 'You can delete only your own account unless you are a manager.',
        confirmButtonColor: '#2563eb'
    });
} else if (staffPageParams.get('invalidDeleteRequest') === 'true') {
    Swal.fire({
        icon: 'warning',
        title: 'Invalid Request',
        text: 'Please use the Delete button to remove an account.',
        confirmButtonColor: '#2563eb'
    });
}
</script>

</body>
</html>