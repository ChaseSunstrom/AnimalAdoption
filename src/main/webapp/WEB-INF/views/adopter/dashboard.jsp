<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Dashboard - Animal Adoption</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>

    <main class="dashboard-page">
        <div class="container">
            <div class="dashboard-header">
                <h1>Welcome, ${sessionScope.userName}!</h1>
                <p>Find your perfect companion from our personalized recommendations</p>
            </div>

            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success">
                    ${sessionScope.successMessage}
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>

            <c:if test="${not empty recommendationsError}">
                <div class="alert alert-warning">
                    ${recommendationsError}
                </div>
            </c:if>

            <div class="dashboard-actions">
                <a href="${pageContext.request.contextPath}/animals" class="btn btn-primary">
                    Browse All Animals
                </a>
                <a href="${pageContext.request.contextPath}/profile/edit" class="btn btn-secondary">
                    Edit My Profile
                </a>
                <a href="${pageContext.request.contextPath}/applications" class="btn btn-secondary">
                    My Applications
                </a>
            </div>

            <c:if test="${not empty recommendations}">
                <section class="recommendations-section">
                    <h2>Your Top Matches</h2>
                    <p class="section-description">
                        Based on your profile, lifestyle, and preferences, we've found these perfect companions for you!
                    </p>

                    <div class="animal-grid">
                        <c:forEach var="matchScore" items="${recommendations}">
                            <div class="animal-card">
                                <div class="animal-image">
                                    <c:choose>
                                        <c:when test="${not empty matchScore.animal.primaryImageUrl}">
                                            <img src="${matchScore.animal.primaryImageUrl}" alt="${matchScore.animal.name}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/images/placeholder-pet.jpg" alt="${matchScore.animal.name}">
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="match-overlay">
                                        <span class="match-badge">
                                            <fmt:formatNumber value="${matchScore.matchScore}" maxFractionDigits="0"/>% Match
                                        </span>
                                    </div>
                                </div>
                                <div class="animal-info">
                                    <h3>${matchScore.animal.name}</h3>
                                    <p class="animal-meta">${matchScore.animal.breed} • ${matchScore.animal.formattedAge}</p>
                                    <p class="animal-meta">${matchScore.animal.size} • ${matchScore.animal.gender}</p>
                                    <c:if test="${not empty matchScore.animal.shelter}">
                                        <p class="shelter-info">
                                            📍 ${matchScore.animal.shelter.city}, ${matchScore.animal.shelter.state}
                                        </p>
                                    </c:if>
                                    <div class="animal-badges">
                                        <c:if test="${matchScore.animal.goodWithKids}">
                                            <span class="badge">Good with kids</span>
                                        </c:if>
                                        <c:if test="${matchScore.animal.goodWithDogs}">
                                            <span class="badge">Good with dogs</span>
                                        </c:if>
                                        <c:if test="${matchScore.animal.goodWithCats}">
                                            <span class="badge">Good with cats</span>
                                        </c:if>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/animal-detail?id=${matchScore.animal.animalId}"
                                       class="btn btn-primary btn-block">View Details</a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </section>
            </c:if>

            <c:if test="${empty recommendations}">
                <div class="no-results">
                    <h2>No Recommendations Yet</h2>
                    <p>We're calculating your perfect matches based on your profile. Check back soon or browse all available animals!</p>
                    <a href="${pageContext.request.contextPath}/animals" class="btn btn-primary">
                        Browse All Animals
                    </a>
                </div>
            </c:if>
        </div>
    </main>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>




