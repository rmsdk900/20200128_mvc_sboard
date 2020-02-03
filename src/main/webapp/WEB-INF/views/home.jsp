<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:choose>
		<c:when test="${!empty userInfo}">
			<span>${userInfo.uname}</span>
			<a href="board/register">글 작성</a>
			<a href="user/signOut">로그아웃</a>
		</c:when>
		<c:otherwise>
			<a href="user/signIn">로그인</a>
			<a href="user/signUp">회원가입</a>	
		</c:otherwise>
	</c:choose>
	<br/>
	<a href="board/listReply">답변형 게시판</a>
</body>
</html>