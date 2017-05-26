package com.logistics.dao;

import java.util.List;
import java.util.Map;

import com.logistics.entity.Car;

/**
 * car mapper interface
 * @author zys
 *
 */
public interface CarMapper {
	
	public void saveCar(Car car);
	
	public void deleteCarByID(int carID);
	
	public List<Car> findAllCar();
	
	public List<Car> findCarByStatus(int status);
	
	public void updateCarStatus(Map<String, String> map);
	
	public void updateCarInfo(Map<String, String> map);
	
}
