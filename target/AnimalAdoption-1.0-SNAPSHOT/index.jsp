<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Animal Adoption - Find Your Perfect Pet</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>

    <main>
        <section class="hero">
            <div class="container">
                <h1>Find Your Perfect Companion</h1>
                <p>Thousands of pets are waiting for their forever homes. Start your adoption journey today!</p>
                <div class="hero-actions">
                    <a href="${pageContext.request.contextPath}/animals" class="btn btn-primary btn-large">
                        Browse Available Pets
                    </a>
                    <c:if test="${empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary btn-large">
                            Get Started
                        </a>
                    </c:if>
                </div>
            </div>
        </section>

        <section class="features">
            <div class="container">
                <h2>How It Works</h2>
                <div class="feature-grid">
                    <div class="feature-card">
                        <div class="feature-icon">🔍</div>
                        <h3>Search & Filter</h3>
                        <p>Find pets by breed, size, age, and compatibility with your lifestyle.</p>
                    </div>
                    <div class="feature-card">
                        <div class="feature-icon">❤️</div>
                        <h3>Get Matched</h3>
                        <p>Our smart matching algorithm recommends pets perfect for your home.</p>
                    </div>
                    <div class="feature-card">
                        <div class="feature-icon">📝</div>
                        <h3>Apply</h3>
                        <p>Submit an adoption application and connect with the shelter.</p>
                    </div>
                    <div class="feature-card">
                        <div class="feature-icon">🏠</div>
                        <h3>Bring Them Home</h3>
                        <p>Complete the adoption process and welcome your new family member!</p>
                    </div>
                </div>
            </div>
        </section>

        <section class="cta">
            <div class="container">
                <h2>Ready to Find Your Perfect Pet?</h2>
                <p>Join thousands of happy adopters who found their companions through our platform.</p>
                <a href="${pageContext.request.contextPath}/animals" class="btn btn-primary btn-large">
                    Start Searching
                </a>
            </div>
        </section>
    </main>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>
