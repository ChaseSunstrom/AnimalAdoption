<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="main-header">
    <div class="container">
        <nav class="navbar">
            <div class="nav-brand">
                <a href="${pageContext.request.contextPath}/">
                    🐾 Animal Adoption
                </a>
            </div>
            <ul class="nav-menu">
                <li><a href="${pageContext.request.contextPath}/animals">Browse Pets</a></li>
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <c:if test="${sessionScope.userType eq 'adopter'}">
                            <li><a href="${pageContext.request.contextPath}/dashboard">My Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/saved-animals">❤️ Saved Animals</a></li>
                            <li><a href="${pageContext.request.contextPath}/applications">My Applications</a></li>
                        </c:if>
                        <c:if test="${sessionScope.userType eq 'shelter'}">
                            <li><a href="${pageContext.request.contextPath}/shelter/dashboard">Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/shelter/animals">My Animals</a></li>
                            <li><a href="${pageContext.request.contextPath}/applications">Applications</a></li>
                        </c:if>
                        <li class="user-menu">
                            <span>Welcome, ${sessionScope.userName}</span>
                            <a href="${pageContext.request.contextPath}/logout" class="btn btn-small">Logout</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                        <li><a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Sign Up</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
    </div>
</header>

<c:if test="${not empty sessionScope.successMessage}">
    <div class="alert alert-success">
        ${sessionScope.successMessage}
    </div>
    <c:remove var="successMessage" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.errorMessage}">
    <div class="alert alert-error">
        ${sessionScope.errorMessage}
    </div>
    <c:remove var="errorMessage" scope="session" />
</c:if>
