package com.logistics.dao;

import java.util.List;

import com.logistics.entity.Carrier;

public interface CarrierMapper {
	
	public void saveCarrier(Carrier carrier);
	
	public void deleteCarrierByID(int carrierID);
	
	public List<Carrier> findAllCarrier();
}
