<%@ page language="java" contentType="text/html; charset=UTF-8"
				pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register form</title>
<meta name="viewport"
				content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Reference Bootstrap files -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>

<script	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<style>
.error {
	color: red
}
</style>
</head>
<body>
	<div>
		<div id="loginbox" style="margin-top: 50px;"
			class="mainbox col-md-3 col-md-offset-2 col-sm-6 col-sm-offset-2">	
			<div class="panel panel-primary">
				<div class="panel-heading">
					<div class="panel-title">Register New User</div>
				</div>
				<div style="padding-top: 30px" class="panel-body">
					<form:form method="POST"
							action="${pageContext.request.contextPath}/register/process"
							modelAttribute="registrationUser"
							class="form-horizontal">
						<div class="form-group">
							<div class="col-xs-15">
								<div>
								<!-- Check for registration error -->
									<c:if test="${registrationError != null}">
										<div class="alert alert-danger col-xs-offset-1 col-xs-10">
											${registrationError}</div>
									</c:if>
								</div>
							</div>
						</div>
						<div style="margin-bottom: 25px" class="input-group">
							<span class="input-group-addon">
								<i class="glyphicon glyphicon-user"></i>
							</span>
							<form:errors path="userInfo.users.username"
										cssClass="error" />
							<form:input path="userInfo.users.username"
										placeholder="username (*)"
										class="form-control" />
						</div>
						<div style="margin-bottom: 25px"
							class="input-group">
							<span class="input-group-addon">
								<i class="glyphicon glyphicon-lock"></i>
							</span>
							<form:errors path="userInfo.users.password"
										cssClass="error" />
							<form:password path="userInfo.users.password"
										placeholder="password (*)"
										class="form-control" />
						</div>
						<div style="margin-bottom: 25px"
							class="input-group">
							<span class="input-group-addon">
								<i class="glyphicon glyphicon-lock"></i>
							</span>
							<form:errors path="matchingPassword"
										cssClass="error" />
							<form:password path="matchingPassword"
										placeholder="matchingPassword (*)"
										class="form-control" />
						</div>
						<div style="margin-bottom: 25px"
							class="input-group">
							<span class="input-group-addon">
								<i class="glyphicon glyphicon-user"></i>
							</span>
							<form:input	path="userInfo.userDetail.nickname"
										placeholder="nickname"
										class="form-control" />
						</div>
						<div style="margin-bottom: 25px"
							class="input-group">
							<span class="input-group-addon">
								<i class="glyphicon glyphicon-user"></i>
							</span>
							<form:errors path="userInfo.userDetail.email"
										cssClass="error" />
							<form:input path="userInfo.userDetail.email"
										placeholder="email (*)"
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
										class="btn btn-primary">Register
								</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>