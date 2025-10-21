<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Animal Adoption</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <div class="auth-form">
            <h1>Login to Animal Adoption</h1>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email" required
                           placeholder="Enter your email" value="${param.email}">
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required
                           placeholder="Enter your password">
                </div>

                <button type="submit" class="btn btn-primary btn-block">Login</button>
            </form>

            <div class="auth-links">
                <p>Don't have an account? <a href="${pageContext.request.contextPath}/register">Register here</a></p>
                <p><a href="${pageContext.request.contextPath}/animals">Browse animals</a> without logging in</p>
            </div>
        </div>
    </div>
</body>
</html>
