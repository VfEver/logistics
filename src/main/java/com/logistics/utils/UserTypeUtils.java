package com.logistics.utils;
/**
 * 用户类别数字转职业
 * @author zys
 *
 */
public class UserTypeUtils {
	
	public static String findUserJob(int typeID) {
		
		String job = "";
		
		switch(typeID) {
			case 0 : {
				job = "未分配";
				break;
			}
			case 1 : {
				job = "管理员";
				break;
			}
			case 2 : {
				job = "调度员";
				break;
			}
			case 3 : {
				job = "维护员";
				break;
			}
			case 4 : {
				job = "驾驶员";
				break;
			}
			default : {
				job = "未分配";
				break;
			}
		}
		
		return job;
	}
	
	public static String parseJob(String job) {
		
		String userType = "0";
		switch(job) {
			case "未分配" : {
				userType = "0";
				break;
			}
			case "管理员" : {
				userType = "1";
				break;
			}
			case "调度员" : {
				userType = "2";
				break;
			}
			case "维护员" : {
				userType = "3";
				break;
			}
			case "驾驶员" : {
				userType = "4";
				break;
			}
			default : {
				userType = "0";
				break;
			}
		}
		
		return userType;
	}
}
