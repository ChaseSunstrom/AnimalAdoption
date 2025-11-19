<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shelter Dashboard - Animal Adoption</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>

    <main class="shelter-dashboard-page">
        <div class="container">
            <div class="dashboard-header">
                <h1>${shelter.shelterName}</h1>
                <p>${shelter.city}, ${shelter.state}</p>
                <c:if test="${!shelter.verified}">
                    <div class="alert alert-warning">
                        Your shelter is pending verification. You can view your dashboard, but you'll need to be verified before adding animals.
                    </div>
                </c:if>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-number">${availableCount}</div>
                    <div class="stat-label">Available</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number">${pendingCount}</div>
                    <div class="stat-label">Pending</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number">${adoptedCount}</div>
                    <div class="stat-label">Adopted</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number">${animals.size()}</div>
                    <div class="stat-label">Total Animals</div>
                </div>
            </div>

            <div class="dashboard-actions">
                <c:if test="${shelter.verified}">
                    <a href="${pageContext.request.contextPath}/shelter/animals/add" class="btn btn-primary">
                        + Add New Animal
                    </a>
                </c:if>
                <a href="${pageContext.request.contextPath}/applications" class="btn btn-secondary">
                    View Applications
                </a>
                <a href="${pageContext.request.contextPath}/change-password" class="btn btn-secondary">
                    Change Password
                </a>
            </div>

            <section class="animals-section">
                <h2>Your Animals</h2>
                
                <c:choose>
                    <c:when test="${empty animals}">
                        <div class="no-results">
                            <p>You haven't added any animals yet.</p>
                            <c:if test="${shelter.verified}">
                                <a href="${pageContext.request.contextPath}/shelter/animals/add" class="btn btn-primary">
                                    Add Your First Animal
                                </a>
                            </c:if>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="animals-table-container">
                            <table class="animals-table">
                                <thead>
                                    <tr>
                                        <th>Photo</th>
                                        <th>Name</th>
                                        <th>Species</th>
                                        <th>Breed</th>
                                        <th>Age</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="animal" items="${animals}">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty animal.primaryImageUrl}">
                                                        <img src="${animal.primaryImageUrl}" alt="${animal.name}" class="table-thumbnail">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="table-thumbnail-placeholder">📷</div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><strong>${animal.name}</strong></td>
                                            <td>${animal.species}</td>
                                            <td>${animal.breed}</td>
                                            <td>${animal.formattedAge}</td>
                                            <td>
                                                <span class="status-badge status-${animal.status}">
                                                    ${animal.status}
                                                </span>
                                            </td>
                                            <td>
                                                <div class="table-actions">
                                                    <a href="${pageContext.request.contextPath}/animal-detail?id=${animal.animalId}" 
                                                       class="btn btn-small btn-secondary">View</a>
                                                    <a href="${pageContext.request.contextPath}/shelter/animals/edit?id=${animal.animalId}" 
                                                       class="btn btn-small btn-primary">Edit</a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>
        </div>
    </main>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>




