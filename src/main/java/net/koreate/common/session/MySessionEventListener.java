package net.koreate.common.session;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

import net.koreate.user.vo.UserVO;

@Component
public class MySessionEventListener implements HttpSessionListener, HttpSessionAttributeListener{
	
	private static MySessionEventListener mySessionEventListener = null;
	
	private static Hashtable<String, Object> sessionRepository;
	
	public MySessionEventListener() {
		System.out.println("MySessionEventListener created");
		if(sessionRepository == null) {
			sessionRepository = new Hashtable<>();
		}
	}
	public static synchronized MySessionEventListener getInstance() {
		if(mySessionEventListener == null) {
			mySessionEventListener = new MySessionEventListener();
		}
		return mySessionEventListener;
	}
	
	public boolean expireDuplicatedSession(String uid, String sessionId) {
		boolean result = false;
		
		System.out.println("Active Session count : "+sessionRepository.size());
		
		Enumeration<Object> enumeration = sessionRepository.elements();
		System.out.println("session id : "+sessionId + " uid : "+uid);
		
		while(enumeration.hasMoreElements()) {
			HttpSession session = (HttpSession) enumeration.nextElement();
			UserVO user = (UserVO) session.getAttribute("userInfo");
			// 세선에 사용자 정보가 존재
			if(user != null) {
				if(user.getUid().equals(uid)) {
					if(!session.getId().equals(sessionId)) {
						System.out.println("login - user "+user.getUid()+" sessionId : "+sessionId);
						session.invalidate();
						return true;
					}
				}
			}
			
		}
		
		return result;
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		System.out.println("attributeAdded 호출");
		
		if("userInfo".equals(event.getName())) {
			HttpSession session = event.getSession();
			synchronized (sessionRepository) {
				System.out.println("session registered! : "+session.getId());
				sessionRepository.put(session.getId(), session);
			}
		}
		
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		
		synchronized (sessionRepository) {
			System.out.println("session destroyed : "+session.getId());
			sessionRepository.remove(session.getId());
		}
	}
	
}
