package net.koreate.user.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import net.koreate.user.vo.BanIPVO;
import net.koreate.user.vo.LoginDTO;
import net.koreate.user.vo.UserVO;

public interface UserDAO {
	
	@Insert("INSERT INTO tbl_user(uid, upw, uname) VALUES(#{uid}, #{upw}, #{uname})")
	void signUp(UserVO vo) throws Exception;
	
	@Select("SELECT * FROM tbl_user WHERE uid=#{uid} AND upw=#{upw}")
	UserVO signIn(LoginDTO dto) throws Exception;
	
	@Select("SELECT * FROM tbl_user WHERE uid=#{uid}")
	UserVO getUserById(String uid)throws Exception;
	
	// ban ip 
	@Select("SELECT * FROM ban_ip WHERE ip = #{ip}")
	BanIPVO getBanIPVO(String ip) throws Exception;
	
	@Insert("INSERT INTO ban_ip(ip) VALUES(#{ip})")
	void signInFail(String ip) throws Exception;
	
	@Update("UPDATE ban_ip SET cnt=cnt+1, bandate=now() WHERE ip=#{ip}")
	void updateBanIPCnt(String ip) throws Exception;
	
	@Delete("DELETE FROM ban_ip WHERE ip=#{ip}")
	void removeBanIP(String ip) throws Exception;
	
}
