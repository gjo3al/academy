<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>搜尋結果</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
  <h3>${keyword}的搜尋結果：共有 ${fn:length(courses)}筆結果</h3>
  <form:form action="${pageContext.request.contextPath}/courses/search" method="GET">
    <div style="margin-bottom: 25px"
		class="input-group">
      <span class="input-group-addon">
		<i class="glyphicon glyphicon-search"></i>
	  </span>
	  <input type="text" name="keyword"/>
	  <input type="submit" value="Search" class="btn btn-primary"/>
	</div>
  </form:form>
  <hr>
 
  <table class="table table-striped table-hover">
    <thead>
  	  <tr class="info">
		<td>課程名稱</td>
		<td>簡介</td>
		<td>教師</td>
		<td>編輯</td>
	  </tr>
	</thead>
	  
	<tbody>
	  <c:forEach var="course" items="${courses}">
	    <tr>
		  <td>${course.name}</td>
		  <td>${course.description}</td>
		  <td>${course.instructor.userDetail.nickname}</td>
		  <td>
		  
		  <c:choose>
		    <c:when test="${user.id == course.instructor.id}">
			<form:form action="${pageContext.request.contextPath}/courses/${user.id}/${course.id}/study"
			  		   method="POST">
			  <strong class="text-warning">自己的課程</strong>
			</form:form>
		    </c:when>
		  
		    <c:when test="${studyingCoursesId.contains(course.id)}">
			<form:form action="${pageContext.request.contextPath}/courses/${user.id}/${course.id}/study"
			  		   method="POST">
			  <strong class="text-warning">已選修此課程</strong>
			</form:form>
		    </c:when>
		  
		    <c:otherwise>
			<form:form action="${pageContext.request.contextPath}/courses/${user.id}/${course.id}/study"
			  		   method="POST">
			  <button type="submit" class="btn btn-info">選修</button>
			</form:form>
		    </c:otherwise>
		  
		  </c:choose>
		  
		  </td>
		</tr>
	  </c:forEach>
	</tbody>
	
  </table>
  
  <hr>
  <a href="${pageContext.request.contextPath}" class="btn btn-success">首頁</a>
</div>
</body>
</html>