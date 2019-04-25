<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ForGet Password</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
  <div id="loginbox" style="margin-top: 5px;margin-left: 5px;"
			class="mainbox col-md-3 col-md-offset-2 col-sm-6 col-sm-offset-2">
  <div class="panel panel-info">
    <div class="panel-heading">
      <div class="panel-title">忘記密碼</div>
  </div>
  <div style="padding-top: 30px" class="panel-body">
  <form:form action="${pageContext.request.contextPath}/users/forget/process" 
  			method="POST" class="form-horizontal">
  	<div class="form-group">
  	  <div class="col-xs-15">
  	    <c:if test="${error != null}">
		  <div class="alert alert-danger col-xs-offset-1 col-xs-10">
		    Invalid username or email.
		  </div>
		</c:if>
  	  </div>
  	</div>
  	
    <div style="margin-bottom: 25px" class="input-group">
	  <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span> 					
	  <input type="text" name="username" placeholder="username" class="form-control">
	</div>
	
	<div style="margin-bottom: 25px" class="input-group">
	  <span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span> 					
	  <input type="text" name="email" placeholder="email" class="form-control">
	</div>
	
	<button type="submit" class="btn btn-primary">
	  送出
	</button>
	
	<a href="${pageContext.request.contextPath}" class="btn btn-success">首頁</a>  
  </form:form>
  </div> 
  </div>
  </div>
</div>
</body>
</html>