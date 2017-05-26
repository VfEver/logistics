package com.logistics.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.logistics.entity.User;
import com.logistics.service.UserService;
import com.logistics.utils.StringUtils;
import com.logistics.utils.UserTypeUtils;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * user login
	 * 
	 * @param account
	 * @param password
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String register(
			@RequestParam(value = "account", defaultValue = "") String account,
			@RequestParam(value = "password", defaultValue = "") String password,
			HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();
		
		if (!StringUtils.isEmpty(account) && !StringUtils.isEmpty(password)) {

			json.put("status", 200);
			
			User user = new User();
			user.setAccount(account);
			user.setPassword(password);
			
			userService.saveUser(user);
			
			user = userService.findUser(account, password);
			if (user != null) {
					
				json.put("username", user.getAccount());
				json.put("userID", user.getUserID());
				json.put("userType", user.getType());
				json.put("job", UserTypeUtils.findUserJob(user.getType()));
				
			} else {
				json.put("reason", "注册出现问题");
			}
			
		} else {
			json.put("status", -1);
			json.put("reason", "注册出现问题");
		}

		return json.toString();
	}

	/**
	 * 查询账号是否已经注册过
	 * @param account
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/accountused", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String accountUsed(
			@RequestParam(value = "account", defaultValue = "") String account,
			HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();
		
		if (!StringUtils.isEmpty(account)) {

			json.put("status", 200);
			
			User user = userService.findUserByAccount(account);
			
			if (user == null) {
				json.put("used", false);
			} else {
				json.put("used", true);
			}
			
		} else {
			json.put("status", -1);
		}

		return json.toString();
	}
	
	/**
	 * 账号登录
	 * @param account
	 * @param password
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String login(
			@RequestParam(value = "account", defaultValue = "") String account,
			@RequestParam(value = "password", defaultValue = "") String password,
			HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();
		
		if (!StringUtils.isEmpty(account) && !StringUtils.isEmpty(password)) {

			
			User user = userService.findUser(account, password);
			
			if (user != null) {
				json.put("status", 200);
				if (!StringUtils.isEmpty(user.getUsername())) {
					
					json.put("username", user.getUsername());
				} else {
					
					json.put("username", user.getAccount());
				}
				
				json.put("userID", user.getUserID());
				json.put("userType", user.getType());
				json.put("job", UserTypeUtils.findUserJob(user.getType()));
			} else {
				json.put("status", -1);
				json.put("reason", "账号或者密码错误，请重新登录");
			}
			
		} else {
			json.put("status", -1);
			json.put("reason", "账号或者密码错误，请重新登录");
		}

		return json.toString();
	}
	
	/**
	 * 根据状态返回用户列表
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/findstatususer", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findUserByStatus(
			@RequestParam(value = "status", defaultValue = "") String status) {

		JSONObject json = new JSONObject();
		
		if (!StringUtils.isEmpty(status)) {

			json.put("status", 200);
			
			List<User> userList = userService.findUserByStatus(Integer.parseInt(status));
			
			json.put("userList", userList);
			
		} else {
			
			json.put("status", -1);
			json.put("reason", "状态为空");
		}

		return json.toString();
	}
	
	/**
	 * 更新用户信息
	 * @param userID
	 * @param password
	 * @param username
	 * @param telephone
	 * @param IDcard
	 * @param status
	 * @param userType
	 * @return
	 */
	@RequestMapping(value = "/updateuser", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateUser(
			@RequestParam(value = "userID", defaultValue = "") String userID,
			@RequestParam(value = "password", defaultValue = "") String password,
			@RequestParam(value = "username", defaultValue = "") String username,
			@RequestParam(value = "telephone", defaultValue = "") String telephone,
			@RequestParam(value = "IDcard", defaultValue = "") String IDcard,
			@RequestParam(value = "status", defaultValue = "") String status,
			@RequestParam(value = "userType", defaultValue = "") String userType) {

		JSONObject json = new JSONObject();
		
		if (!StringUtils.isEmpty(userID)) {

			json.put("status", 200);
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("userID", userID);
			
			if (!StringUtils.isEmpty(password)) {
				map.put("password", password);
			}
			
			if (!StringUtils.isEmpty(username)) {
				map.put("username", username);
			}
			
			if (!StringUtils.isEmpty(telephone)) {
				map.put("telephone", telephone);
			}
			
			if (!StringUtils.isEmpty(IDcard)) {
				map.put("IDcard", IDcard);
			}
			
			if (!StringUtils.isEmpty(status)) {
				map.put("status", status);
			}
			
			if (!StringUtils.isEmpty(userType)) {
				
				String type = UserTypeUtils.parseJob(userType);
				
				map.put("userType", type);
			}
			
			userService.updateUser(map);
			
		} else {
			
			json.put("status", -1);
		}

		return json.toString();
	}

	/**
	 * 获得所有用户列表
	 * @return
	 */
	@RequestMapping(value = "/getuserlist", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findAllUser() {

		JSONObject json = new JSONObject();

		List<User> userList = userService.findAllUser();
		List<User> resUserList = new ArrayList<>();
		for (User user : userList) {
			user.setJob(UserTypeUtils.findUserJob(user.getType()));
			resUserList.add(user);
		}
		
		json.put("status", 200);
		json.put("userList", resUserList);
		return json.toString();

	
	}
	
}
