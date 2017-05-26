package com.logistics.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.logistics.dao.TaskMapper;
import com.logistics.entity.Task;
import com.logistics.utils.DateUtils;
import com.logistics.utils.MybatisUtils;
import com.logistics.utils.StringUtils;

/**
 * 任务单 服务层
 * @author zys
 *
 */
public class TaskService {
	
	private SqlSessionFactory sqlSessionFactory;

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	/**
	 * 保存任务单信息
	 * @param sendName
	 * @param sendTelephone
	 * @param sendAddress
	 * @param receiveName
	 * @param receiveTelephone
	 * @param receiveAddress
	 * @param goodsType
	 * @param weight
	 * @param carID
	 * @param driverName
	 * @param driverID
	 * @param realCost
	 */
	public void saveTask(String sendName, String sendTelephone, String sendAddress, 
			String receiveName, String receiveTelephone, String receiveAddress, 
			String goodsType, String weight, String carID, String driverName,
			String driverID, String realCost, int transType) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		TaskMapper taskMapper = sqlSession.getMapper(TaskMapper.class);
		
		Task task = new Task();
		task.setBeginTime(new Date());
		task.setCarID(Integer.parseInt(carID));
		task.setDriverID(Integer.parseInt(driverID));
		task.setDriverName(driverName);
		task.setGoodsType(goodsType);
		task.setRealCost(Double.parseDouble(realCost));
		task.setReceiveAddress(receiveAddress);
		task.setReceiveName(receiveName);
		task.setReceiveTelephone(receiveTelephone);
		task.setSendName(sendName);
		task.setSendAddress(sendAddress);
		task.setSendTelephone(sendTelephone);
		task.setWeight(Double.parseDouble(weight));
		task.setStatus(0);
		task.setTransType(transType);
		
		taskMapper.saveTask(task);
		sqlSession.commit();
		
		MybatisUtils.closeSqlSession(sqlSession);
		
	}

	/**
	 * 查询所有状态任务单
	 * @param status
	 * @return
	 */
	public List<Task> findSettlelessTask(int status) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		TaskMapper taskMapper = sqlSession.getMapper(TaskMapper.class);
		
		List<Task> taskList = taskMapper.findSettlelessTask(status);
		List<Task> resTaskList = new ArrayList<>();
		for (Task task : taskList) {
			task.setbTime(DateUtils.parseIntoString(task.getBeginTime()));
			resTaskList.add(task);
		}
		
		MybatisUtils.closeSqlSession(sqlSession);
		
		return resTaskList;
	}
	
	/**
	 * 更新任务单信息
	 * @param taskID
	 * @param oil
	 * @param miles
	 * @param transCost
	 */
	public void updateTask(String taskID, String oil, String miles, String transCost) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		TaskMapper taskMapper = sqlSession.getMapper(TaskMapper.class);
		
		Map<String, String> map = new HashMap<>();
		
		if (!StringUtils.isEmpty(oil)) {
			map.put("oil", oil);
		}
		
		if (!StringUtils.isEmpty(miles)) {
			map.put("miles", miles);
		}
		
		if (!StringUtils.isEmpty(transCost)) {
			map.put("transCost", transCost);
		}
		
		map.put("endTime", DateUtils.parseIntoString(new Date()));
		map.put("taskID", taskID);
		
		taskMapper.updateTask(map);
		
		sqlSession.commit();
		
		MybatisUtils.closeSqlSession(sqlSession);
	}
	
	/**
	 * 查询所有任务单
	 * @return
	 */
	public List<Task> findAllTask() {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		TaskMapper taskMapper = sqlSession.getMapper(TaskMapper.class);
		
		List<Task> taskList = taskMapper.findAllTask();
		
		MybatisUtils.closeSqlSession(sqlSession);
		
		return taskList;
	}
	
	/**
	 * 根据id查询task
	 * @param taskID
	 * @return
	 */
	public Task findTaskByID(int taskID) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		TaskMapper taskMapper = sqlSession.getMapper(TaskMapper.class);
		
		Task task = taskMapper.findTaskByID(taskID);
		MybatisUtils.closeSqlSession(sqlSession);
		return task;
	}
	
	/**
	 * 根据类别查询所有订单
	 * @param type
	 * @return
	 */
	public List<Task> findAllTaskByType(String type) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		TaskMapper taskMapper = sqlSession.getMapper(TaskMapper.class);
		
		List<Task> taskList = taskMapper.findAllTaskByType(type);
		
		MybatisUtils.closeSqlSession(sqlSession);
		return taskList;
	}
	
	/**
	 * 根据car ID查询车牌
	 * @param carID
	 * @return
	 */
	public String findCarNumberByID(int carID) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		TaskMapper taskMapper = sqlSession.getMapper(TaskMapper.class);
		
		String carNumber = taskMapper.findCarNumberByID(carID);
		MybatisUtils.closeSqlSession(sqlSession);
		return carNumber;
	}
	
	/**
	 * 根据car ID查询车辆类型
	 * @param carID
	 * @return
	 */
	public String findCarTypeByID(int carID) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		TaskMapper taskMapper = sqlSession.getMapper(TaskMapper.class);
		
		String carType = taskMapper.findCarTypeByID(carID);
		MybatisUtils.closeSqlSession(sqlSession);
		return carType;
	}
	
	/**
	 * 查询出车辆类型的数据信息
	 * @return
	 */
	public List<Map<String, String>> findCarTypeData() {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		TaskMapper taskMapper = sqlSession.getMapper(TaskMapper.class);
		
		List<Map<String, String>> dataList = taskMapper.findCarTypeData();
		MybatisUtils.closeSqlSession(sqlSession);
		
		return dataList;
	}
	
}
