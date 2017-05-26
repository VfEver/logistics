package com.logistics.dao;

import java.util.List;
import java.util.Map;

import com.logistics.entity.User;
/**
 * user dao
 * @author zys
 *
 */
public interface UserMapper {
	
	public void saveUser(User user);
	
	public User findUserByAccount(String account);
	
	public User findUser(Map<String, String> map);
	
	public List<User> findAllUser();
	
	public List<User> findUserByStatus(int status);
	
	public void updateUser(Map<String, String> map);
}
