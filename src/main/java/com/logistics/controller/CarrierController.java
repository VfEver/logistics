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

import com.logistics.entity.Carrier;
import com.logistics.service.CarrierService;
import com.logistics.utils.StringUtils;

@Controller
@RequestMapping("/carrier")
public class CarrierController {

	@Autowired
	private CarrierService carrierService;

	/**
	 * 保存承运商信息
	 * @param carrierName
	 * @param carrierTelephone
	 * @param carrierAddress
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addcarrier", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String register(
			@RequestParam(value = "carrierName", defaultValue = "") String carrierName,
			@RequestParam(value = "carrierTelephone", defaultValue = "") String carrierTelephone,
			@RequestParam(value = "carrierAddress", defaultValue = "") String carrierAddress,
			HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();
		
		if (!StringUtils.isEmpty(carrierName)) {

			json.put("status", 200);
			
			carrierService.saveCarrier(carrierName, carrierAddress, carrierTelephone);
			
		} else {
			json.put("status", -1);
		}

		return json.toString();
	}
	
	/**
	 * 查询所有承运商
	 * @return
	 */
	@RequestMapping(value = "/getcarrierlist", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findAllCarrier() {

		JSONObject json = new JSONObject();
		
		List<Carrier> carrierList = carrierService.findAllCarrier();
		
		json.put("status", 200);
		json.put("carrierList", carrierList);
		
		return json.toString();
	}
	
	/**
	 * 查询所有承运商
	 * @return
	 */
	@RequestMapping(value = "/deletecarrier", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteCarrier(@RequestParam(value = "carrierID", defaultValue = "") String carrierID) {

		JSONObject json = new JSONObject();
		if (!StringUtils.isEmpty(carrierID)) {
			
			json.put("status", 200);
			carrierService.deleteCarrierByID(Integer.parseInt(carrierID));
		} else {
			json.put("status", -1);
		}
		
		
		return json.toString();
	}
	
	/**
	 * 更新承运商信息
	 * @param carrierID
	 * @param carrierName
	 * @param carrierTelephone
	 * @return
	 */
	@RequestMapping(value = "/updatecarrier", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateCarrier(@RequestParam(value = "carrierID", defaultValue = "") String carrierID, 
								@RequestParam(value = "carrierName", defaultValue = "") String carrierName, 
								@RequestParam(value = "carrierTelephone", defaultValue = "") String carrierTelephone ) {

		JSONObject json = new JSONObject();
		if (!StringUtils.isEmpty(carrierID)) {
			
			json.put("status", 200);

			Map<String, String> map = new HashMap<>();
			map.put("carrierID", carrierID);

			if (!StringUtils.isEmpty(carrierName)) {
				map.put("carrierName", carrierName);
				carrierService.updateCarCarrierName(map);
				carrierService.updateTaskCarrierName(map);
			}
			
			if (!StringUtils.isEmpty(carrierTelephone)) {
				map.put("carrierTelephone", carrierTelephone);
			}
			
			
			carrierService.updateCarrierInfo(map);
			
		} else {
			json.put("status", -1);
		}
		
		
		return json.toString();
	}
}
