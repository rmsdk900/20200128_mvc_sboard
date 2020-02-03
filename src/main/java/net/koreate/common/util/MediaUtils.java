package net.koreate.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

public class MediaUtils {
	private static Map<String, MediaType> mediaType;
	
	static {
		mediaType = new HashMap<>();
		mediaType.put("JPEG", MediaType.IMAGE_JPEG);
		mediaType.put("JPG", MediaType.IMAGE_JPEG);
		mediaType.put("PNG", MediaType.IMAGE_PNG);
		mediaType.put("GIF", MediaType.IMAGE_GIF);
	}
	
	public static MediaType getMediaType(String ex) {
		return mediaType.get(ex.toUpperCase());
	}
}
