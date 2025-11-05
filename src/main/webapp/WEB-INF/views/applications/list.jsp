<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Applications - Animal Adoption</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../common/header.jsp"/>
    
    <div class="container">
        <div class="page-header">
            <h1>My Adoption Applications</h1>
            <c:if test="${userType == 'adopter'}">
                <a href="${pageContext.request.contextPath}/animals" class="btn btn-primary">Browse Animals</a>
            </c:if>
        </div>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        
        <c:choose>
            <c:when test="${empty applications}">
                <div class="empty-state">
                    <h3>No applications yet</h3>
                    <c:if test="${userType == 'adopter'}">
                        <p>You haven't submitted any adoption applications yet.</p>
                        <a href="${pageContext.request.contextPath}/animals" class="btn btn-primary">Find Your Perfect Pet</a>
                    </c:if>
                    <c:if test="${userType == 'shelter'}">
                        <p>No applications have been submitted for your animals yet.</p>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <div class="applications-grid">
                    <c:forEach items="${applications}" var="app">
                        <div class="application-card">
                            <div class="application-header">
                                <h3>${app.animal.name}</h3>
                                <span class="status-badge status-${app.status}">${app.status}</span>
                            </div>
                            
                            <div class="application-body">
                                <c:if test="${not empty app.animal.primaryImageUrl}">
                                    <img src="${app.animal.primaryImageUrl}" alt="${app.animal.name}" class="animal-thumbnail">
                                </c:if>
                                
                                <div class="application-info">
                                    <p><strong>Species:</strong> ${app.animal.species}</p>
                                    <p><strong>Breed:</strong> ${app.animal.breed}</p>
                                    <p><strong>Submitted:</strong> <fmt:formatDate value="${app.submittedAt}" pattern="MMM dd, yyyy"/></p>
                                    
                                    <c:if test="${userType == 'adopter'}">
                                        <p><strong>Shelter:</strong> ${app.shelter.shelterName}</p>
                                    </c:if>
                                    
                                    <c:if test="${userType == 'shelter'}">
                                        <p><strong>Applicant:</strong> ${app.adopter.firstName} ${app.adopter.lastName}</p>
                                    </c:if>
                                </div>
                            </div>
                            
                            <div class="application-actions">
                                <a href="${pageContext.request.contextPath}/applications/view/${app.applicationId}" class="btn btn-secondary">View Details</a>
                                
                                <c:if test="${app.status == 'submitted' && userType == 'adopter'}">
                                    <form method="post" action="${pageContext.request.contextPath}/applications/withdraw" style="display: inline;">
                                        <input type="hidden" name="applicationId" value="${app.applicationId}">
                                        <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to withdraw this application?')">Withdraw</button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <jsp:include page="../common/footer.jsp"/>
    
    <style>
        .applications-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
            gap: 2rem;
            margin-top: 2rem;
        }
        
        .application-card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        
        .application-header {
            padding: 1.5rem;
            background: #f8f9fa;
            border-bottom: 1px solid #dee2e6;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .application-header h3 {
            margin: 0;
            font-size: 1.5rem;
        }
        
        .status-badge {
            padding: 0.25rem 0.75rem;
            border-radius: 20px;
            font-size: 0.875rem;
            font-weight: 600;
            text-transform: capitalize;
        }
        
        .status-submitted {
            background: #fef3c7;
            color: #92400e;
        }
        
        .status-under_review {
            background: #dbeafe;
            color: #1e40af;
        }
        
        .status-approved {
            background: #d1fae5;
            color: #065f46;
        }
        
        .status-rejected {
            background: #fee2e2;
            color: #991b1b;
        }
        
        .status-withdrawn {
            background: #e5e7eb;
            color: #374151;
        }
        
        .application-body {
            padding: 1.5rem;
            display: flex;
            gap: 1rem;
        }
        
        .animal-thumbnail {
            width: 100px;
            height: 100px;
            object-fit: cover;
            border-radius: 8px;
        }
        
        .application-info {
            flex: 1;
        }
        
        .application-info p {
            margin: 0.5rem 0;
        }
        
        .application-actions {
            padding: 1rem 1.5rem;
            background: #f8f9fa;
            border-top: 1px solid #dee2e6;
            display: flex;
            gap: 1rem;
        }
        
        .empty-state {
            text-align: center;
            padding: 4rem 2rem;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .empty-state h3 {
            color: #6b7280;
            margin-bottom: 1rem;
        }
        
        .empty-state p {
            color: #9ca3af;
            margin-bottom: 2rem;
        }
    </style>
</body>
</html>



