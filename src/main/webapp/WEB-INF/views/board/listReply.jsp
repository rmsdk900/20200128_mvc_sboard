<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>listReply</title>
</head>
<body>
	<h1><a href="${pageContext.request.contextPath}">HOME</a></h1>
	<div>
		<select name="searchType">
			<option value="n">-----------------------</option>
			<option value="t">TITLE</option>
			<option value="c">CONTENT</option>
			<option value="w">WRITER</option>
			<option value="tc">TITLE & CONTENT</option>
			<option value="cw">CONTENT & WRITER</option>
			<option value="tcw">TITLE & CONTENT & WRITER</option>
		</select>
		<input type="text" name="keyword" id="keyword"/>
		<input type="button" id="searchBtn" value="검색"/>
		<input type="button" id="newBtn" value="글작성"/>
	</div>
	<br/>
	<table border=1>
		<tr>
			<th>BNO</th>
			<th>TITLE</th>
			<th>WRITER</th>
			<th>UPDATEDATE</th>
			<th>VIEWCNT</th>
		</tr>
		<!-- 게시글 목록 ${list} -->
		<c:choose>
			<c:when test="${!empty list}">
				<!-- 목록 출력 -->
				<c:forEach var="board" items="${list}">
					<c:choose>
						<c:when test="${board.showboard == 'y'}">
							<tr>
								<td>${board.bno}</td>
								<td>
									<a style="text-decoration:none;" href="readPage${pageMaker.search(pageMaker.cri.page)}&bno=${board.bno}">
										<c:if test="${board.depth != 0}">
											<c:forEach var="i" begin="1" end="${board.depth}">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</c:forEach>
											└
										</c:if>
										<c:out value="${board.title}" escapeXml="true"/>
									</a>
								</td>
								<td>${board.writer}</td>
								<td>
									<f:formatDate value="${board.updatedate}" pattern="yyyy-MM-dd hh:mm" />
								</td>
								<td>${board.viewcnt}</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<td></td>
								<td>삭제된 게시물 입니다.</td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan=5>등록된 게시물이 없습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</table>
	<!-- 페이징 블럭 -->
	<c:if test="${pageMaker.prev}">
		<a href="listReply${pageMaker.search(pageMaker.startPage-1)}">&laquo;</a>
	</c:if>
	<c:forEach var="i" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
		<a href="listReply${pageMaker.search(i)}">${i}</a>
	</c:forEach>
	<c:if test="${pageMaker.next}">
		<a href="listReply${pageMaker.search(pageMaker.endPage+1)}">&raquo;</a>
	</c:if>
	
	
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript">
		$("#searchBtn").click(function(){
			var searchValue = $("select option:selected").val();
			var keywordValue = $("#keyword").val();
			console.log("searchValue : "+searchValue + " | "+"keywordValue : "+keywordValue);
			location.href="listReply?searchType="+searchValue+"&keyword="+keywordValue;
		});
		
		$("#newBtn").click(function(){
			location.href="register";
		});
	</script>
</body>
</html>