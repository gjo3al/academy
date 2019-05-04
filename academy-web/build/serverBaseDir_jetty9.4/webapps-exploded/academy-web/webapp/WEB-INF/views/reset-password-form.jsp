<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>重設密碼</title>
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">	
<!-- Reference Bootstrap files -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
	
<script	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

  <style>
    .error {
	  color: red
  }
</style>
</head>
<body>
  <div id="loginbox" style="margin-top: 50px;"
	class="mainbox col-md-3 col-md-offset-2 col-sm-6 col-sm-offset-2">
	<div class="panel panel-info">
	  <div class="panel-heading">
	    <div class="panel-title">重設密碼</div>
	  </div>
	  
	  <div style="padding-top: 30px" class="panel-body">
	    <form:form action="${pageContext.request.contextPath}/users/reset/process" 
				   modelAttribute="user"
				   method="POST" class="form-horizontal">
		  <div class="form-group">
  	  		<div class="col-xs-15">
  	    	  <c:if test="${error != null}">
		        <div class="alert alert-danger col-xs-offset-1 col-xs-10">
		          	請輸入相同的密碼
		        </div>
		      </c:if>
  	        </div>
  	      </div>
		  
		  <form:hidden path="id"/>
		  <form:hidden path="username"/>
		  <form:hidden path="password"/>
		  <form:hidden path="enabled"/>
		  <form:hidden path="userDetail.id"/>
		  <form:hidden path="userDetail.nickname"/>
		  <form:hidden path="userDetail.email"/>
		  
		  <div style="margin-bottom: 25px" class="input-group">
	  		<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span> 	
	  		  <input type="password" name="newPassword" placeholder="新密碼" class="form-control">				
		  </div>
		  
		  <div style="margin-bottom: 25px" class="input-group">
	  		<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
	  		  <input type="password" name="matchingPassword" placeholder="再次輸入新密碼" class="form-control">	
		  </div>
  	      
  	      <button type="submit" class="btn btn-primary">
	 		 送出
		  </button>
		  
		  <a href="${pageContext.request.contextPath}" class="btn btn-success">首頁</a> 
		</form:form>
	  </div>
	</div>
  </div>
</body>
</html>