package com.logistics.controller;

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

import com.logistics.entity.Car;
import com.logistics.service.CarService;
import com.logistics.utils.StringUtils;

@Controller
@RequestMapping("/car")
public class CarController {

	@Autowired
	private CarService carService;

	/**
	 * 保存车辆信息
	 * @param carNumber
	 * @param carType
	 * @param capacity
	 * @param belong
	 * @param carrierID
	 * @param carrierName
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addcar", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addCar(
			@RequestParam(value = "carNumber", defaultValue = "") String carNumber,
			@RequestParam(value = "carType", defaultValue = "") String carType,
			@RequestParam(value = "capacity", defaultValue = "") String capacity,
			@RequestParam(value = "belong", defaultValue = "") String belong,
			@RequestParam(value = "carrierID", defaultValue = "") String carrierID,
			@RequestParam(value = "carrierName", defaultValue = "") String carrierName,
			HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();
		
		if (!StringUtils.isEmpty(carNumber)) {

			json.put("status", 200);
			
			if ("自有".equals(belong)) {

				carService.saveCar(carType, carNumber, Double.parseDouble(capacity), -1, "自有", belong);
			} else {
				
				carService.saveCar(carType, carNumber, Double.parseDouble(capacity), Integer.parseInt(carrierID), carrierName, belong);
			}
			
		} else {
			json.put("status", -1);
		}

		return json.toString();
	}
	
	/**
	 * 根据ID删除车辆
	 * @param carID
	 * @return
	 */
	@RequestMapping(value = "/deletecar", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteCar(
			@RequestParam(value = "carID", defaultValue = "") String carID) {

		JSONObject json = new JSONObject();
		
		if (!StringUtils.isEmpty(carID)) {
			
			json.put("status", 200);
			carService.deleteCarByID(carID);
		} else {
			json.put("status", -1);
		}

		return json.toString();
	}
	
	/**
	 * 查询所有车辆信息并返回
	 * @return
	 */
	@RequestMapping(value = "/getcarlist", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findAllCar() {

		JSONObject json = new JSONObject();

		List<Car> carList = carService.findAllCar();
			
		json.put("status", 200);
		json.put("carList", carList);
		
		return json.toString();
	}
	
	/**
	 * 根据车辆状态信息查询
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/getfreecar", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findFreeCar(
			@RequestParam(value = "status", defaultValue = "") String status) {

		JSONObject json = new JSONObject();

		if (!StringUtils.isEmpty(status)) {
			
			json.put("status", 200);
			
			List<Car> carList = carService.findCarByStatus(Integer.parseInt(status));
			json.put("carList", carList);
			
		} else {
			
			json.put("status", -1);
		}
		
		return json.toString();
	}
	
	/**
	 * 更新车辆状态信息
	 * @param carID
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/updatecar", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateCar(
			@RequestParam(value = "carID", defaultValue = "") String carID,
			@RequestParam(value = "status", defaultValue = "") String status) {

		JSONObject json = new JSONObject();

		if (!StringUtils.isEmpty(status)) {
			
			json.put("status", 200);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("carID", carID);
			map.put("status", status);
			
			carService.updateCarStatus(map);
			
		} else {
			
			json.put("status", -1);
		}
		
		return json.toString();
	}


	/**
	 * 更新车辆的具体信息
	 * @param carID
	 * @param carType
	 * @param weight
	 * @return
	 */
	@RequestMapping(value = "/updatecarinfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateCarInfo(
			@RequestParam(value = "carID", defaultValue = "") String carID,
			@RequestParam(value = "carType", defaultValue = "") String carType,
			@RequestParam(value = "weight", defaultValue = "") String weight) {

		JSONObject json = new JSONObject();

		if (!StringUtils.isEmpty(carID)) {
			
			json.put("status", 200);
			
			Map<String, String> map = new HashMap<String, String>();
			
			if (!StringUtils.isEmpty(carType)) {
				
				map.put("carType", carType);
			}
			
			if (!StringUtils.isEmpty(weight)) {
				
				map.put("weight", weight);
			}
			map.put("carID", carID);
			
			carService.updateCarInfo(map);
			
		} else {
			
			json.put("status", -1);
		}
		
		return json.toString();
	}
}
