package com.logistics.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.logistics.dao.CarMapper;
import com.logistics.entity.Car;
import com.logistics.utils.MybatisUtils;

/**
 * 承运商 服务层
 * @author zys
 *
 */
public class CarService {
	
	private SqlSessionFactory sqlSessionFactory;

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	/**
	 * 保存车辆信息
	 * @param carType
	 * @param carNumber
	 * @param capacity
	 * @param carrierID
	 * @param carrierName
	 * @param belongType
	 */
	public void saveCar(String carType, String carNumber, double capacity, int carrierID, String carrierName, String belongType) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		CarMapper carMapper = sqlSession.getMapper(CarMapper.class);
		
		Car car = new Car();
		car.setBelongType(belongType);
		car.setCapacity(capacity);
		car.setCarNumber(carNumber);
		car.setCarrierName(carrierName);
		car.setCarrierID(carrierID);
		car.setCarType(carType);
		
		carMapper.saveCar(car);
		sqlSession.commit();
		
		MybatisUtils.closeSqlSession(sqlSession);
		
	}
	
	/**
	 * 删除指定车辆信息
	 * @param carID
	 */
	public void deleteCarByID(String carID) {

		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		CarMapper carMapper = sqlSession.getMapper(CarMapper.class);
		
		carMapper.deleteCarByID(Integer.parseInt(carID));
		sqlSession.commit();
		
		MybatisUtils.closeSqlSession(sqlSession);
		
	}
	
	/**
	 * 查询所有车辆信息
	 * @return
	 */
	public List<Car> findAllCar() {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		CarMapper carMapper = sqlSession.getMapper(CarMapper.class);
		
		List<Car> carList = carMapper.findAllCar();
		
		MybatisUtils.closeSqlSession(sqlSession);
		
		return carList;
	}
	
}
