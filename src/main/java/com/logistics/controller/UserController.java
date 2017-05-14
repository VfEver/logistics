package com.logistics.controller;

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

			json.put("status", 200);
			
			User user = userService.findUser(account, password);
			
			if (user != null) {
				if (!StringUtils.isEmpty(user.getUsername())) {
					
					json.put("username", user.getUsername());
				} else {
					
					json.put("username", user.getAccount());
				}
				
				json.put("userID", user.getUserID());
				json.put("userType", user.getType());
				json.put("job", UserTypeUtils.findUserJob(user.getType()));
			} else {
				json.put("reason", "账号或者密码错误，请重新登录");
			}
			
		} else {
			json.put("status", -1);
			json.put("reason", "账号或者密码错误，请重新登录");
		}

		return json.toString();
	}

}
