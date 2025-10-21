<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Animal Adoption</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>

    <main>
        <div class="container">
            <div class="error-page" style="text-align: center; padding: 4rem 0;">
                <h1 style="font-size: 4rem; color: var(--primary-color);">Oops!</h1>
                <h2>Something went wrong</h2>

                <c:choose>
                    <c:when test="${not empty error}">
                        <p class="error-message" style="margin: 2rem 0; font-size: 1.1rem;">
                            ${error}
                        </p>
                    </c:when>
                    <c:when test="${not empty pageContext.errorData.statusCode}">
                        <p class="error-message" style="margin: 2rem 0; font-size: 1.1rem;">
                            Error Code: ${pageContext.errorData.statusCode}
                        </p>
                    </c:when>
                    <c:otherwise>
                        <p class="error-message" style="margin: 2rem 0; font-size: 1.1rem;">
                            We encountered an unexpected error. Please try again later.
                        </p>
                    </c:otherwise>
                </c:choose>

                <div style="margin-top: 2rem;">
                    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                        Go to Home Page
                    </a>
                    <a href="${pageContext.request.contextPath}/animals" class="btn btn-secondary">
                        Browse Animals
                    </a>
                </div>
            </div>
        </div>
    </main>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>
