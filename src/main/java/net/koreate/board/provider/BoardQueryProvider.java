package net.koreate.board.provider;

import org.apache.ibatis.jdbc.SQL;

import net.koreate.mvc.common.util.SearchCriteria;

public class BoardQueryProvider {
	public String searchSelectSql(SearchCriteria cri) {
		/*
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("re_tbl_board");
		sql.WHERE("title LIKE CONCAT('%','"+cri.getKeyword()+"','%')")
		.OR()
		.WHERE("content LIKE CONCAT('%','"+cri.getKeyword()+"','%')")
		.ORDER_BY("origin DESC, seq ASC")
		.LIMIT(cri.getPageStart()+","+cri.getPerPageNum());
		*/
		
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("re_tbl_board");
				
				getSearchWhere(cri, this);
				
				ORDER_BY("origin DESC, seq ASC");
				LIMIT(cri.getPageStart()+","+cri.getPerPageNum());
			}
		};
		String query = sql.toString();
		System.out.println(query);
		return query;
	}
	
	public String searchSelectCount(SearchCriteria cri) {
		return new SQL() {
			{
				SELECT("count(*)");
				FROM("re_tbl_board");
				getSearchWhere(cri, this);
			}
		}.toString();
	}
	
	public void getSearchWhere(SearchCriteria cri, SQL sql) {
		if(cri.getSearchType() != null) {
			String titleQuery = "title LIKE CONCAT('%','"+cri.getKeyword()+"','%')";
			String contentQuery = "content LIKE CONCAT('%','"+cri.getKeyword()+"','%')";
			String writerQuery = "writer LIKE CONCAT('%','"+cri.getKeyword()+"','%')";
			
			switch(cri.getSearchType()) {
				case "t" :
					sql.WHERE(titleQuery);
					break;
				case "c" :
					sql.WHERE(contentQuery);
					break;
				case "w" :
					sql.WHERE(writerQuery);
					break;
				case "tc" :
					sql.WHERE(titleQuery).OR().WHERE(contentQuery);
					break;
				case "cw" :
					sql.WHERE(contentQuery).OR().WHERE(writerQuery);
					break;
				case "tcw" :
					sql.WHERE(titleQuery).OR().WHERE(contentQuery).OR().WHERE(writerQuery);
					break;
			}
		}
	}
}
