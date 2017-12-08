<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hello world</title>
</head>
<body>

<h1><spring:message code="hello.world" /></h1>

<h2>&mdash;&mdash;&mdash;&mdash;</h2>

<nav role="navigation">
    <ul>
    <c:if test="${not empty currentUser}">
        <li>
			<c:url value="/logout" var="logoutUrl" />
			
			<form id="logout" action="${logoutUrl}" method="post" >
			    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <button type="submit">Log out</button>
			</form>
        </li>
    </c:if>
    <c:if test="${currentUser != null && currentUser.role == 'ADMIN'}">
        <li><a href="/user/create">Create a new user</a></li>
        <li><a href="/users">View all users</a></li>
    </c:if>
    </ul>
    <p>${currentUser}</p>
</nav>

</body>
</html>