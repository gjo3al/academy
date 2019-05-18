<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>選修的課程</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
  
</head>
<body>
<div class="container">
  <h3>${user.userDetail.nickname}選修的課程</h3>
  <hr>
	
  <c:if test="${param.registerSuccess}">
	<div class="alert alert-success col-xs-1">
	    新增成功
	</div>
  </c:if>
  
  <c:if test="${param.deleteSuccess == true}">
    <div class="alert alert-success col-xs-1">
  	   刪除成功
    </div>
  </c:if>
  
  <form:form action="${pageContext.request.contextPath}/courses/search" method="GET">
    <div style="margin-bottom: 25px"
		class="input-group">
      <span class="input-group-addon">
		<i class="glyphicon glyphicon-search"></i>
	  </span>
	  <input type="text" name="keyword" placeholder="課程關鍵字" value="${param.keyword}"/>
	  <input type="submit" value="Search" class="btn btn-primary"/>
	</div>
  </form:form>
  
  <c:choose>
  <c:when test="${courses != null}">
	<table class="table table-striped table-hover">
	  <thead>
		<tr class="info">
			<td>課程名稱</td>
			<td>簡介</td>
			<td>編輯</td>
		</tr>
	  </thead>
	  
	  <tbody>
	    <c:forEach var="course" items="${courses}">
	      <tr>
			<td>${course.name}</td>
			<td>${course.description}</td>
			
			<td>
			  <form:form action="${pageContext.request.contextPath}/courses/${user.id}/${course.id}/study/delete"
			  		   method="POST">
			  	<input type="hidden" name="keyword" value="${keyword}"/>
			  	<button type="submit" 
			  		    class="btn btn-warning"
			  		    onclick="if(!(confirm('確定要退出課程?'))) return false">
			  	退出</button>
			  </form:form>
			</td>
		  </tr>
	    </c:forEach>
	  </tbody>
	</table>
  </c:when>
  <c:otherwise>
    <h4>${user.userDetail.nickname}尚未選修任何課程</h4>
  </c:otherwise>
  </c:choose>
  <hr>
  <a href="${pageContext.request.contextPath}" class="btn btn-success">首頁</a>
</div>
</body>
</html>