<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
<meta charset="UTF-8">
<title>Login page</title>
</head>
<body>

<c:url value="/login" var="loginUrl"/>

<form action="${loginUrl}" method="post">       
	<c:if test="${param.error != null}">        
		<div>
			Invalid username and password.
		</div>
	</c:if>
	<c:if test="${param.logout != null}">       
		<div>
			You have been logged out.
		</div>
	</c:if>
	<div>
		<label for="username">Username</label>
		<input type="text" id="username" name="username"/>	
	</div>
	<div>
		<label for="password">Password</label>
		<input type="password" id="password" name="password"/>	
	</div>
	<div>
        <label for="remember-me">Remember me</label>
        <input type="checkbox" name="remember-me" id="remember-me">
    </div>
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	<button type="submit" class="btn">Log in</button>
</form>

</body>
</html>