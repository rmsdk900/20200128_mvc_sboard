<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.fileDrop {
		width: 100%;
		height: 150px;
		border: 1px solid gray;
		background-color: lightgray;
		margin: auto;
	}
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
	<!-- request: ${board} -->
	<h1><a href="${pageContext.request.contextPath}">HOME</a></h1>
	<h3>REGISTER BOARD</h3>
	<form id="modifyForm" action="modifyPage" method="post">
		<input type="hidden" name="uno" value="${userInfo.uno}" />
		<table>
			<tr>
				<td>제목</td>
				<td><input type="text" name="title" value="${board.title}" required/></td>
			</tr>
			<tr>
				<td>작성자</td>
				<td><input type="text" name="writer" value="${userInfo.uname}" readonly /></td>
			</tr>
			<tr>
				<td>내용</td>
				<td>
					<textarea rows=30 cols=50 name="content">${board.content}</textarea>
				</td>
			</tr>
			<tr>
				<td colspan=2>
					<input type="button" id="modBtn" value="Mod" />
				</td>
			</tr>
		</table>
		<div>
			<label>FILE DROP HERE</label>
			<div class="fileDrop">
			
			</div>
		</div>
		<hr/>
		<div>
			<ul class="uploadedList">
				
			</ul>
		</div>
		<div>
			<input type="hidden" name="bno" value="${board.bno}"/>
			<input type="hidden" name="page" value="${cri.page}"/>
			<input type="hidden" name="perPageNum" value="${cri.perPageNum}"/>
			<input type="hidden" name="searchType" value="${cri.searchType}"/>
			<input type="hidden" name="keyword" value="${cri.keyword}"/>
		</div>
	</form>
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript">var contextPath = '${pageContext.request.contextPath}';</script>
	<script src="${pageContext.request.contextPath}/resources/js/upload.js"></script>
	<script>
	
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
				html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				html += "<a href='"+fileInfo.fullName+"' class='delBtn'>[삭제]</a>";
				html += "</div>";
				html += "</li>";
				$(".uploadedList").append(html);
			});
		});
		
		var delArr= [];
		$(".uploadedList").on("click", ".delBtn", function(event){
			event.preventDefault();
			var target=$(this);
			delArr.push(target.attr("href"));
			target.closest("li").remove();
		});
		
		$(".fileDrop").on("dragenter dragover", function(event){
			event.preventDefault();
		});
		
		$(".fileDrop").on("drop", function(event){
			event.preventDefault();
			
			//upload
			var files = event.originalEvent.dataTransfer.files;
			console.log(files);
			
			var maxSize = 10485760;
			var formData = new FormData();
			
			// 기존에 있던 애들도 추가
			
			for(var i=0; i<files.length; i++){
				if (files[i].size > maxSize){
					alert("업로드 할 수 없는 파일이 포함되어있습니다. size : "+files[i].size);
					return;
				}
				formData.append("file", files[i]);
			}
			
			$.ajax({
				type: "POST",
				data: formData,
				dataType: "json",
				url: contextPath+"/uploadFile",
				processData: false,
				contentType: false,
				success: function(data){
					console.log(data);
					console.log(data.length);
					for(var i=0;i<data.length;i++){
						
						var fileInfo = getFileInfo(data[i]);
						
						var html = "<li>";
						html += "<img src='"+fileInfo.imgSrc+"' alt='attachment' />";
						html += "<div>";
						html += "<a href='"+fileInfo.getLink+"' target='_blank' >";
						html += fileInfo.fileName;
						html += "</a>";
						html += "</div>";
						html += "<div>";
						html += "<a href='"+fileInfo.fullName+"' class='delBtn'>X</a>";
						html += "</div>";
						html += "</li>";
						$(".uploadedList").append(html);
					}
				},
				error: function(res){
					alert(res.responseText);
				}
			});
			
		});
		$("#modBtn").click(function(){
			if(delArr.length >0){
				$.post(contextPath+"/deleteAllFiles", {files: delArr}, function(result){
					console.log(result);
				});
			}
			
			var str = "";
			
			var fileList = $(".uploadedList .delBtn");
			$(fileList).each(function(index){
				str +="<input type='hidden' name='files["+index+"]' value='"+$(this).attr("href")+"' />";
			});
			$("#modifyForm").append(str);
			
			$("#modifyForm").submit();
		});
		
		
		
		
	</script>
</body>
</html>