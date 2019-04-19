<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Enter user data</title>
</head>
<body>
	<form:form method="POST" action="${pageContext.request.contextPath}/user/showUser" modelAttribute="userInfo">
		<table>
			<tr>
				<td><form:label path="user.username">(*)UserName</form:label></td>
				<td><form:input path="user.username"></form:input></td>
				<td><form:errors path="user.username"></form:errors></td>
			</tr>
			<tr>
				<td><form:label path="userDetail.nickname">NickName</form:label></td>
				<td><form:input path="userDetail.nickname"></form:input></td>
			</tr>
			<tr>
				<td><form:label path="userDetail.passwd">Password</form:label></td>
				<td><form:input path="userDetail.passwd"></form:input></td>
				<td><form:errors path="userDetail.passwd"></form:errors></td>
			</tr>
			<tr>
				<td><form:label path="userDetail.email">Email</form:label></td>
				<td><form:input path="userDetail.email"></form:input></td>
			</tr>
			<tr>
                <td><input type="submit" value="Submit"/></td>
            </tr>
			
		</table>
	</form:form>
</body>
</html>