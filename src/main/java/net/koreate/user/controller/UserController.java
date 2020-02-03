package net.koreate.user.controller;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import net.koreate.user.service.UserService;
import net.koreate.user.vo.LoginDTO;
import net.koreate.user.vo.UserVO;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Inject
	UserService us;
	
	@RequestMapping("/signIn")
	public String signIn() {
		return "/user/signIn";
	}
	
	@RequestMapping("/signUp")
	public String signUp() {
		return "/user/signUp";
	}
	
	@PostMapping("/signInPost")
	public ModelAndView signInPost(LoginDTO dto, ModelAndView mav) throws Exception{
		
		mav.addObject("loginDTO", dto);
		mav.setViewName("redirect:/");
		return mav;
	}
	
	
	@PostMapping("/signUpPost")
	public String signUpPost(UserVO vo, RedirectAttributes rttr) throws Exception{
		us.signUp(vo);
		rttr.addFlashAttribute("message", "회원가입 성공");
		return "redirect:/user/signIn";
	}
	
	@GetMapping("/signOut")
	public String signOut(
			HttpSession session, 
			HttpServletRequest request, 
			HttpServletResponse response, 
			@CookieValue(name="signInCookie", required=false)Cookie signInCookie) throws Exception{
		if(session.getAttribute("userInfo") != null) {
			session.removeAttribute("userInfo");
			if(signInCookie != null) {
				System.out.println("signInCookie key : "+signInCookie.getName());
				System.out.println("signInCookie value : "+signInCookie.getValue());
				
				signInCookie.setPath("/");
				signInCookie.setMaxAge(0);
				response.addCookie(signInCookie);
			}
		}
//		Cookie[] cookie = request.getCookies();
		/*
		Cookie cookie = WebUtils.getCookie(request, "signInCookie");
		if(cookie != null) {
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		*/
		
		
		
		
		return "redirect:/";
	}
	
}
