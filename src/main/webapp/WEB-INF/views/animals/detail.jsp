<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${animal.name} - Animal Adoption</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>

    <main class="animal-detail-page">
        <div class="container">
            <div class="detail-layout">
                <div class="detail-main">
                    <div class="animal-images">
                        <c:choose>
                            <c:when test="${not empty animal.primaryImageUrl}">
                                <img src="${animal.primaryImageUrl}" alt="${animal.name}" class="main-image">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/images/placeholder-pet.jpg" alt="${animal.name}" class="main-image">
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="animal-details">
                        <div class="detail-header">
                            <h1>${animal.name}</h1>
                            <c:if test="${not empty matchScore}">
                                <div class="match-badge match-${matchScore.matchLevel}">
                                    ${matchScore.matchPercentage}% Match
                                    <span class="match-label">${matchScore.matchLevel}</span>
                                </div>
                            </c:if>
                        </div>

                        <div class="quick-info">
                            <div class="info-item">
                                <strong>Species:</strong> ${animal.species}
                            </div>
                            <div class="info-item">
                                <strong>Breed:</strong> ${animal.breed}
                            </div>
                            <div class="info-item">
                                <strong>Age:</strong> ${animal.formattedAge}
                            </div>
                            <div class="info-item">
                                <strong>Gender:</strong> ${animal.gender}
                            </div>
                            <div class="info-item">
                                <strong>Size:</strong> ${animal.size}
                            </div>
                            <c:if test="${animal.weightLbs > 0}">
                                <div class="info-item">
                                    <strong>Weight:</strong> <fmt:formatNumber value="${animal.weightLbs}" maxFractionDigits="1"/> lbs
                                </div>
                            </c:if>
                        </div>

                        <div class="description-section">
                            <h3>About ${animal.name}</h3>
                            <p>${animal.description}</p>
                        </div>

                        <div class="compatibility-section">
                            <h3>Compatibility & Behavior</h3>
                            <div class="compatibility-grid">
                                <div class="compat-item ${animal.goodWithKids ? 'yes' : 'no'}">
                                    <span class="icon">${animal.goodWithKids ? '✓' : '✗'}</span>
                                    Good with kids
                                </div>
                                <div class="compat-item ${animal.goodWithDogs ? 'yes' : 'no'}">
                                    <span class="icon">${animal.goodWithDogs ? '✓' : '✗'}</span>
                                    Good with dogs
                                </div>
                                <div class="compat-item ${animal.goodWithCats ? 'yes' : 'no'}">
                                    <span class="icon">${animal.goodWithCats ? '✓' : '✗'}</span>
                                    Good with cats
                                </div>
                            </div>

                            <c:if test="${not empty animal.behavioralNotes}">
                                <div class="notes">
                                    <h4>Behavioral Notes</h4>
                                    <p>${animal.behavioralNotes}</p>
                                </div>
                            </c:if>
                        </div>

                        <div class="requirements-section">
                            <h3>Requirements</h3>
                            <ul class="requirements-list">
                                <li>Energy Level: <strong>${animal.energyLevel}</strong></li>
                                <c:if test="${animal.requiresYard}">
                                    <li class="requirement-needed">Requires a yard</li>
                                </c:if>
                                <c:if test="${animal.requiresExperiencedOwner}">
                                    <li class="requirement-needed">Requires experienced owner</li>
                                </c:if>
                            </ul>
                        </div>

                        <c:if test="${not empty animal.medicalNotes}">
                            <div class="medical-section">
                                <h3>Medical Information</h3>
                                <p>${animal.medicalNotes}</p>
                            </div>
                        </c:if>
                    </div>
                </div>

                <aside class="detail-sidebar">
                    <div class="sidebar-card">
                        <c:if test="${not empty animal.shelter}">
                            <h3>Shelter Information</h3>
                            <p><strong>${animal.shelter.shelterName}</strong></p>
                            <p>📍 ${animal.shelter.city}, ${animal.shelter.state}</p>
                            <c:if test="${not empty animal.shelter.phone}">
                                <p>📞 ${animal.shelter.phone}</p>
                            </c:if>
                            <c:if test="${not empty animal.shelter.email}">
                                <p>✉️ ${animal.shelter.email}</p>
                            </c:if>
                        </c:if>
                    </div>

                    <div class="sidebar-card action-card">
                        <c:choose>
                            <c:when test="${empty sessionScope.user}">
                                <p>Please log in to adopt this pet</p>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-block">
                                    Login to Apply
                                </a>
                            </c:when>
                            <c:when test="${sessionScope.userType eq 'adopter'}">
                                <c:choose>
                                    <c:when test="${hasApplied}">
                                        <div class="alert alert-info">
                                            You have already applied for this pet
                                        </div>
                                        <a href="${pageContext.request.contextPath}/applications" class="btn btn-secondary btn-block">
                                            View My Applications
                                        </a>
                                    </c:when>
                                    <c:when test="${animal.status ne 'available'}">
                                        <div class="alert alert-warning">
                                            This pet is no longer available
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/applications/new?animalId=${animal.animalId}"
                                           class="btn btn-primary btn-block btn-large">
                                            Apply to Adopt ${animal.name}
                                        </a>
                                        <c:if test="${not empty matchScore}">
                                            <div class="match-details">
                                                <h4>Why this is a ${matchScore.matchLevel}:</h4>
                                                <ul class="match-factors">
                                                    <c:if test="${not empty matchScore.scoreDetails.compatibility}">
                                                        <li>Compatibility: <fmt:formatNumber value="${matchScore.scoreDetails.compatibility}" maxFractionDigits="0"/>%</li>
                                                    </c:if>
                                                    <c:if test="${not empty matchScore.scoreDetails.experience}">
                                                        <li>Experience: <fmt:formatNumber value="${matchScore.scoreDetails.experience}" maxFractionDigits="0"/>%</li>
                                                    </c:if>
                                                    <c:if test="${not empty matchScore.scoreDetails.home}">
                                                        <li>Home Environment: <fmt:formatNumber value="${matchScore.scoreDetails.home}" maxFractionDigits="0"/>%</li>
                                                    </c:if>
                                                    <c:if test="${not empty matchScore.scoreDetails.activity}">
                                                        <li>Activity Match: <fmt:formatNumber value="${matchScore.scoreDetails.activity}" maxFractionDigits="0"/>%</li>
                                                    </c:if>
                                                </ul>
                                            </div>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <p>Shelter accounts cannot apply for adoption</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="sidebar-card">
                        <a href="${pageContext.request.contextPath}/animals" class="btn btn-secondary btn-block">
                            ← Back to Browse
                        </a>
                    </div>
                </aside>
            </div>
        </div>
    </main>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>
