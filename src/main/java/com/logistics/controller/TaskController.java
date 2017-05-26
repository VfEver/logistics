package com.logistics.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.logistics.entity.Task;
import com.logistics.service.TaskService;
import com.logistics.utils.DateUtils;
import com.logistics.utils.StringUtils;

@Controller
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private TaskService taskService;

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
	 * @return
	 */
	@RequestMapping(value = "/savetask", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveTask(
			@RequestParam(value = "sendName", defaultValue = "") String sendName,
			@RequestParam(value = "sendTelephone", defaultValue = "") String sendTelephone,
			@RequestParam(value = "sendAddress", defaultValue = "") String sendAddress,
			@RequestParam(value = "receiveName", defaultValue = "") String receiveName,
			@RequestParam(value = "receiveTelephone", defaultValue = "") String receiveTelephone,
			@RequestParam(value = "receiveAddress", defaultValue = "") String receiveAddress,
			@RequestParam(value = "goodsType", defaultValue = "") String goodsType,
			@RequestParam(value = "weight", defaultValue = "") String weight,
			@RequestParam(value = "carID", defaultValue = "") String carID,
			@RequestParam(value = "driverName", defaultValue = "") String driverName,
			@RequestParam(value = "driverID", defaultValue = "") String driverID,
			@RequestParam(value = "realCost", defaultValue = "") String realCost,
			@RequestParam(value = "transType", defaultValue = "") String transType) {

		JSONObject json = new JSONObject();
		
		taskService.saveTask(sendName, sendTelephone, sendAddress, receiveName, receiveTelephone, 
				receiveAddress, goodsType, weight, carID, driverName, driverID, realCost, Integer.parseInt(transType));
		
		json.put("status", 200);

		return json.toString();
	}
	
	/**
	 * 按照状态查询任务单
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/findstatustask", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findStatusTask(
			@RequestParam(value = "status", defaultValue = "") String status) {

		JSONObject json = new JSONObject();
		
		if (!StringUtils.isEmpty(status)) {

			json.put("status", 200);
			
			List<Task> taskList = taskService.findSettlelessTask(Integer.parseInt(status));
			json.put("taskList", taskList);
			
		} else {
			json.put("status", -1);
		}
		
		return json.toString();
	}

	/**
	 * 更新任务单的信息
	 * @param taskID
	 * @param miles
	 * @param oil
	 * @param transCost
	 * @return
	 */
	@RequestMapping(value = "/updatetask", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateTask(
			@RequestParam(value = "taskID", defaultValue = "") String taskID,
			@RequestParam(value = "miles", defaultValue = "") String miles,
			@RequestParam(value = "oil", defaultValue = "") String oil,
			@RequestParam(value = "transCost", defaultValue = "") String transCost) {

		JSONObject json = new JSONObject();
		
		if (!StringUtils.isEmpty(taskID)) {

			json.put("status", 200);
			
			taskService.updateTask(taskID, oil, miles, transCost);
		} else {
			json.put("status", -1);
		}
		
		return json.toString();
	}
	
	/**
	 * 组织数据
	 * @return
	 */
	@RequestMapping(value = "/getchartdata", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findChartData() {

		JSONObject json = new JSONObject();
		
		List<Task> taskList = taskService.findAllTask();
		
		//自有折线图
		List<String> incomeX = new ArrayList<>();
		List<Double> incomeData = new ArrayList<>();
		
		//收入饼图比例
		JSONArray incomeRatio = new JSONArray();
		
		Map<String, Double> incomeMap = new HashMap<>();
		
		for (Task task : taskList) {
		
			if (task.getTransType() == 0) {
				incomeX.add(DateUtils.parseIntoString(task.getEndTime()));
				incomeData.add(task.getRealCost()-task.getTransCost());
				if (incomeMap.containsKey("自有")) {
					incomeMap.put("自有", incomeMap.get("自有") + task.getRealCost()-task.getTransCost());
				} else {
					incomeMap.put("自有", task.getRealCost()-task.getTransCost());
				}
			} else {
				incomeX.add(DateUtils.parseIntoString(task.getEndTime()));
				incomeData.add(task.getTransCost());
				
				if (incomeMap.containsKey(task.getDriverName())) {
					incomeMap.put(task.getDriverName(), incomeMap.get(task.getDriverName()) + task.getRealCost()-task.getTransCost());
				} else {
					incomeMap.put(task.getDriverName(), task.getRealCost()-task.getTransCost());
				}
			}
			
		}
		
		for (Map.Entry<String, Double> entry : incomeMap.entrySet()) {
			
			JSONObject temp = new JSONObject();
			temp.put("name", entry.getKey());
			temp.put("value", entry.getValue());
			incomeRatio.add(temp);
		}
		
		json.put("incomeX", incomeX);
		json.put("incomeData", incomeData);
		json.put("incomeRatio", incomeRatio);
		
		return json.toString();
	}
	
	/**
	 * 根据单号查询task
	 * @param taskID
	 * @return
	 */
	@RequestMapping(value = "/findtaskbyid", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findTaskByID(
			@RequestParam(value = "taskID", defaultValue = "") String taskID) {

		JSONObject json = new JSONObject();
		
		if (!StringUtils.isEmpty(taskID)) {

			json.put("status", 200);
			Task task = taskService.findTaskByID(Integer.parseInt(taskID));
			if (task != null) {
				task.setbTime(DateUtils.parseIntoString(task.getBeginTime()));
				task.seteTime(DateUtils.parseIntoString(task.getEndTime()));
				
				json.put("task", task);
			} else {
				json.put("info", "没有此任务单");
			}
		} else {
			json.put("status", -1);
		}
		
		return json.toString();
	}
	
	/**
	 * 查询出所有的task
	 * @return
	 */
	@RequestMapping(value = "/tasklist", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findAllTask() {

		JSONObject json = new JSONObject();
		
		List<Task> taskList = taskService.findAllTask();
		
		List<Task> result = new ArrayList<>();
		for (Task task : taskList) {
			task.setbTime(DateUtils.parseIntoString(task.getBeginTime()));
			task.seteTime(DateUtils.parseIntoString(task.getEndTime()));
			result.add(task);
		}
		
		json.put("status", 200);
		json.put("taskList", result);
		return json.toString();
	}
	
	/**
	 * 查询类型任务单并组织数据
	 * @return
	 */
	@RequestMapping(value = "/drivercolumndata", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findTypeTask() {

		JSONObject json = new JSONObject();
		
		List<Task> taskList = taskService.findAllTaskByType("0");
		Map<String, Double> driverMiles = new HashMap<>(); 
		Map<String, Double> driverOil = new HashMap<>(); 
		Map<String, Double> driverRealCost = new HashMap<>(); 
		Map<String, Double> driverTransCost = new HashMap<>(); 
		
		for (Task task : taskList) {
			
			String driverName = task.getDriverName();
			if (driverMiles.containsKey(driverName)) {
				
				double tempMiles = driverMiles.get(driverName) + task.getMiles();
				driverMiles.put(driverName, tempMiles);
			} else {
				driverMiles.put(driverName, task.getMiles());
			}
			
			if (driverOil.containsKey(driverName)) {
				
				double tempOil = driverOil.get(driverName) + task.getOil();
				driverOil.put(driverName, tempOil);
			} else {
				
				driverOil.put(driverName, task.getOil());
			}
			
			if (driverRealCost.containsKey(driverName)) {
				
				double tempCost = driverRealCost.get(driverName) + task.getRealCost();
				driverRealCost.put(driverName, tempCost);
			} else {
				
				driverRealCost.put(driverName, task.getRealCost());
			}
			
			if (driverTransCost.containsKey(driverName)) {
				
				double tempCost = driverTransCost.get(driverName) + task.getTransCost();
				driverTransCost.put(driverName, tempCost);
			} else {
				
				driverTransCost.put(driverName, task.getTransCost());
			}
			
		}
		
		JSONArray res = new JSONArray();
		JSONArray xName = new JSONArray();
		JSONArray milesArr = new JSONArray();
		JSONArray oilArr = new JSONArray();
		JSONArray returnRateArr = new JSONArray();
		
		for (Map.Entry<String, Double> entry : driverMiles.entrySet()) {
			
			String driverName = entry.getKey();
			xName.add(driverName);
			milesArr.add(entry.getValue());
		}
		
		for (int i = 0; i != xName.size(); ++i) {
			String driverName = xName.getString(i);
			oilArr.add(driverOil.get(driverName));
			returnRateArr.add((driverRealCost.get(driverName)-driverTransCost.get(driverName)) / driverRealCost.get(driverName));
		}
		
		JSONObject t1 = new JSONObject();
		t1.put("name", "公里数km");
		t1.put("data", milesArr);
		
		res.add(t1);

		JSONObject t2 = new JSONObject();
		t2.put("name", "油耗L");
		t2.put("data", oilArr);
		
		res.add(t2);
		
		JSONObject t3 = new JSONObject();
		t3.put("name", "回报率%");
		t3.put("data", returnRateArr);
		
		res.add(t3);
		
		json.put("status", 200);
		json.put("series", res);
		json.put("xName", xName);
		return json.toString();
	}
	
	/**
	 * 查询车辆的数据并组织
	 * @return
	 */
	@RequestMapping(value = "/carcolumndata", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findCarData() {

		JSONObject json = new JSONObject();
		
		List<Task> taskList = taskService.findAllTaskByType("0");
		Map<Integer, Double> carMiles = new HashMap<>(); 
		Map<Integer, Double> carOil = new HashMap<>(); 
		Map<Integer, Double> carRealCost = new HashMap<>(); 
		Map<Integer, Double> carTransCost = new HashMap<>(); 
		
		for (Task task : taskList) {
			
			int carID = task.getCarID();
			if (carMiles.containsKey(carID)) {
				
				double tempMiles = carMiles.get(carID) + task.getMiles();
				carMiles.put(carID, tempMiles);
			} else {
				carMiles.put(carID, task.getMiles());
			}
			
			if (carOil.containsKey(carID)) {
				
				double tempOil = carOil.get(carID) + task.getOil();
				carOil.put(carID, tempOil);
			} else {
				
				carOil.put(carID, task.getOil());
			}
			
			if (carRealCost.containsKey(carID)) {
				
				double tempCost = carRealCost.get(carID) + task.getRealCost();
				carRealCost.put(carID, tempCost);
			} else {
				
				carRealCost.put(carID, task.getRealCost());
			}
			
			if (carTransCost.containsKey(carID)) {
				
				double tempCost = carTransCost.get(carID) + task.getTransCost();
				carTransCost.put(carID, tempCost);
			} else {
				
				carTransCost.put(carID, task.getTransCost());
			}
			
		}
		
		JSONArray res = new JSONArray();
		JSONArray xName = new JSONArray();
		JSONArray milesArr = new JSONArray();
		JSONArray oilArr = new JSONArray();
		JSONArray returnRateArr = new JSONArray();
		JSONArray trueXName = new JSONArray();
		
		for (Map.Entry<Integer, Double> entry : carMiles.entrySet()) {
			
			int carID = entry.getKey();
			xName.add(carID);
			milesArr.add(entry.getValue());
		}
		
		for (int i = 0; i != xName.size(); ++i) {
			int carID = xName.getInt(i);
			trueXName.add(taskService.findCarNumberByID(carID));
			oilArr.add(carOil.get(carID));
			returnRateArr.add((carRealCost.get(carID)-carTransCost.get(carID)) / carRealCost.get(carID));
		}
		
		JSONObject t1 = new JSONObject();
		t1.put("name", "公里数km");
		t1.put("data", milesArr);
		
		res.add(t1);

		JSONObject t2 = new JSONObject();
		t2.put("name", "油耗L");
		t2.put("data", oilArr);
		
		res.add(t2);
		
		JSONObject t3 = new JSONObject();
		t3.put("name", "回报率%");
		t3.put("data", returnRateArr);
		
		res.add(t3);
		
		json.put("status", 200);
		json.put("series", res);
		json.put("xName", trueXName);
		return json.toString();
	}
	
	/**
	 * 组织类型数据
	 * @return
	 */
	@RequestMapping(value = "/typecolumndata", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findTypeData() {

		JSONObject json = new JSONObject();
		
		List<Map<String, String>> dataList = taskService.findCarTypeData();
		
		JSONArray res = new JSONArray();
		JSONArray xName = new JSONArray();
		JSONArray milesArr = new JSONArray();
		JSONArray oilArr = new JSONArray();
		JSONArray returnRateArr = new JSONArray();
		for (Map<String, String> map : dataList) {
		
			xName.add(map.get("carType"));
			milesArr.add(map.get("miles"));
			oilArr.add(map.get("oil"));
			
			Object o = map.get("realCost");
			Object o1 = map.get("transCost");
			returnRateArr.add(((double)o-(double)o1)/(double)o);
			
		}
		
		
		JSONObject t1 = new JSONObject();
		t1.put("name", "公里数km");
		t1.put("data", milesArr);
		
		res.add(t1);

		JSONObject t2 = new JSONObject();
		t2.put("name", "油耗L");
		t2.put("data", oilArr);
		
		res.add(t2);
		
		JSONObject t3 = new JSONObject();
		t3.put("name", "回报率%");
		t3.put("data", returnRateArr);
		
		res.add(t3);
		
		json.put("status", 200);
		json.put("series", res);
		json.put("xName", xName);
		return json.toString();
	}
}
