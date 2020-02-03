package net.koreate.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import net.koreate.board.provider.BoardQueryProvider;
import net.koreate.board.vo.BoardVO;
import net.koreate.mvc.common.util.SearchCriteria;

public interface BoardDAO {
	@Insert("INSERT INTO re_tbl_board(title,content,writer,uno) "
			+ " VALUES(#{title}, #{content}, #{writer}, #{uno})")
	void register(BoardVO board) throws Exception;
	
	@Update("UPDATE re_tbl_board SET origin=LAST_INSERT_ID() WHERE bno= LAST_INSERT_ID()")
	void updateOrigin() throws Exception;
	
	// 검색된 게시물의 페이징 처리
	@SelectProvider(type=BoardQueryProvider.class, method="searchSelectSql")
	List<BoardVO> listReplyCriteria(SearchCriteria cri) throws Exception;
	
	// 검색된 게시물의 갯수
	@SelectProvider(type=BoardQueryProvider.class, method="searchSelectCount")
	int listReplyCount(SearchCriteria cri) throws Exception;
	
	
	//게시물 조회수 증가
	@Update("UPDATE re_tbl_board SET viewcnt = viewcnt+1 WHERE bno=#{bno}")
	void updateCnt(int bno) throws Exception;
	
	// 게시물 상세 보기
	@Select("SELECT * FROM re_tbl_board WHERE bno=#{bno}")
	BoardVO readReply(int bno) throws Exception;

	// 답글들의 정렬값 수정
	@Update("UPDATE re_tbl_board SET seq = seq+1 WHERE origin = #{origin} AND seq > #{seq}")
	void updateReply(BoardVO vo) throws Exception;
	// 답변글 등록
	@Insert("INSERT INTO re_tbl_board(title, content, writer, origin, depth, seq, uno) "
			+ " VALUES(#{title}, #{content}, #{writer}, #{origin}, #{depth}, #{seq}, #{uno})")
	void replyRegister(BoardVO vo) throws Exception;
	
	// 게시글 수정
	@Update("UPDATE re_tbl_board SET title=#{title}, content=#{content}, updatedate=now() "
			+ " WHERE bno=#{bno}")
	void modify(BoardVO vo) throws Exception;
	
	//게시글 삭제
	@Update("UPDATE re_tbl_board SET showboard = 'n' WHERE bno=#{bno}")
	void remove(int bno) throws Exception;

	// 첨부파일 
	
	// 첨부파일 등록
	@Insert("INSERT INTO tbl_attach(fullName, bno) VALUES(#{fullName}, LAST_INSERT_ID())")
	void addAttach(String fullName) throws Exception;
	
	// 첨부파일 목록 불러오기
	@Select("SELECT fullName FROM tbl_attach WHERE bno=#{bno}")
	List<String> getAttach(int bno) throws Exception;
	// 첨부파일 목록 삭제
	@Delete("DELETE FROM tbl_attach WHERE bno=#{bno}")
	void deleteAttach(int bno) throws Exception;
	
}
