<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h3><a href="${pageContext.request.contextPath}">HOME</a></h3>
	<h3>SIGN IN</h3>
	<form action="signInPost" method="POST">
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
				<td colspan=2>
					<label>
						<input type="checkbox" name="userCookie" />
						로그인 정보 저장
					</label>
				</td>
			</tr>
			<tr>
				<td colspan=2>
					<input type="submit" value="signIn" />
					<input type="button" onclick="location.href='signUp';" value="signUp" />
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