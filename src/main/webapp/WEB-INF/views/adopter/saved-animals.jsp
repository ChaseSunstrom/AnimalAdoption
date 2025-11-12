<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Saved Animals - Animal Adoption</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../common/header.jsp" />
    
    <main class="container">
        <div class="page-header">
            <h1>❤️ My Saved Animals</h1>
            <p>Animals you've saved for later (${totalSaved})</p>
        </div>
        
        <c:if test="${empty savedAnimals}">
            <div class="empty-state">
                <div class="empty-icon">💔</div>
                <h2>No Saved Animals Yet</h2>
                <p>Browse available animals and click the heart icon to save your favorites!</p>
                <a href="${pageContext.request.contextPath}/animals/browse" class="btn btn-primary">Browse Animals</a>
            </div>
        </c:if>
        
        <c:if test="${not empty savedAnimals}">
            <div class="animals-grid">
                <c:forEach var="animal" items="${savedAnimals}">
                    <div class="animal-card" id="animal-card-${animal.animalId}">
                        <div class="animal-image">
                            <c:choose>
                                <c:when test="${not empty animal.primaryImageUrl}">
                                    <img src="${animal.primaryImageUrl}" alt="${animal.name}">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/images/placeholder-animal.jpg" alt="${animal.name}">
                                </c:otherwise>
                            </c:choose>
                            
                            <button class="favorite-btn active" 
                                    onclick="toggleFavorite(${animal.animalId}, this)"
                                    title="Remove from saved">
                                ❤️
                            </button>
                        </div>
                        
                        <div class="animal-info">
                            <h3>${animal.name}</h3>
                            
                            <div class="animal-meta">
                                <span class="badge badge-${animal.species}">${animal.species}</span>
                                <span class="animal-age">${animal.formattedAge}</span>
                                <span class="animal-gender">${animal.gender}</span>
                            </div>
                            
                            <p class="animal-breed">${animal.breed}</p>
                            
                            <c:if test="${not empty animal.shelter}">
                                <p class="animal-shelter">📍 ${animal.shelter.shelterName}</p>
                            </c:if>
                            
                            <div class="animal-actions">
                                <a href="${pageContext.request.contextPath}/animal-detail?id=${animal.animalId}" 
                                   class="btn btn-primary">View Details</a>
                                <a href="${pageContext.request.contextPath}/applications/new?animalId=${animal.animalId}" 
                                   class="btn btn-secondary">Apply to Adopt</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </main>
    
    <jsp:include page="../common/footer.jsp" />
    
    <script>
        function toggleFavorite(animalId, button) {
            const isActive = button.classList.contains('active');
            const action = isActive ? 'unsave' : 'save';
            
            fetch('${pageContext.request.contextPath}/saved-animals', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'action=' + action + '&animalId=' + animalId
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    if (action === 'unsave') {
                        // Remove the card with animation
                        const card = document.getElementById('animal-card-' + animalId);
                        card.style.opacity = '0';
                        card.style.transform = 'scale(0.8)';
                        setTimeout(() => {
                            card.remove();
                            
                            // Check if there are no more saved animals
                            const remainingCards = document.querySelectorAll('.animal-card');
                            if (remainingCards.length === 0) {
                                location.reload();
                            } else {
                                // Update count in header
                                const countElement = document.querySelector('.page-header p');
                                if (countElement) {
                                    countElement.textContent = 'Animals you\'ve saved for later (' + data.count + ')';
                                }
                            }
                        }, 300);
                    } else {
                        button.classList.add('active');
                        button.textContent = '❤️';
                    }
                } else {
                    alert('Failed to update favorite: ' + (data.message || 'Unknown error'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Failed to update favorite');
            });
        }
    </script>
</body>
</html>

