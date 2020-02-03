package net.koreate.board.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.koreate.board.dao.BoardDAO;
import net.koreate.board.vo.BoardVO;
import net.koreate.mvc.common.util.PageMaker;
import net.koreate.mvc.common.util.SearchCriteria;

@Service
public class BoardServiceImpl implements BoardService {

	@Inject
	BoardDAO dao;

	@Override
	@Transactional
	public void register(BoardVO board) throws Exception {
		dao.register(board);
		dao.updateOrigin();
		
		List<String> files = board.getFiles();
		if(files == null) {
			return;
		}
		
		for(String fullName : files) {
			dao.addAttach(fullName);
		}
	}

	@Override
	public List<BoardVO> listReplyCriteria(SearchCriteria cri) throws Exception {
		return dao.listReplyCriteria(cri);
	}

	@Override
	public PageMaker getPageMaker(SearchCriteria cri) throws Exception {
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(dao.listReplyCount(cri));
		return pageMaker;
	}

	@Override
	public void updateCnt(int bno) throws Exception {
		dao.updateCnt(bno);

	}

	@Override
	public BoardVO readReply(int bno) throws Exception {
		return dao.readReply(bno);
	}
	
	@Transactional
	@Override
	public void replyRegister(BoardVO vo) throws Exception {
		// 기존 글들의 seq 정렬 순서 값 수정
		dao.updateReply(vo);
		
		vo.setDepth(vo.getDepth()+1);
		vo.setSeq(vo.getSeq()+1);
		
		System.out.println("등록된 게시물 정보 : "+vo);
		
		dao.replyRegister(vo);
	}
	
	@Transactional
	@Override
	public void modify(BoardVO vo) throws Exception {
		
		// 첨부파일 목록 삭제
		dao.deleteAttach(vo.getBno());
		
		List<String> files = vo.getFiles();
		if(files == null) {
			return;
		}
		
		for(String fullName : files) {
			dao.addAttach(fullName);
		}
		
		dao.modify(vo);
	}

	@Override
	public void remove(int bno) throws Exception {
		
		// 첨부파일 목록 삭제
		dao.deleteAttach(bno);
		
		
		// 게시글 삭제
		dao.remove(bno);
	}

	@Override
	public List<String> getAttach(int bno) throws Exception {
		return dao.getAttach(bno);
	}
	
	
	
	
	

}
