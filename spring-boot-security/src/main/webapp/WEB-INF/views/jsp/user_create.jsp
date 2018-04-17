<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create a new user</title>
</head>
<body>
<nav role="navigation">
    <ul>
        <li><a href="/"><sec:authentication property="principal.username" /></a></li>
    </ul>
</nav>

<h1>Create a new user</h1>

<form:form modelAttribute="form" method="post">
	<div>
	在spring form taglib中的属性键值对必须匹配，以key="value"形式出现，不能按html方式只写required，会报错"equal symbol expected"
	</div>
    <div>
        <label for="username">Username</label>
        <form:input path="username" size="20" />
    </div>
    <div>
        <label for="email">Email address</label>
        <form:input path="email" size="50" />
    </div>
    <div>
        <label for="password">Password</label>
        <form:password path="password" size="20" />
    </div>
    <div>
        <label for="passwordRepeated">Repeat</label>
        <form:password path="passwordRepeated" size="20" />
    </div>
    <div>
        <label for="role">Role</label>
        <select name="role" id="role" required>
            <option <c:if test="${form.role == 'USER'}">selected</c:if>>USER</option>
            <option <c:if test="${form.role == 'ADMIN'}">selected</c:if>>ADMIN</option>
        </select>
    </div>
    <button type="submit">Save</button>
    <div>
		<form:errors path="*" />
    </div>
</form:form>

</body>
</html>