package net.koreate.board.service;

import java.util.List;

import net.koreate.board.vo.BoardVO;
import net.koreate.mvc.common.util.PageMaker;
import net.koreate.mvc.common.util.SearchCriteria;

public interface BoardService {
	
	// 게시글 작성
	void register(BoardVO board) throws Exception;
	
	// 게시물 목록
	List<BoardVO> listReplyCriteria(SearchCriteria cri) throws Exception;
	
	// pageMaker
	PageMaker getPageMaker(SearchCriteria cri) throws Exception;

	// 게시물 조회수 증가
	void updateCnt(int bno)throws Exception;
	
	// 게시물 상세 보기
	BoardVO readReply(int bno) throws Exception;
	
	// 답변글 등록
	void replyRegister(BoardVO vo) throws Exception;
	
	//게시글 수정
	void modify(BoardVO vo)throws Exception;
	
	// 게시글 삭제
	void remove(int bno) throws Exception;

	// 첨부파일 목록
	List<String> getAttach(int bno) throws Exception;
		
}
