package com.logistics.dao;

import java.util.List;

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
}
