<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  

<!doctype html>
<html>

<head>
  <title>登入</title>
  <meta charset="utf-8">
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
	<div class="panel panel-info">
	  <div class="panel-heading">
	  	<div class="panel-title">登入</div>
	  </div>

	  <div style="padding-top: 30px" class="panel-body">
		<!-- Login Form -->
		<form:form action="${pageContext.request.contextPath}/authenticateTheUser" 
				   method="POST" class="form-horizontal">
		  <!-- Place for messages: error, alert etc ... -->
		  <div class="form-group col-xs-15">
			<!-- Check for login error -->
		    <c:if test="${loginError != null}">
		   	  <div class="alert alert-danger col-xs-offset-1 col-xs-10">
  				${loginError}
			  </div>						
			</c:if>
						
			<c:if test="${param.logout != null}">								            
			  <div class="alert alert-success col-xs-offset-1 col-xs-10">
				已登出
		  	  </div>
			</c:if>
		  </div>
		  
		  <!-- User name -->
		  <div style="margin-bottom: 25px" class="input-group">
			<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span> 
			<input type="text" name="username" placeholder="帳號" class="form-control">
		  </div>

		  <!-- Password -->
		  <div style="margin-bottom: 25px" class="input-group">
			<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span> 
			<input type="password" name="password" placeholder="密碼" class="form-control" >
		  </div>

		  <!-- Login/Submit Button -->
		  <div style="margin-top: 10px" class="form-group">						
			<div class="col-sm-6 controls">
			  <button type="submit" class="btn btn-success">登入</button>
			</div>
		  </div>
						
		  <div style="margin-top: 10px" class="form-group">						
			<div class="col-sm-6 controls">
			  <a href="${pageContext.request.contextPath}/register" 
				class="btn btn-primary">
				註冊
			  </a>
			</div>
		  </div>
						
		  <div style="margin-top: 10px" class="form-group">						
			<div class="col-sm-6 controls">
			  <a href="${pageContext.request.contextPath}/users/forget" 
				class="btn btn-warning">
				忘記密碼
			  </a>
			</div>
		  </div>
		</form:form>
	  </div>
	</div>
  </div>
</body>
</html>