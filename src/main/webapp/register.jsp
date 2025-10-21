<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Animal Adoption</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <div class="auth-form">
            <h1>Create Your Account</h1>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    ${error}
                </div>
            </c:if>

            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    ${success}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm">
                <div class="form-group">
                    <label>I want to:</label>
                    <div class="radio-group">
                        <label class="radio-label">
                            <input type="radio" name="userType" value="adopter" checked
                                   onchange="toggleShelterFields()">
                            Adopt a pet
                        </label>
                        <label class="radio-label">
                            <input type="radio" name="userType" value="shelter"
                                   onchange="toggleShelterFields()">
                            Register my shelter
                        </label>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="firstName">First Name *</label>
                        <input type="text" id="firstName" name="firstName" required
                               value="${formData.firstName[0]}">
                    </div>

                    <div class="form-group">
                        <label for="lastName">Last Name *</label>
                        <input type="text" id="lastName" name="lastName" required
                               value="${formData.lastName[0]}">
                    </div>
                </div>

                <div class="form-group">
                    <label for="email">Email Address *</label>
                    <input type="email" id="email" name="email" required
                           value="${formData.email[0]}">
                </div>

                <div class="form-group">
                    <label for="phone">Phone Number</label>
                    <input type="tel" id="phone" name="phone"
                           placeholder="555-1234 or (555) 555-1234"
                           value="${formData.phone[0]}">
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="password">Password *</label>
                        <input type="password" id="password" name="password" required>
                        <small>At least 8 characters with letters and numbers</small>
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword">Confirm Password *</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required>
                    </div>
                </div>

                <div id="shelterFields" style="display: none;">
                    <hr>
                    <h3>Shelter Information</h3>

                    <div class="form-group">
                        <label for="shelterName">Shelter Name *</label>
                        <input type="text" id="shelterName" name="shelterName"
                               value="${formData.shelterName[0]}">
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="city">City</label>
                            <input type="text" id="city" name="city"
                                   value="${formData.city[0]}">
                        </div>

                        <div class="form-group">
                            <label for="state">State</label>
                            <input type="text" id="state" name="state"
                                   placeholder="CA"
                                   value="${formData.state[0]}">
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary btn-block">Create Account</button>
            </form>

            <div class="auth-links">
                <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Login here</a></p>
            </div>
        </div>
    </div>

    <script>
        function toggleShelterFields() {
            const shelterRadio = document.querySelector('input[name="userType"][value="shelter"]');
            const shelterFields = document.getElementById('shelterFields');
            const shelterName = document.getElementById('shelterName');

            if (shelterRadio.checked) {
                shelterFields.style.display = 'block';
                shelterName.required = true;
            } else {
                shelterFields.style.display = 'none';
                shelterName.required = false;
            }
        }

        // Initialize on page load
        document.addEventListener('DOMContentLoaded', toggleShelterFields);
    </script>
</body>
</html>
