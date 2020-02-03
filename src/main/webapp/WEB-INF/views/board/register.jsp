<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.fileDrop{
		width: 100%;
		height: 150px;
		border: 1px solid gray;
		background-color: lightgray;
		margin: auto;
	}
	.uploadList{
		width:100%;
	}
	
	.uploadList li{
		float: left;
		padding: 20px;
		list-style: none;
	}
</style>
</head>
<body>
	<h1><a href="${pageContext.request.contextPath}">HOME</a></h1>
	<h3>REGISTER BOARD</h3>
	<form id="registerForm" action="register" method="post">
		<input type="hidden" name="uno" value="${userInfo.uno}" />
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
					<input type="button" id="saveBtn" value="Reg" />
				</td>
			</tr>
		</table>
		<div>
			<label>FILE DROP HERE</label>
			<div class="fileDrop">
			
			</div>
		</div>
		<div>
			<ul class="uploadList">
			
			</ul>
		</div>
	</form>
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript">var contextPath = '${pageContext.request.contextPath}';</script>
	<script src="${pageContext.request.contextPath}/resources/js/upload.js"></script>
	<script>
		
		
		
		
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
						$(".uploadList").append(html);
					}
				},
				error: function(res){
					alert(res.responseText);
				}
			});
			
		});
		
		$(".uploadList").on("click", ".delBtn", function(event){
			event.preventDefault();
			var target=$(this);
			$.ajax({
				type: "post",
				url: contextPath+"/deleteFile",
				data : {
					fileName : target.attr("href")
				},
				success: function(data){
					console.log(data);
					target.closest("li").remove();
				}
			});
		});
		
		$("#saveBtn").click(function(){
			var str = "";
			
			var fileList = $(".uploadList .delBtn");
			$(fileList).each(function(index){
				str +="<input type='hidden' name='files["+index+"]' value='"+$(this).attr("href")+"' />";
			});
			$("#registerForm").append(str);
			
			$("#registerForm").submit();
		});
	</script>
</body>
</html>