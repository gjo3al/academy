<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新增課程</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <!-- Reference Bootstrap files -->
  <link rel="stylesheet"
	 href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">	
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
  <script	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div id="loginbox" style="margin-top: 50px;"
	class="mainbox col-md-3 col-md-offset-2 col-sm-6 col-sm-offset-2">
  <div class="panel panel-primary">
    <div class="panel panel-info">
      <div class="panel-heading">
	    <div class="panel-title">新增課程</div>
	  </div>
    </div>
  
    <div style="padding-top: 5px" class="panel-body">
      <form:form action="${pageContext.request.contextPath}/courses/${instructor.id}/create" 
		  	     method="POST" 
			     class="form-horizontal" 
			     modelAttribute="course">
	
	    <!-- Place for messages: error, alert etc ... -->
	    <div class="form-group col-xs-15">
		  <!-- Check for login error -->
		  <c:if test="${duplicateCourse}">
		    <div class="alert alert-danger col-xs-offset-1 col-xs-10">
  			    重複的課程
		    </div>
		  </c:if>
	    </div>
	  
	    <form:hidden path="instructor.id" value="${instructor.id}"/>
	    
	    <form:hidden path="id" value="${course.id}"/>
	    
	    <div style="margin-bottom: 25px" class="input-group">
		  <span class="input-group-addon"><i class="glyphicon glyphicon-flag"></i></span> 
		  <form:errors path="name"
					  cssClass="alert alert-danger" />
	      <form:input path="name"
					  placeholder="名稱 (*)"
					  class="form-control" />
	    </div>
	  
	    <div style="margin-bottom: 25px" class="input-group">
		  <span class="input-group-addon"><i class="glyphicon glyphicon-pencil"></i></span> 
		  <form:textarea path="description"
					     placeholder="簡介"
					     rows="8" cols="30"
					     class="form-control" />
	    </div>
	  
	    <div style="margin-top: 10px" class="form-group col-sm-6 controls">
		  <button type="submit"
				  class="btn btn-primary">送出
		  </button>
	    </div>
	    <a style="margin-top: 10px" href="${pageContext.request.contextPath}/courses/${instructor.id}/offer" class="btn btn-success">回到我的課程</a>
	  </form:form>
    </div>
  </div>
</div>
</body>
</html>