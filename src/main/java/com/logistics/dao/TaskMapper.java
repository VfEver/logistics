package com.logistics.dao;

import java.util.List;
import java.util.Map;

import com.logistics.entity.Task;

/**
 * 任务单dao层
 * @author zys
 *
 */
public interface TaskMapper {
	
	public void saveTask(Task task);
	
	public void updateTask(Map<String, String> map);
	
	public List<Task> findSettlelessTask(int status);
	
	public List<Task> findAllTask();
	
	public Task findTaskByID(int taskID);
	
	public List<Task> findAllTaskByType(String type);
	
	public String findCarNumberByID(int carID);
	
	public String findCarTypeByID(int carID);
	
	public List<Map<String, String>> findCarTypeData();
}

