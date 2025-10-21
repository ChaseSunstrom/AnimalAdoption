<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Available Pets - Animal Adoption</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>

    <main class="browse-page">
        <div class="container">
            <h1>Find Your Perfect Pet</h1>

            <div class="content-layout">
                <aside class="filters-sidebar">
                    <h3>Filter Results</h3>
                    <form action="${pageContext.request.contextPath}/animals" method="get">
                        <div class="filter-group">
                            <label>Species</label>
                            <select name="species">
                                <option value="">All</option>
                                <option value="dog" ${criteria.species eq 'dog' ? 'selected' : ''}>Dog</option>
                                <option value="cat" ${criteria.species eq 'cat' ? 'selected' : ''}>Cat</option>
                                <option value="bird" ${criteria.species eq 'bird' ? 'selected' : ''}>Bird</option>
                                <option value="rabbit" ${criteria.species eq 'rabbit' ? 'selected' : ''}>Rabbit</option>
                            </select>
                        </div>

                        <div class="filter-group">
                            <label>Size</label>
                            <select name="size">
                                <option value="">All</option>
                                <option value="small" ${criteria.size eq 'small' ? 'selected' : ''}>Small</option>
                                <option value="medium" ${criteria.size eq 'medium' ? 'selected' : ''}>Medium</option>
                                <option value="large" ${criteria.size eq 'large' ? 'selected' : ''}>Large</option>
                                <option value="extra_large" ${criteria.size eq 'extra_large' ? 'selected' : ''}>Extra Large</option>
                            </select>
                        </div>

                        <div class="filter-group">
                            <label>Gender</label>
                            <select name="gender">
                                <option value="">All</option>
                                <option value="male" ${criteria.gender eq 'male' ? 'selected' : ''}>Male</option>
                                <option value="female" ${criteria.gender eq 'female' ? 'selected' : ''}>Female</option>
                            </select>
                        </div>

                        <div class="filter-group">
                            <label>Compatibility</label>
                            <label class="checkbox-label">
                                <input type="checkbox" name="goodWithKids" value="true"
                                       ${criteria.goodWithKids ? 'checked' : ''}>
                                Good with kids
                            </label>
                            <label class="checkbox-label">
                                <input type="checkbox" name="goodWithDogs" value="true"
                                       ${criteria.goodWithDogs ? 'checked' : ''}>
                                Good with dogs
                            </label>
                            <label class="checkbox-label">
                                <input type="checkbox" name="goodWithCats" value="true"
                                       ${criteria.goodWithCats ? 'checked' : ''}>
                                Good with cats
                            </label>
                        </div>

                        <div class="filter-group">
                            <label>Location</label>
                            <input type="text" name="city" placeholder="City" value="${criteria.city}">
                            <input type="text" name="state" placeholder="State" value="${criteria.state}" maxlength="2">
                        </div>

                        <button type="submit" class="btn btn-primary btn-block">Apply Filters</button>
                        <a href="${pageContext.request.contextPath}/animals" class="btn btn-secondary btn-block">Clear All</a>
                    </form>
                </aside>

                <div class="animals-content">
                    <div class="results-header">
                        <p>Found <strong>${totalCount}</strong> available pets</p>
                    </div>

                    <c:choose>
                        <c:when test="${empty animals}">
                            <div class="no-results">
                                <p>No animals found matching your criteria. Try adjusting your filters.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="animal-grid">
                                <c:forEach var="animal" items="${animals}">
                                    <div class="animal-card">
                                        <div class="animal-image">
                                            <c:choose>
                                                <c:when test="${not empty animal.primaryImageUrl}">
                                                    <img src="${animal.primaryImageUrl}" alt="${animal.name}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${pageContext.request.contextPath}/images/placeholder-pet.jpg" alt="${animal.name}">
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="animal-info">
                                            <h3>${animal.name}</h3>
                                            <p class="animal-meta">${animal.breed} • ${animal.formattedAge}</p>
                                            <p class="animal-meta">${animal.size} • ${animal.gender}</p>
                                            <c:if test="${not empty animal.shelter}">
                                                <p class="shelter-info">
                                                    📍 ${animal.shelter.city}, ${animal.shelter.state}
                                                </p>
                                            </c:if>
                                            <div class="animal-badges">
                                                <c:if test="${animal.goodWithKids}">
                                                    <span class="badge">Good with kids</span>
                                                </c:if>
                                                <c:if test="${animal.goodWithDogs}">
                                                    <span class="badge">Good with dogs</span>
                                                </c:if>
                                                <c:if test="${animal.goodWithCats}">
                                                    <span class="badge">Good with cats</span>
                                                </c:if>
                                            </div>
                                            <a href="${pageContext.request.contextPath}/animal-detail?id=${animal.animalId}"
                                               class="btn btn-primary btn-block">View Details</a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                            <c:if test="${totalPages > 1}">
                                <div class="pagination">
                                    <c:if test="${currentPage > 1}">
                                        <a href="?page=${currentPage - 1}&species=${criteria.species}&size=${criteria.size}&gender=${criteria.gender}"
                                           class="btn btn-secondary">Previous</a>
                                    </c:if>

                                    <span class="page-info">Page ${currentPage} of ${totalPages}</span>

                                    <c:if test="${currentPage < totalPages}">
                                        <a href="?page=${currentPage + 1}&species=${criteria.species}&size=${criteria.size}&gender=${criteria.gender}"
                                           class="btn btn-secondary">Next</a>
                                    </c:if>
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </main>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>
