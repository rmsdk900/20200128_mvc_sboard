package net.koreate.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	
	ServletContext context;
	
	String uploadPath;
	
	private static FileUtils utils;
	
	private FileUtils() {}
	
	private FileUtils(ServletContext context) {
		this.context = context;
		createUploadPath();
	}
	
	public static FileUtils getInstance(ServletContext context) {
		if(utils == null) {
			utils = new FileUtils(context);
		}
		return utils;
	}
	
	private void createUploadPath() {
		uploadPath = context.getRealPath(File.separator+"upload");
		System.out.println("uploadPath : "+uploadPath);
		File file = new File(uploadPath);
		
		if(!file.exists()) {
			System.out.println("경로 생성");
			file.mkdirs();
		}
		
	}
	
	public List<String> uploadFile(MultipartFile[] files) throws Exception{
		List<String> fileList = new ArrayList<>();
		
		for(MultipartFile file : files) {
			try {
				String fileName = uploadFile(file);
				fileList.add(fileName);
			} catch (Exception e) {
				e.printStackTrace();
				throw new IOException("업로드 할 수 없는 파일이 포함되어 있습니다.");
			}
		}
		
		return fileList;
	}
	
	public String uploadFile(MultipartFile file) throws Exception{
		String uploadFolder = getFolder();
		System.out.println("uploadFolder : "+uploadFolder);
		String originalName = file.getOriginalFilename();
		
		
		UUID uid = UUID.randomUUID();
		String savedName = uid.toString().replace("-", "")+"_"+originalName;
		System.out.println("uploadFile savedName : "+savedName);
		
		File uploadFolderFile = new File(uploadPath, uploadFolder);
		if(!uploadFolderFile.exists()) {
			uploadFolderFile.mkdirs();
		}
		File upload = new File(uploadPath+File.separator+uploadFolder, savedName);
		
		System.out.println("upload file absolute path"+upload.getAbsolutePath());
		
		byte[] fileData = file.getBytes();
		
		FileCopyUtils.copy(fileData, upload);
		
		return makeFileUploadName(uploadFolder, savedName);
	}
	
	private String makeFileUploadName(String uploadFolder, String savedName) throws IOException{
		
		String ex = savedName.substring(savedName.lastIndexOf(".")+1);
		String thumbnail = uploadPath+File.separator+uploadFolder+File.separator+savedName;
		
		
		System.out.println("thumbnail : "+thumbnail);
		
		if(MediaUtils.getMediaType(ex)!= null) {
			System.out.println("IMAGE");
			
			try {
				File file = new File(uploadPath+File.separator+uploadFolder, savedName);
				
				BufferedImage fileImage = ImageIO.read(file);
				
				BufferedImage sourceImage = Scalr.resize(fileImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT,100);
				
				thumbnail = uploadPath+File.separator+uploadFolder+File.separator+"s_"+savedName;
				file = new File(thumbnail);
				
				ImageIO.write(sourceImage, ex, file);
			} catch (Exception e) {
				e.printStackTrace();
				throw new IOException();
			} 
			
		}
		
		return thumbnail.substring(uploadPath.length()).replace(File.separatorChar, '/');
	}
	
	public byte[] displayFile(String fileName) throws IOException{
		System.out.println("보여줄 fileName : "+fileName);
		
		InputStream in = null;
		String path = uploadPath+(fileName).replace('/', File.separatorChar);
		byte[] bytes = null;
		try {
			in = new FileInputStream(path);
			bytes = IOUtils.toByteArray(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return bytes;
	}
	
	public HttpHeaders getHeader(String fileName) throws Exception{
		HttpHeaders header = null;
		
		header = new HttpHeaders();
		String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
		
		MediaType mType = MediaUtils.getMediaType(formatName);
		if(mType != null) {
			System.out.println("IMAGE");
			header.setContentType(mType);
		}else {
			fileName = fileName.substring(fileName.indexOf("_")+1);
			header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			header.add("content-disposition", "attachment;fileName=\""+new String(fileName.getBytes("utf-8"), "ISO-8859-1")+"\"");
		}
		
		return header;
	}
	
	public String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(new Date());
		System.out.println(str);
		
		return str.replace("-", File.separator);
	}

	public String deleteAllFiles(String[] files) {
		for(String file : files) {
			String ex = file.substring(file.lastIndexOf(".")+1);
			if(MediaUtils.getMediaType(ex)!= null) {
				// 원본도 삭제
				String name = file.replace("s_", "");
				new File(uploadPath+(name).replace('/', File.separatorChar)).delete();
			}
			new File(uploadPath+(file).replace('/', File.separatorChar)).delete();
		}
		return "SELECTED FILES IS DELETED";
	}

	public String deleteFile(String fileName) {
		String ex = fileName.substring(fileName.lastIndexOf(".")+1);
		if(MediaUtils.getMediaType(ex)!= null) {
			// 원본도 삭제
			String name = fileName.replace("s_", "");
			new File(uploadPath+(name).replace('/', File.separatorChar)).delete();
		}
		new File(uploadPath+(fileName).replace('/', File.separatorChar)).delete();
		return "DELETED";
	}
	
	
	
}
