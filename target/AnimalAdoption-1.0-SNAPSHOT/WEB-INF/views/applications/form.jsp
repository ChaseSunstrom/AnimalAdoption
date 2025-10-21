<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Adoption Application - ${animal.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>

    <main class="application-form-page">
        <div class="container">
            <div class="form-header">
                <h1>Adoption Application for ${animal.name}</h1>
                <p>Please fill out this form to apply for adoption. All fields marked with * are required.</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    ${error}
                </div>
            </c:if>

            <div class="form-layout">
                <aside class="animal-summary">
                    <h3>Pet Information</h3>
                    <div class="summary-image">
                        <c:choose>
                            <c:when test="${not empty animal.primaryImageUrl}">
                                <img src="${animal.primaryImageUrl}" alt="${animal.name}">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/images/placeholder-pet.jpg" alt="${animal.name}">
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="summary-info">
                        <h4>${animal.name}</h4>
                        <p>${animal.species} • ${animal.breed}</p>
                        <p>${animal.formattedAge} • ${animal.gender}</p>
                        <c:if test="${not empty animal.shelter}">
                            <p class="shelter-name">${animal.shelter.shelterName}</p>
                        </c:if>
                    </div>
                </aside>

                <form action="${pageContext.request.contextPath}/applications/submit" method="post" class="adoption-form">
                    <input type="hidden" name="animalId" value="${animal.animalId}">

                    <div class="form-section">
                        <h3>Why do you want to adopt this pet? *</h3>
                        <textarea name="whyAdopt" rows="5" required
                                  placeholder="Tell us why you'd like to adopt ${animal.name} and what makes you a good match...">${formData.whyAdopt}</textarea>
                        <small>Minimum 50 characters</small>
                    </div>

                    <div class="form-section">
                        <h3>Pet Experience</h3>
                        <div class="form-group">
                            <label for="previousPetExperience">
                                Have you had pets before? Tell us about your experience.
                            </label>
                            <textarea name="previousPetExperience" id="previousPetExperience" rows="4"
                                      placeholder="Describe your experience with pets...">${formData.previousPetExperience}</textarea>
                        </div>
                    </div>

                    <div class="form-section">
                        <h3>Veterinarian Information</h3>
                        <p>Please provide your current or previous veterinarian (if applicable)</p>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="veterinarianName">Veterinarian Name</label>
                                <input type="text" id="veterinarianName" name="veterinarianName"
                                       value="${formData.veterinarianName}">
                            </div>
                            <div class="form-group">
                                <label for="veterinarianPhone">Veterinarian Phone</label>
                                <input type="tel" id="veterinarianPhone" name="veterinarianPhone"
                                       value="${formData.veterinarianPhone}">
                            </div>
                        </div>
                    </div>

                    <div class="form-section">
                        <h3>Personal References</h3>
                        <p>Please provide two personal references (optional but recommended)</p>

                        <h4>Reference 1</h4>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="reference1Name">Name</label>
                                <input type="text" id="reference1Name" name="reference1Name"
                                       value="${formData.reference1Name}">
                            </div>
                            <div class="form-group">
                                <label for="reference1Phone">Phone</label>
                                <input type="tel" id="reference1Phone" name="reference1Phone"
                                       value="${formData.reference1Phone}">
                            </div>
                            <div class="form-group">
                                <label for="reference1Relationship">Relationship</label>
                                <input type="text" id="reference1Relationship" name="reference1Relationship"
                                       placeholder="Friend, Colleague, etc."
                                       value="${formData.reference1Relationship}">
                            </div>
                        </div>

                        <h4>Reference 2</h4>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="reference2Name">Name</label>
                                <input type="text" id="reference2Name" name="reference2Name"
                                       value="${formData.reference2Name}">
                            </div>
                            <div class="form-group">
                                <label for="reference2Phone">Phone</label>
                                <input type="tel" id="reference2Phone" name="reference2Phone"
                                       value="${formData.reference2Phone}">
                            </div>
                            <div class="form-group">
                                <label for="reference2Relationship">Relationship</label>
                                <input type="text" id="reference2Relationship" name="reference2Relationship"
                                       placeholder="Friend, Colleague, etc."
                                       value="${formData.reference2Relationship}">
                            </div>
                        </div>
                    </div>

                    <div class="form-section">
                        <div class="form-notice">
                            <h4>Please Note:</h4>
                            <ul>
                                <li>By submitting this application, you agree that all information provided is accurate.</li>
                                <li>The shelter will review your application and contact you within 3-5 business days.</li>
                                <li>Approval is subject to shelter verification and a potential home visit.</li>
                            </ul>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary btn-large">Submit Application</button>
                        <a href="${pageContext.request.contextPath}/animal-detail?id=${animal.animalId}"
                           class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </main>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>
