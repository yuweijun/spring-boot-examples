<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

<html>
<head>
<title>SELECT 操作</title>
</head>
<body>
<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver"
     url="jdbc:mysql://localhost:3306/jdbc?useSSL=false&useUnicode=true&characterEncoding=utf-8"
     user="dbuser"  password="dbpass"/>

<sql:update dataSource="${snapshot}" var="count">
  DELETE FROM user WHERE Id = ?
  <sql:param value="${11}" />
</sql:update>

<sql:query dataSource="${snapshot}" var="result">
SELECT * from user limit 100;
</sql:query>
<h1>JSP 数据库实例</h1>
<table border="1" width="100%">
<tr>
   <th>ID</th>
   <th>Name</th>
</tr>
<c:forEach var="row" items="${result.rows}">
<tr>
   <td><c:out value="${row.id}"/></td>
   <td><c:out value="${row.name}"/></td>
</tr>
</c:forEach>
</table>

</body>
</html>