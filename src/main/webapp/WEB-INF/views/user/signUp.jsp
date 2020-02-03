<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	${pageContext.request.contextPath}
	<h3><a href="${pageContext.request.contextPath}">HOME</a></h3>
	<h3>SIGN UP</h3>
	<form action="signUpPost" method="post">
		<table>
			<tr>
				<td>아이디</td>
				<td>
					<input type="text" name="uid" placeholder="USER ID" required />
				</td>
			</tr>
			<tr>
				<td>비밀번호</td>
				<td>
					<input type="password" name="upw" placeholder="USER PW" required />
				</td>
			</tr>
			<tr>
				<td>비밀번호 확인</td>
				<td>
					<input type="password" name="repw" placeholder="USER REPW" required />
				</td>
			</tr>
			<tr>
				<td>사용자 이름</td>
				<td>
					<input type="text" name="uname" placeholder="USER NAME" required />
				</td>
			</tr>
			<tr>
				
				<td colspan=2>
					<input type="submit" value="signUp" />
					<input type="button" onclick="location.href='signIn';" value="SignIn" />
				</td>
			</tr>
		</table>
	</form>
	<script>
		var message = '${message}';
		if(message != null && message!=''){
			alert(message);
		}
	</script>
</body>
</html>