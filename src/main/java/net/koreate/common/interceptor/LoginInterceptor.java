package net.koreate.common.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import net.koreate.common.session.MySessionEventListener;
import net.koreate.user.dao.UserDAO;
import net.koreate.user.service.UserService;
import net.koreate.user.vo.BanIPVO;
import net.koreate.user.vo.LoginDTO;
import net.koreate.user.vo.UserVO;

public class LoginInterceptor extends HandlerInterceptorAdapter{
	
	@Inject
	UserService us;
	
	@Inject
	UserDAO dao;
	
	@Inject
	MySessionEventListener listener;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		
		if(session.getAttribute("userInfo") != null) {
			session.removeAttribute("userInfo");
		}
		
		String ip = getIP(request);
		System.out.println("preHandle : "+ip);
		
		BanIPVO banVO = dao.getBanIPVO(ip);
		
		if(banVO != null && banVO.getCnt() >= 5) {
			long saveTime = getTime(banVO.getBandate());
			if(saveTime > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
				String now = sdf.format(new Date(saveTime));
				
				RequestDispatcher rd = request.getRequestDispatcher("/user/signIn");
				request.setAttribute("message", "일정시간동안 로그인할 수 없습니다. 남은시간 : "+now);
				rd.forward(request, response);
				return false;
			}else {
				System.out.println("제한 시간 초기화");
				dao.removeBanIP(ip);
			}
		}
		
		return true;
	}

	

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		ModelMap modelObj = modelAndView.getModelMap();
		
		LoginDTO dto = (LoginDTO) modelObj.get("loginDTO");
		
		System.out.println("LoginInterceptor postHandle : "+dto);
		
		UserVO userInfo = us.signIn(dto);
		
		String ip = getIP(request);
		
		BanIPVO vo = dao.getBanIPVO(ip);
		
		HttpSession session = request.getSession();
		
		if(userInfo != null) {
			
			boolean result = listener.expireDuplicatedSession(dto.getUid(), session.getId());
			
			if(result) {
				System.out.println("중복 제거");
			}else {
				System.out.println("최초 로그인");
			}
			
			session.setAttribute("userInfo", userInfo);
			
			if(dto.isUserCookie()) {
				Cookie cookie = new Cookie("signInCookie", userInfo.getUid());
				cookie.setPath("/");
				cookie.setMaxAge(60*60*24*15);
				response.addCookie(cookie);
			}
			if(vo != null) {
				dao.removeBanIP(ip);
				System.out.println("로그인 성공 후 ban_ip 초기화");
			}
		}else {
			int count = 5;
			
			String message = "";
			
			if(vo == null) {
				System.out.println("최초 실패");
				dao.signInFail(ip);
				count = count - 1;
			}else {
				System.out.println(vo);
				dao.updateBanIPCnt(ip);
				count = count -(vo.getCnt()+1);
			}
			
			if(count > 0) {
				message = "회원 정보가 일치하지 않습니다. 남은 시도 횟수 : "+count;
				
			}else {
				message = "너무 많은 시도.... 30분 동안 IP가 차단됩니다. ";
			}
			
			modelAndView.addObject("message", message);
			modelAndView.setViewName("/user/signIn");
			
		}
		
		
		
	}
	
	public String getIP(HttpServletRequest request) {
		
		String ip = request.getHeader("X-forwarded-For");
		
		if(ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
			System.out.println("Proxy-Client-IP : "+ip);
		}
		
		if(ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			System.out.println("WL-Proxy-Client-IP : "+ip);
		}
		
		if(ip == null) {
			ip = request.getHeader("HTTP-X_FORWARDED-FOR");
			System.out.println("HTTP-X_FORWARDED-FOR : "+ip);
		}
		
		if(ip == null) {
			ip = request.getRemoteAddr();
			System.out.println("removeAddr : "+ip);
		}
		
		return ip;
	}
	
	private long getTime(Date bandate) {
		int limit = 1000*60*30;
		System.out.println("limit : "+limit);
		return limit - (System.currentTimeMillis()-bandate.getTime());
	}
	
}
