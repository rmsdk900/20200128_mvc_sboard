<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.uploadedList{
		width: 100%;
	}
	.uploadedList li {
		float: left;
		padding: 20px;
		list-style: none;
	}
</style>
</head>
<body>
	<!-- request : ${board} -->
	<h1><a href="${pageContext.request.contextPath}">HOME</a></h1>
	<h3>READ PAGE</h3>
	<table border=1>
		<tr>
			<td>Title</td>
			<td><c:out value="${board.title}" escapeXml="true" /></td>
		</tr>
		<tr>
			<td>Writer</td>
			<td><c:out value="${board.writer}" escapeXml="true" /></td>
		</tr>
		<tr>
			<td>Content</td>
			<td><c:out value="${board.content}" escapeXml="true" /></td>
		</tr>
	</table>
	<hr/>
	<div>
		<ul class="uploadedList">
			
		</ul>
	</div>
	
	<div style="clear:both;"></div>
	<hr/>
	<div>
		<c:if test="${!empty userInfo}">
			<c:if test="${userInfo.uno eq board.uno}">
				<input type="button" id="modifyBtn" value="MODIFY" />
				<input type="button" id="deleteBtn" value="DELETE" />
			</c:if>
			<input type="button" id="replyBtn" value="REPLY"/>
		</c:if>
		<input type="button" id="listBtn" value="LIST"/>
	</div>
	<form id="readForm">
		<input type="hidden" name="bno" value="${board.bno}"/>
		<input type="hidden" name="page" value="${cri.page}"/>
		<input type="hidden" name="perPageNum" value="${cri.perPageNum}"/>
		<input type="hidden" name="searchType" value="${cri.searchType}"/>
		<input type="hidden" name="keyword" value="${cri.keyword}"/>
	</form>
	
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript">var contextPath = '${pageContext.request.contextPath}';</script>
	<script src="${pageContext.request.contextPath}/resources/js/upload.js"></script>
	<script>
		var obj = $("#readForm");
		$("#listBtn").click(function(){
			obj.attr("action", "listReply");
			obj.submit();
		});
		$("#replyBtn").click(function(){
			obj.attr("action", "replyRegister");
			obj.submit();
		});
		$("#modifyBtn").click(function(){
			obj.attr("action", "modifyPage");
			obj.submit();
		});
		$("#deleteBtn").click(function(){
			var isDelete = confirm("첨부된 파일이 모두 삭제 됩니다. 삭제하시겠습니까?");
			if(isDelete){
				var arr = [];
				$(".uploadedList li").each(function(index){
					arr.push($(this).attr("data-src"));
				});
				
				if(arr.length > 0){
					$.post(contextPath+"/deleteAllFiles", {files: arr}, function(result){
						alert(result);
					});
				}
				
				
				obj.attr("action", "remove");
				obj.attr("method", "post");
				obj.submit();	
			}else {
				alert("삭제 요청이 취소되었습니다.")
			}
			
		});
		var bno = ${board.bno};
		$.getJSON("getAttach/"+bno, function(data){
			// 첨부파일 목록 = data
			$(data).each(function(){
				var fileInfo = getFileInfo(this);
				console.log(fileInfo);
				var html = "<li data-src='"+fileInfo.fullName+"'>";
				html += "<span>";
				html += "<img src='"+fileInfo.imgSrc+"' alt='attachment' />";
				html += "</span>";
				html += "<div>";
				html += "<a href='"+fileInfo.getLink+"' target='_blank'>";
				html += fileInfo.fileName;
				html += "</a>";
				html += "</div>";
				html += "</li>";
				$(".uploadedList").append(html);
			});
			
		});
		
		
	</script>
</body>
</html>