package net.koreate.board.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.koreate.board.service.BoardService;
import net.koreate.board.vo.BoardVO;
import net.koreate.mvc.common.util.SearchCriteria;

@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	@Inject
	BoardService service;
	
	@GetMapping("register")
	public String registerGet() {
		return "board/register";
	}
	
	@PostMapping("register")
	public String register(BoardVO board) throws Exception{
		System.out.println("register : "+board);
		service.register(board);
		return "redirect:/board/listReply";
	}
	
	@GetMapping("listReply")
	public String listReply(@ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception{
		System.out.println(cri);
		
		// 게시물 목록
		model.addAttribute("list", service.listReplyCriteria(cri));
		// 페이징 정보
		model.addAttribute("pageMaker", service.getPageMaker(cri));
		return "board/listReply";
	}
	
	@GetMapping("readPage")
	public String readPage(
			SearchCriteria cri, 
			int bno, 
			RedirectAttributes rttr) throws Exception{
		// 조회수 증가
		service.updateCnt(bno);
		rttr.addAttribute("bno", bno);
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());
		return "redirect:/board/read";
	}
	
	@GetMapping("read")
	public String read(
			@ModelAttribute("cri") SearchCriteria cri,
			Model model,
			int bno) throws Exception {
		System.out.println("read : "+cri);
		model.addAttribute("board", service.readReply(bno));
		return "board/readPage";
	}
	
	@GetMapping("replyRegister")
	public String replyRegisterGet(
			int bno,
			int uno,
			@ModelAttribute("cri") SearchCriteria cri,
			Model model
			) throws Exception{
		int babo = bno;
		model.addAttribute("board", service.readReply(babo));
		
		return "board/replyRegister";
	}
	
	@PostMapping("replyRegister")
	public String replyRegisterPost(
			RedirectAttributes rttr,
			BoardVO vo,
			SearchCriteria cri
			) throws Exception{
		
		// 답변글 등록
		service.replyRegister(vo);
		
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/board/listReply";
	}
	
	@GetMapping("modifyPage")
	public String modifyPage(
			int bno,
			@ModelAttribute("cri") SearchCriteria cri,
			Model model
			)throws Exception{
		model.addAttribute("board", service.readReply(bno));
		return "board/modifyPage";
	}
	
	@PostMapping("modifyPage")
	public String modifyPage(
			BoardVO vo,
			@ModelAttribute("cri") SearchCriteria cri,
			RedirectAttributes rttr
			)throws Exception{
		
		service.modify(vo);
		
		rttr.addAttribute("bno", vo.getBno());
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/board/read";
	}
	
	@PostMapping("remove")
	public String remove(
			@ModelAttribute("cri") SearchCriteria cri,
			int bno,
			RedirectAttributes rttr) throws Exception{
		
		// 삭제처리
		service.remove(bno);
		
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());
		return "redirect:/board/listReply";
	}
	
	@GetMapping("getAttach/{bno}")
	@ResponseBody
	public List<String> getAttach(
			@PathVariable("bno") int bno
			) throws Exception{
		System.out.println("getAttach : "+bno);
		return service.getAttach(bno);
	}
	
	
}
