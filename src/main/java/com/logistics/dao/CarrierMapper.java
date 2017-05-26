package com.logistics.dao;

import java.util.List;
import java.util.Map;

import com.logistics.entity.Carrier;

public interface CarrierMapper {
	
	public void saveCarrier(Carrier carrier);
	
	public void deleteCarrierByID(int carrierID);
	
	public List<Carrier> findAllCarrier();
	
	public void updateCarrierInfo(Map<String, String> map);
	
	public void updateCarCarrierName(Map<String,String> map);
	
	public void updateTaskCarrierName(Map<String, String>map);
}
