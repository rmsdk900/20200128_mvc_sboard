<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1><a href="${pageContext.request.contextPath}">HOME</a></h1>
	<h3>REPLY REGISTER BOARD</h3>
	<form id="registerForm" action="replyRegister" method="post">
		<table>
			<tr>
				<td>제목</td>
				<td><input type="text" name="title" required/></td>
			</tr>
			<tr>
				<td>작성자</td>
				<td><input type="text" name="writer" value="${userInfo.uname}" readonly /></td>
			</tr>
			<tr>
				<td>내용</td>
				<td>
					<textarea rows=30 cols=50 name="content"></textarea>
				</td>
			</tr>
			<tr>
				<td colspan=2>
					<input type="submit" value="Reg" />
				</td>
			</tr>
		</table>
		<hr/>
		<div>
			<input type="hidden" name="uno" value="${userInfo.uno}" />
			<input type="hidden" name="origin" value="${board.origin}" />
			<input type="hidden" name="depth" value="${board.depth}" />
			<input type="hidden" name="seq" value="${board.seq}" />
			<input type="hidden" name="page" value="${cri.page}" />
			<input type="hidden" name="perPageNum" value="${cri.perPageNum}" />
			<input type="hidden" name="searchType" value="${cri.searchType}" />
			<input type="hidden" name="keyword" value="${cri.keyword}" />
		</div>
	</form>
</body>
</html>