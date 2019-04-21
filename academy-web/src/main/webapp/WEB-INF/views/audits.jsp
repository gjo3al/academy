<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Audits</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
	<h3>登入錯誤資訊</h3>
	<br>
	<table class="table table-striped">
		<tr>
			<td>時間</td>
			<td>IP</td>
		</tr>
		
		<c:forEach var="audit" items="${audits}">
			<tr>
				<td>
					<fmt:formatDate value="${audit.timestamp}" pattern="yyyy/MM/dd - HH:mm:ss" />
			    </td>
				<td>${audit.address}</td>
			</tr>
		</c:forEach>
		
		<form:form method="POST" 
				action="${pageContext.request.contextPath}/audits/delete/${user.username}">
		<tr>
			<td>
			<button type="submit" 
					onclick="if(!(confirm('確定要刪除紀錄?'))) return false">
				刪除紀錄
			</button>
			</td>
		</tr>
		</form:form>
	</table>
</div>
</body>
</html>