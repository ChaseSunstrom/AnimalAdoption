<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Application Details - Animal Adoption</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../common/header.jsp"/>
    
    <div class="container">
        <div class="page-header">
            <h1>Adoption Application Details</h1>
            <a href="${pageContext.request.contextPath}/applications" class="btn btn-secondary">← Back to Applications</a>
        </div>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        
        <c:if test="${not empty application}">
            <div class="application-detail">
                <!-- Status Banner -->
                <div class="status-banner status-${application.status}">
                    <h2>Status: <span class="status-text">${application.status}</span></h2>
                    <p class="status-date">
                        Submitted on <fmt:formatDate value="${application.submittedAt}" pattern="MMMM dd, yyyy 'at' h:mm a"/>
                    </p>
                </div>
                
                <!-- Animal Information -->
                <div class="detail-section">
                    <h3>Animal Information</h3>
                    <div class="animal-summary">
                        <c:if test="${not empty application.animal.primaryImageUrl}">
                            <img src="${application.animal.primaryImageUrl}" alt="${application.animal.name}" class="animal-detail-image">
                        </c:if>
                        <div class="animal-details">
                            <h2>${application.animal.name}</h2>
                            <p><strong>Species:</strong> ${application.animal.species}</p>
                            <p><strong>Breedrong> ${application.animal.breed}</p>
                            <p><strong>Age:</strong> ${application.animal.formattedAge}</p>
                            <p><strong>Size:</strong> ${application.animal.size}</p>:</st
                            <p><strong>Gender:</strong> ${application.animal.gender}</p>
                            <a href="${pageContext.request.contextPath}/animal-detail?id=${application.animal.animalId}" class="btn btn-secondary">View Animal Profile</a>
                        </div>
                    </div>
                </div>
                
                <!-- Shelter Information -->
                <c:if test="${userType == 'adopter'}">
                    <div class="detail-section">
                        <h3>Shelter Information</h3>
                        <p><strong>Name:</strong> ${application.shelter.shelterName}</p>
                        <c:if test="${not empty application.shelter.city}">
                            <p><strong>Location:</strong> ${application.shelter.city}, ${application.shelter.state}</p>
                        </c:if>
                        <c:if test="${not empty application.shelter.phone}">
                            <p><strong>Phone:</strong> ${application.shelter.phone}</p>
                        </c:if>
                        <c:if test="${not empty application.shelter.email}">
                            <p><strong>Email:</strong> ${application.shelter.email}</p>
                        </c:if>
                    </div>
                </c:if>
                
                <!-- Applicant Information (for shelter view) -->
                <c:if test="${userType == 'shelter'}">
                    <div class="detail-section">
                        <h3>Applicant Information</h3>
                        <p><strong>Name:</strong> ${application.adopter.firstName} ${application.adopter.lastName}</p>
                        <p><strong>Email:</strong> ${application.adopter.email}</p>
                        <c:if test="${not empty application.adopter.phone}">
                            <p><strong>Phone:</strong> ${application.adopter.phone}</p>
                        </c:if>
                    </div>
                </c:if>
                
                <!-- Application Details -->
                <div class="detail-section">
                    <h3>Application Details</h3>
                    
                    <div class="application-field">
                        <label>Why do you want to adopt this pet?</label>
                        <p>${application.whyAdopt}</p>
                    </div>
                    
                    <c:if test="${not empty application.previousPetExperience}">
                        <div class="application-field">
                            <label>Previous Pet Experience:</label>
                            <p>${application.previousPetExperience}</p>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty application.veterinarianName}">
                        <div class="application-field">
                            <label>Veterinarian Reference:</label>
                            <p><strong>Name:</strong> ${application.veterinarianName}<br>
                            <c:if test="${not empty application.veterinarianPhone}">
                                <strong>Phone:</strong> ${application.veterinarianPhone}
                            </c:if>
                            </p>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty application.reference1Name}">
                        <div class="application-field">
                            <label>Reference 1:</label>
                            <p><strong>Name:</strong> ${application.reference1Name}<br>
                            <c:if test="${not empty application.reference1Phone}">
                                <strong>Phone:</strong> ${application.reference1Phone}<br>
                            </c:if>
                            <c:if test="${not empty application.reference1Relationship}">
                                <strong>Relationship:</strong> ${application.reference1Relationship}
                            </c:if>
                            </p>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty application.reference2Name}">
                        <div class="application-field">
                            <label>Reference 2:</label>
                            <p><strong>Name:</strong> ${application.reference2Name}<br>
                            <c:if test="${not empty application.reference2Phone}">
                                <strong>Phone:</strong> ${application.reference2Phone}<br>
                            </c:if>
                            <c:if test="${not empty application.reference2Relationship}">
                                <strong>Relationship:</strong> ${application.reference2Relationship}
                            </c:if>
                            </p>
                        </div>
                    </c:if>
                </div>
                
                <!-- Shelter Notes (shelter only) -->
                <c:if test="${userType == 'shelter' && not empty application.shelterNotes}">
                    <div class="detail-section">
                        <h3>Internal Notes</h3>
                        <p>${application.shelterNotes}</p>
                    </div>
                </c:if>
                
                <!-- Actions -->
                <div class="detail-actions">
                    <c:if test="${application.status == 'submitted' && userType == 'adopter'}">
                        <form method="post" action="${pageContext.request.contextPath}/applications/withdraw" style="display: inline;">
                            <input type="hidden" name="applicationId" value="${application.applicationId}">
                            <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to withdraw this application?')">Withdraw Application</button>
                        </form>
                    </c:if>
                    
                    <c:if test="${userType == 'shelter' && (application.status == 'submitted' || application.status == 'under_review')}">
                        <form method="post" action="${pageContext.request.contextPath}/applications/update-status" style="display: inline;">
                            <input type="hidden" name="applicationId" value="${application.applicationId}">
                            <select name="newStatus" class="form-control" style="width: auto; display: inline-block;">
                                <option value="submitted" ${application.status == 'submitted' ? 'selected' : ''}>Submitted</option>
                                <option value="under_review" ${application.status == 'under_review' ? 'selected' : ''}>Under Review</option>
                                <option value="approved" ${application.status == 'approved' ? 'selected' : ''}>Approved</option>
                                <option value="rejected" ${application.status == 'rejected' ? 'selected' : ''}>Rejected</option>
                            </select>
                            <textarea name="shelterNotes" class="form-control" placeholder="Add notes (optional)" rows="2" style="margin-top: 10px;">${application.shelterNotes}</textarea>
                            <button type="submit" class="btn btn-primary" style="margin-top: 10px;">Update Status</button>
                        </form>
                    </c:if>
                </div>
            </div>
        </c:if>
    </div>
    
    <jsp:include page="../common/footer.jsp"/>
    
    <style>
        .application-detail {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-top: 2rem;
        }
        
        .status-banner {
            padding: 2rem;
            border-radius: 8px 8px 0 0;
            text-align: center;
            color: white;
        }
        
        .status-banner h2 {
            margin: 0 0 0.5rem 0;
            text-transform: capitalize;
        }
        
        .status-banner .status-text {
            text-transform: capitalize;
            font-weight: bold;
        }
        
        .status-banner.status-submitted {
            background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
        }
        
        .status-banner.status-under_review {
            background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
        }
        
        .status-banner.status-approved {
            background: linear-gradient(135deg, #10b981 0%, #059669 100%);
        }
        
        .status-banner.status-rejected {
            background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
        }
        
        .status-banner.status-withdrawn {
            background: linear-gradient(135deg, #6b7280 0%, #4b5563 100%);
        }
        
        .status-date {
            margin: 0;
            opacity: 0.9;
        }
        
        .detail-section {
            padding: 2rem;
            border-bottom: 1px solid #e5e7eb;
        }
        
        .detail-section:last-child {
            border-bottom: none;
        }
        
        .detail-section h3 {
            margin-top: 0;
            color: #1f2937;
            border-bottom: 2px solid #e5e7eb;
            padding-bottom: 0.5rem;
        }
        
        .animal-summary {
            display: flex;
            gap: 2rem;
            margin-top: 1rem;
        }
        
        .animal-detail-image {
            width: 200px;
            height: 200px;
            object-fit: cover;
            border-radius: 8px;
        }
        
        .animal-details {
            flex: 1;
        }
        
        .animal-details h2 {
            margin-top: 0;
            color: #1f2937;
        }
        
        .animal-details p {
            margin: 0.5rem 0;
        }
        
        .application-field {
            margin-bottom: 1.5rem;
        }
        
        .application-field label {
            display: block;
            font-weight: 600;
            color: #374151;
            margin-bottom: 0.5rem;
        }
        
        .application-field p {
            margin: 0;
            color: #4b5563;
            line-height: 1.6;
            white-space: pre-wrap;
        }
        
        .detail-actions {
            padding: 2rem;
            background: #f9fafb;
            border-radius: 0 0 8px 8px;
            display: flex;
            gap: 1rem;
            flex-wrap: wrap;
        }
        
        .form-control {
            padding: 0.5rem;
            border: 1px solid #d1d5db;
            border-radius: 4px;
            font-size: 1rem;
        }
        
        @media (max-width: 768px) {
            .animal-summary {
                flex-direction: column;
            }
            
            .animal-detail-image {
                width: 100%;
                max-width: 300px;
                margin: 0 auto;
            }
        }
    </style>
</body>
</html>



