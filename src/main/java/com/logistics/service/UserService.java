package com.logistics.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.logistics.dao.UserMapper;
import com.logistics.entity.User;
import com.logistics.utils.MybatisUtils;

/**
 * user service
 * @author zys
 *
 */
public class UserService {
	
	private SqlSessionFactory sqlSessionFactory;

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	/**
	 * 保存用户
	 * @param user
	 */
	public void saveUser(User user) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		
		userMapper.saveUser(user);
		sqlSession.commit();
		
		MybatisUtils.closeSqlSession(sqlSession);
	}
	
	/**
	 * 根据账号查询用户
	 * @param account
	 * @return
	 */
	public User findUserByAccount(String account) {

		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

		User user = userMapper.findUserByAccount(account);
		
		MybatisUtils.closeSqlSession(sqlSession);
		
		return user;
	}
	
	/**
	 * 根据账号密码查询用户
	 * @param account
	 * @param password
	 * @return
	 */
	public User findUser(String account, String password) {

		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

		Map<String, String> map = new HashMap<>();
		map.put("account", account);
		map.put("password", password);
		
		User user = userMapper.findUser(map);
		
		MybatisUtils.closeSqlSession(sqlSession);
		
		return user;
	}
	
	/**
	 * 查询所有用户信息
	 * @return
	 */
	public List<User> findAllUser() {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		
		List<User> userList = userMapper.findAllUser();
		
		MybatisUtils.closeSqlSession(sqlSession);
		
		return userList;
	}
}
