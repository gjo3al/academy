<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>提供的課程</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
  
</head>
<body>
<div class="container">
  <h3>${instructor.nickname}提供的課程</h3>
  <a href="${pageContext.request.contextPath}/courses/${user.id}/create" class="btn btn-primary">新增課程</a>
  <hr>
  
  <c:if test="${param.createSuccess}">
	<div class="alert alert-success col-xs-1">
	    新增成功
	</div>
  </c:if>
		  
  <c:if test="${param.updateSuccess == true}">
    <div class="alert alert-success col-xs-1">
  	    更新成功
    </div>
  </c:if>
  
  <c:if test="${param.deleteSuccess == true}">
    <div class="alert alert-success col-xs-1">
  	   刪除成功
    </div>
  </c:if>
  
  <c:choose>
  <c:when test="${courses != null}">
	<table class="table table-striped table-hover">
	  <thead class="table-info">
		<tr>
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
			  <c:if test="${isSelf}">
			  <a href="${pageContext.request.contextPath}/courses/${instructor.id}/${course.id}/update" class="btn btn-info">更新</a>
			  <a href="${pageContext.request.contextPath}/courses/${instructor.id}/${course.id}/delete" class="btn btn-warning"
			  	 onclick="if(!(confirm('確定要刪除紀錄?'))) return false">
			  	 刪除</a>
			  </c:if>
			  
			  <c:if test="${!isSelf}">
			  <a href="${pageContext.request.contextPath}/courses/${user.id}" class="btn btn-info">選修</a>
			  </c:if>
			</td>
		  </tr>
	    </c:forEach>
	  </tbody>
	</table>
  </c:when>
  <c:otherwise>
    <h4>${instructor.nickname}尚未提供任何課程</h4>
  </c:otherwise>
  </c:choose>
  <hr>
  <a href="${pageContext.request.contextPath}" class="btn btn-success">首頁</a>
</div>
</body>
</html>