<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Profile - Animal Adoption</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>

    <main class="profile-edit-page">
        <div class="container">
            <h1>My Profile</h1>
            <p>Tell us about yourself so we can find your perfect pet match!</p>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/profile/edit" method="post" class="profile-form">
                <section class="form-section">
                    <h2>Address Information</h2>
                    
                    <div class="form-group">
                        <label for="addressLine1">Address Line 1</label>
                        <input type="text" id="addressLine1" name="addressLine1"
                               value="${profile.addressLine1}" placeholder="123 Main Street">
                    </div>

                    <div class="form-group">
                        <label for="addressLine2">Address Line 2</label>
                        <input type="text" id="addressLine2" name="addressLine2"
                               value="${profile.addressLine2}" placeholder="Apt 4B (optional)">
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="city">City</label>
                            <input type="text" id="city" name="city"
                                   value="${profile.city}" placeholder="Springfield">
                        </div>

                        <div class="form-group">
                            <label for="state">State</label>
                            <input type="text" id="state" name="state"
                                   value="${profile.state}" placeholder="IL" maxlength="2">
                        </div>

                        <div class="form-group">
                            <label for="zipCode">Zip Code</label>
                            <input type="text" id="zipCode" name="zipCode"
                                   value="${profile.zipCode}" placeholder="62701">
                        </div>
                    </div>
                </section>

                <section class="form-section">
                    <h2>Home Environment</h2>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="homeType">Home Type *</label>
                            <select id="homeType" name="homeType" required>
                                <option value="">Select...</option>
                                <option value="apartment" ${profile.homeType eq 'apartment' ? 'selected' : ''}>Apartment</option>
                                <option value="house" ${profile.homeType eq 'house' ? 'selected' : ''}>House</option>
                                <option value="condo" ${profile.homeType eq 'condo' ? 'selected' : ''}>Condo</option>
                                <option value="townhouse" ${profile.homeType eq 'townhouse' ? 'selected' : ''}>Townhouse</option>
                                <option value="other" ${profile.homeType eq 'other' ? 'selected' : ''}>Other</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="homeSize">Home Size *</label>
                            <select id="homeSize" name="homeSize" required>
                                <option value="">Select...</option>
                                <option value="small" ${profile.homeSize eq 'small' ? 'selected' : ''}>Small (Under 1000 sq ft)</option>
                                <option value="medium" ${profile.homeSize eq 'medium' ? 'selected' : ''}>Medium (1000-2000 sq ft)</option>
                                <option value="large" ${profile.homeSize eq 'large' ? 'selected' : ''}>Large (Over 2000 sq ft)</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="checkbox-label">
                            <input type="checkbox" name="hasYard" value="true"
                                   ${profile.hasYard ? 'checked' : ''}>
                            I have a yard
                        </label>
                    </div>
                </section>

                <section class="form-section">
                    <h2>Household Composition</h2>

                    <div class="form-group">
                        <label class="checkbox-label">
                            <input type="checkbox" name="hasChildren" value="true"
                                   id="hasChildrenCheckbox" onchange="toggleChildrenAges()"
                                   ${profile.hasChildren ? 'checked' : ''}>
                            I have children in the household
                        </label>
                    </div>

                    <div class="form-group" id="childrenAgesGroup" style="display: ${profile.hasChildren ? 'block' : 'none'};">
                        <label for="childrenAges">Children's Ages (comma-separated)</label>
                        <input type="text" id="childrenAges" name="childrenAges"
                               value="${profile.childrenAges}" placeholder="5, 8, 12">
                        <small>Example: 3, 7, 10</small>
                    </div>

                    <div class="form-group">
                        <label class="checkbox-label">
                            <input type="checkbox" name="hasOtherPets" value="true"
                                   id="hasOtherPetsCheckbox" onchange="toggleOtherPetsDesc()"
                                   ${profile.hasOtherPets ? 'checked' : ''}>
                            I have other pets
                        </label>
                    </div>

                    <div class="form-group" id="otherPetsGroup" style="display: ${profile.hasOtherPets ? 'block' : 'none'};">
                        <label for="otherPetsDescription">Describe Your Current Pets</label>
                        <textarea id="otherPetsDescription" name="otherPetsDescription"
                                  rows="3" placeholder="e.g., One 5-year-old golden retriever, two adult cats">${profile.otherPetsDescription}</textarea>
                    </div>
                </section>

                <section class="form-section">
                    <h2>Your Experience & Lifestyle</h2>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="experienceLevel">Pet Experience Level *</label>
                            <select id="experienceLevel" name="experienceLevel" required>
                                <option value="">Select...</option>
                                <option value="first_time" ${profile.experienceLevel eq 'first_time' ? 'selected' : ''}>First-time Pet Owner</option>
                                <option value="some_experience" ${profile.experienceLevel eq 'some_experience' ? 'selected' : ''}>Some Experience</option>
                                <option value="very_experienced" ${profile.experienceLevel eq 'very_experienced' ? 'selected' : ''}>Very Experienced</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="activityLevel">Activity Level *</label>
                            <select id="activityLevel" name="activityLevel" required>
                                <option value="">Select...</option>
                                <option value="low" ${profile.activityLevel eq 'low' ? 'selected' : ''}>Low (Prefer quiet, calm pets)</option>
                                <option value="moderate" ${profile.activityLevel eq 'moderate' ? 'selected' : ''}>Moderate (Some daily activity)</option>
                                <option value="high" ${profile.activityLevel eq 'high' ? 'selected' : ''}>High (Very active, lots of exercise)</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="preferencesNotes">Additional Preferences or Notes</label>
                        <textarea id="preferencesNotes" name="preferencesNotes"
                                  rows="4" placeholder="Tell us about your lifestyle, what you're looking for in a pet, any special requirements, etc.">${profile.preferencesNotes}</textarea>
                    </div>
                </section>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary btn-large">Save Profile</button>
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </main>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>

    <script>
        function toggleChildrenAges() {
            const checkbox = document.getElementById('hasChildrenCheckbox');
            const group = document.getElementById('childrenAgesGroup');
            group.style.display = checkbox.checked ? 'block' : 'none';
        }

        function toggleOtherPetsDesc() {
            const checkbox = document.getElementById('hasOtherPetsCheckbox');
            const group = document.getElementById('otherPetsGroup');
            group.style.display = checkbox.checked ? 'block' : 'none';
        }
    </script>
</body>
</html>




