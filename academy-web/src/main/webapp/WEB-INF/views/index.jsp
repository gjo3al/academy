<%@ page language="java" contentType="text/html; charset=UTF-8"
				pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>選課系統首頁</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
  <body>
  <div style="padding-left: 70px">
    <h1 class="well">Hello ${user.userDetail.nickname}!</h1>
      <br>

      <a href="${pageContext.request.contextPath}/courses/${user.id}/study" class="btn btn-info">選修的課程</a>
      
      <security:authorize access="hasRole('TEACHER')">
      <a href="${pageContext.request.contextPath}/courses/${user.id}/offer" class="btn btn-info">提供的課程</a>
      </security:authorize>
      
      <hr>
      
      <form:form action="${pageContext.request.contextPath}/logout"
				method="POST">
	    <input type="submit" class="btn btn-success" value="登出" />
	  </form:form>
  </div>
  </body>
</html>