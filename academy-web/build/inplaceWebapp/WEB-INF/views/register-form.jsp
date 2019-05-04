<%@ page language="java" contentType="text/html; charset=UTF-8"
				pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>註冊</title>
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
	  <div class="panel-heading">
	    <div class="panel-title">註冊</div>
	  </div>
	  <div style="padding-top: 30px" class="panel-body">
		<form:form method="POST"
				  action="${pageContext.request.contextPath}/register"
				  modelAttribute="registrationUser"
				  class="form-horizontal">
		  <div class="form-group">
	  	    <div class="col-xs-15">
			  <!-- Check for registration error -->
			  <c:if test="${registrationError != null}">
  				<div class="alert alert-danger col-xs-offset-1 col-xs-10">
				  ${registrationError}
				</div>
			  </c:if>
	  		</div>
		  </div>
			
		  <div style="margin-bottom: 25px" class="input-group">
	  		<span class="input-group-addon">
			  <i class="glyphicon glyphicon-user"></i>
	  		</span>
			<form:errors path="user.username"
						cssClass="alert alert-danger" />
	    	<form:input path="user.username"
						placeholder="帳號 (*)"
						class="form-control" />
		  </div>
			
		  <div style="margin-bottom: 25px"
			  class="input-group">
   			<span class="input-group-addon">
			  <i class="glyphicon glyphicon-lock"></i>
			</span>
			<form:errors path="user.password"
						cssClass="alert alert-danger" />
  			<form:password path="user.password"
						  placeholder="密碼 (*)"
						  class="form-control" />
		  </div>
			
		  <div style="margin-bottom: 25px"
		  	  class="input-group">
	  		<span class="input-group-addon">
			  <i class="glyphicon glyphicon-lock"></i>
			</span>
			<form:errors path="matchingPassword"
						cssClass="alert alert-danger" />
			<form:password path="matchingPassword"
						  placeholder="確認密碼 (*)"
						  class="form-control" />
		  </div>
			
		  <div style="margin-bottom: 25px"
			  class="input-group">
			<span class="input-group-addon">
			  <i class="glyphicon glyphicon-user"></i>
			</span>
			<form:input path="user.userDetail.nickname"
						placeholder="暱稱"
						class="form-control" />
		  </div>
			
		  <div style="margin-bottom: 25px"
			  class="input-group">
			<span class="input-group-addon">
			  <i class="glyphicon glyphicon-envelope"></i>
			</span>
			<form:errors path="user.userDetail.email"
						cssClass="alert alert-danger" />
			<form:input path="user.userDetail.email"
						placeholder="電子信箱 (*)"
						class="form-control" />
		  </div>
			
		  <div style="margin-bottom: 25px"
			  class="input-group">
			<form:checkboxes path="authorities"
							items="${authorities}"
							class="form-check" />
		  </div>
		  
		  <div style="margin-top: 10px" class="form-group">						
			<div class="col-sm-6 controls">
			  <button type="submit"
					  class="btn btn-primary">送出
			  </button>
			</div>
		  </div>
		  
		  <div style="margin-top: 10px" class="form-group">						
			<div class="col-sm-6 controls">
			  <a href="${pageContext.request.contextPath}" 
			     class="btn btn-success">回到首頁</a>
			</div>
		  </div>
		  
		</form:form>
	  </div>
	</div>
  </div>
</body>
</html>