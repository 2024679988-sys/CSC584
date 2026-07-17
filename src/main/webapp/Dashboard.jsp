<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="bean.DiscountsAndPromotions" %>

<%!
private String escapeHtml(String value) {
    if (value == null) return "";
    return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
}

private String formatDate(String value) {
    if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value)) {
        return "-";
    }

    try {
        java.sql.Date date = java.sql.Date.valueOf(value);
        return new SimpleDateFormat("dd MMM yyyy").format(date);
    } catch (Exception e) {
        return value;
    }
}

private String getStatusClass(String status) {
    if (status == null) return "status-neutral";

    String normalized = status.trim().toUpperCase();

    if ("COMPLETED".equals(normalized) || "ACTIVE".equals(normalized)) {
        return "status-success";
    }

    if ("PENDING".equals(normalized)
            || "IN PROGRESS".equals(normalized)
            || "EXPIRING SOON".equals(normalized)) {
        return "status-warning";
    }

    if ("CANCELLED".equals(normalized)
            || "EXPIRED".equals(normalized)
            || "EXPIRES TODAY".equals(normalized)) {
        return "status-danger";
    }

    return "status-neutral";
}
%>

<%
if (session == null || session.getAttribute("staffID") == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
}

String staffName = (String) session.getAttribute("staffName");
if (staffName == null || staffName.trim().isEmpty()) {
    staffName = "Staff";
}

Integer totalMembers = (Integer) request.getAttribute("totalMembers");
Integer activeMembers = (Integer) request.getAttribute("activeMembers");
Integer totalPayments = (Integer) request.getAttribute("totalPayments");
Double totalRevenue = (Double) request.getAttribute("totalRevenue");
Integer totalEquipment = (Integer) request.getAttribute("totalEquipment");
Integer pendingMaintenance = (Integer) request.getAttribute("pendingMaintenance");

DiscountsAndPromotions currentPromotion =
        (DiscountsAndPromotions) request.getAttribute("currentPromotion");

List<String[]> recentActivities =
        (List<String[]>) request.getAttribute("recentActivities");

List<String[]> upcomingExpiries =
        (List<String[]>) request.getAttribute("upcomingExpiries");

DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");
SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd MMM yyyy");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dashboard</title>
<link rel="stylesheet"
      href="<%= request.getContextPath() %>/css/dashboard.css?v=20260716-1">
</head>
<body>

<div class="sidebar">
    <div class="logo">FitCore</div>

    <div class="menu">
        <a href="<%= request.getContextPath() %>/DashboardServlet"
           class="active">Dashboard</a>
        <a href="<%= request.getContextPath() %>/MemberServlet?action=list">Members</a>
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

<main class="main">

    <section class="header">
        <div>
            <h1>Dashboard</h1>
            <p>Welcome back, <strong><%= escapeHtml(staffName) %></strong>.</p>
        </div>
        <a class="header-action"
           href="<%= request.getContextPath() %>/PaymentController?action=add">
            + New Payment
        </a>
    </section>

    <section class="cards">
        <article class="card">
            <div class="card-label">Total Members</div>
            <div class="card-value"><%= totalMembers == null ? 0 : totalMembers %></div>
            <div class="card-caption">All registered members</div>
        </article>

        <article class="card">
            <div class="card-label">Active Members</div>
            <div class="card-value"><%= activeMembers == null ? 0 : activeMembers %></div>
            <div class="card-caption">Membership not expired</div>
        </article>

        <article class="card">
            <div class="card-label">Total Payments</div>
            <div class="card-value"><%= totalPayments == null ? 0 : totalPayments %></div>
            <div class="card-caption">Payment records created</div>
        </article>

        <article class="card">
            <div class="card-label">Total Revenue</div>
            <div class="card-value money">
                RM <%= moneyFormat.format(totalRevenue == null ? 0.0 : totalRevenue) %>
            </div>
            <div class="card-caption">After promotion discounts</div>
        </article>

        <article class="card">
            <div class="card-label">Equipment</div>
            <div class="card-value"><%= totalEquipment == null ? 0 : totalEquipment %></div>
            <div class="card-caption">All equipment records</div>
        </article>

        <article class="card">
            <div class="card-label">Pending Maintenance</div>
            <div class="card-value"><%= pendingMaintenance == null ? 0 : pendingMaintenance %></div>
            <div class="card-caption">Pending or in progress</div>
        </article>
    </section>

    <section class="promotion-banner">
        <% if (currentPromotion == null) { %>
            <div>
                <span class="eyebrow">Current Promotion</span>
                <h2>No active promotion</h2>
                <p>Create or activate a promotion to display it during payment.</p>
            </div>
            <a href="<%= request.getContextPath() %>/PromotionServlet?action=add"
               class="secondary-action">Add Promotion</a>
        <% } else { %>
            <div>
                <span class="eyebrow">Current Promotion</span>
                <h2><%= escapeHtml(currentPromotion.getPromoName()) %></h2>
                <p>
                    <%= escapeHtml(currentPromotion.getPromoDesc()) %>
                    Ends on
                    <strong><%= displayDateFormat.format(currentPromotion.getEndDate()) %></strong>.
                </p>
            </div>
            <div class="discount-pill">
                <%= moneyFormat.format(currentPromotion.getDiscountPercent()) %>% OFF
            </div>
        <% } %>
    </section>

    <section class="dashboard-grid">
        <div class="section activity-section">
            <div class="section-heading">
                <div>
                    <h2>Recent Activities</h2>
                    <p>Latest payments and equipment maintenance records.</p>
                </div>
            </div>

            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>Activity</th>
                            <th>Details</th>
                            <th>Date</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (recentActivities == null || recentActivities.isEmpty()) { %>
                        <tr>
                            <td colspan="4" class="empty">No recent activity found.</td>
                        </tr>
                    <% } else {
                        for (String[] activity : recentActivities) {
                            String status = activity.length > 3 ? activity[3] : "-";
                    %>
                        <tr>
                            <td><strong><%= escapeHtml(activity[0]) %></strong></td>
                            <td class="details-cell"><%= escapeHtml(activity[1]) %></td>
                            <td class="date-cell"><%= escapeHtml(formatDate(activity[2])) %></td>
                            <td>
                                <span class="status <%= getStatusClass(status) %>">
                                    <%= escapeHtml(status) %>
                                </span>
                            </td>
                        </tr>
                    <%  }
                       } %>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="section expiry-section">
            <div class="section-heading">
                <div>
                    <h2>Upcoming Expiries</h2>
                    <p>Memberships ending within 45 days.</p>
                </div>
                <a href="<%= request.getContextPath() %>/MemberServlet?action=list"
                   class="text-link">View members</a>
            </div>

            <div class="expiry-list">
                <% if (upcomingExpiries == null || upcomingExpiries.isEmpty()) { %>
                    <div class="empty-card">No membership is expiring soon.</div>
                <% } else {
                    for (String[] expiry : upcomingExpiries) {
                        String expiryStatus = expiry.length > 3
                                ? expiry[3] : "EXPIRING SOON";
                %>
                    <article class="expiry-item">
                        <div class="expiry-main">
                            <strong><%= escapeHtml(expiry[0]) %></strong>
                            <span><%= escapeHtml(expiry[1]) %> member</span>
                        </div>
                        <div class="expiry-meta">
                            <span class="expiry-date"><%= escapeHtml(formatDate(expiry[2])) %></span>
                            <span class="status <%= getStatusClass(expiryStatus) %>">
                                <%= escapeHtml(expiryStatus) %>
                            </span>
                        </div>
                    </article>
                <%  }
                   } %>
            </div>
        </div>
    </section>

</main>

</body>
</html>
