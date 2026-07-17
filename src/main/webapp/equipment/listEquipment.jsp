<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Equipment Management</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/equipment.css">

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/js/sweetalert-helper.js"
        defer></script>

<style>
.action-cell {
    white-space: nowrap;
}

/* Same disabled Delete button design used on Staff page */
.action-btn {
    padding: 7px 11px;
    border-radius: 6px;
    border: none;
    font-family: inherit;
    font-size: 13px;
    color: white;
    display: inline-block;
    margin-bottom: 3px;
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
        <a href="${pageContext.request.contextPath}/DashboardServlet">
            Dashboard
        </a>

        <a href="${pageContext.request.contextPath}/MemberServlet?action=list">
            Members
        </a>

        <a href="${pageContext.request.contextPath}/PaymentController?action=list">
            Payments
        </a>

        <a href="${pageContext.request.contextPath}/PromotionServlet?action=list">
            Promotions
        </a>

        <a href="${pageContext.request.contextPath}/EquipmentServlet"
           class="active">
            Equipment
        </a>

        <a href="${pageContext.request.contextPath}/MaintenanceServlet?action=list">
            Maintenance
        </a>

        <a href="${pageContext.request.contextPath}/ReportServlet">
            Reports
        </a>

        <a href="${pageContext.request.contextPath}/StaffServlet?action=list">
            Staff
        </a>
    </div>

    <div class="logout">
        <a href="${pageContext.request.contextPath}/LogoutServlet">
            Logout
        </a>
    </div>

</div>

<div class="main">

    <div class="header">

        <div>
            <h1>Equipment Management</h1>

            <p>
                Total Equipment:
                <strong>
                    <c:out value="${empty equipmentList ? 0 : equipmentList.size()}"/>
                </strong>
            </p>
        </div>

        <a href="${pageContext.request.contextPath}/EquipmentServlet?action=add"
           class="btn-add">
            + Add Equipment
        </a>

    </div>

    <div class="section">

        <table>

            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Purchase Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>

            <tbody>

                <c:choose>

                    <c:when test="${empty equipmentList}">
                        <tr>
                            <td colspan="6" class="empty">
                                No equipment found.
                            </td>
                        </tr>
                    </c:when>

                    <c:otherwise>

                        <c:forEach var="equipment"
                                   items="${equipmentList}">

                            <tr>

                                <td>
                                    <strong>
                                        <c:out value="${equipment.equipmentID}"/>
                                    </strong>
                                </td>

                                <td>
                                    <c:out value="${equipment.equipmentName}"/>
                                </td>

                                <td>
                                    <c:out value="${equipment.category}"/>
                                </td>

                                <td>
                                    <fmt:formatDate
                                        value="${equipment.purchaseDate}"
                                        pattern="dd-MM-yyyy"/>
                                </td>

                                <td>
                                    <c:out value="${equipment.equStatus}"/>
                                </td>

                                <td class="action-cell">

                                    <a href="${pageContext.request.contextPath}/EquipmentServlet?action=edit&id=${equipment.equipmentID}"
                                       class="btn-edit">
                                        Edit
                                    </a>

                                    <c:choose>

                                        <%-- Equipment has no integrity constraint --%>
                                        <c:when test="${equipment.deletable}">

                                            <a href="${pageContext.request.contextPath}/EquipmentServlet?action=delete&id=${equipment.equipmentID}"
                                               class="btn-delete"
                                               data-swal-delete
                                               data-swal-title="Confirm Deletion"
                                               data-swal-text="Delete this equipment? This action cannot be undone.">
                                                Delete
                                            </a>

                                        </c:when>

                                        <%-- Equipment is referenced by maintenance --%>
                                        <c:otherwise>

                                            <button type="button"
                                                    class="action-btn delete-disabled"
                                                    disabled
                                                    title="Delete is disabled because this equipment is connected to maintenance records.">
                                                Delete
                                            </button>

                                        </c:otherwise>

                                    </c:choose>

                                </td>

                            </tr>

                        </c:forEach>

                    </c:otherwise>

                </c:choose>

            </tbody>

        </table>

    </div>

</div>

<c:if test="${param.deleteResult == 'blocked'}">
<script>
document.addEventListener("DOMContentLoaded", function () {
    Swal.fire({
        icon: "warning",
        title: "Delete Disabled",
        text: "This equipment is connected to maintenance records and cannot be deleted.",
        confirmButtonColor: "#2563eb",
        confirmButtonText: "OK"
    });
});
</script>
</c:if>

<c:if test="${param.deleteResult == 'deleted'}">
<script>
document.addEventListener("DOMContentLoaded", function () {
    Swal.fire({
        icon: "success",
        title: "Equipment Deleted",
        text: "The equipment was deleted successfully.",
        confirmButtonColor: "#2563eb",
        confirmButtonText: "OK"
    });
});
</script>
</c:if>

<c:if test="${param.deleteResult == 'failed'}">
<script>
document.addEventListener("DOMContentLoaded", function () {
    Swal.fire({
        icon: "error",
        title: "Unable to Delete Equipment",
        text: "The equipment could not be deleted.",
        confirmButtonColor: "#2563eb",
        confirmButtonText: "OK"
    });
});
</script>
</c:if>

</body>
</html>