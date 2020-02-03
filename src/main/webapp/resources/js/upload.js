/**
 * file upload / display 
 */

/* image type이면 true 아니면 false */
function checkImageType(fileName){
	var pattern = /jpg|gif|png|jpeg/i;
	return fileName.match(pattern);
}

function getFileInfo(fullName){
	var imgSrc, fileName, getLink;

	if(checkImageType(fullName)){
		imgSrc = contextPath+"/displayFile?fileName="+fullName;
		getLink = contextPath+"/displayFile?fileName="+fullName.replace("s_","");
	}else {
		imgSrc = contextPath+"/resources/img/file.png";
		getLink = contextPath+"/displayFile?fileName="+fullName;
	}

	fileName = fullName.substr(fullName.lastIndexOf("_")+1);
	return {fileName: fileName, imgSrc: imgSrc, fullName: fullName, getLink: getLink };
}