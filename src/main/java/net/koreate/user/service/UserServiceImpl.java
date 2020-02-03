package net.koreate.user.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.koreate.user.dao.UserDAO;
import net.koreate.user.vo.LoginDTO;
import net.koreate.user.vo.UserVO;

@Service
public class UserServiceImpl implements UserService{
	
	@Inject
	UserDAO dao;
	
	@Override
	public void signUp(UserVO vo) throws Exception {
		dao.signUp(vo);
	}

	@Override
	public UserVO signIn(LoginDTO dto) throws Exception {
		return dao.signIn(dto);
	}

	@Override
	public UserVO getUserById(String uid) throws Exception {
		return dao.getUserById(uid);
	}
	
}
