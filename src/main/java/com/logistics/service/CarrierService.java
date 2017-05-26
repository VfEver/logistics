package com.logistics.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.logistics.dao.CarrierMapper;
import com.logistics.entity.Carrier;
import com.logistics.utils.MybatisUtils;

/**
 * 承运商 服务层
 * @author zys
 *
 */
public class CarrierService {
	
	private SqlSessionFactory sqlSessionFactory;

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	/**
	 * 保存承运商信息
	 * @param carrierName
	 * @param carrierAddress
	 * @param carrierTelephone
	 */
	public void saveCarrier(String carrierName, String carrierAddress, String carrierTelephone) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		CarrierMapper carrierMapper = sqlSession.getMapper(CarrierMapper.class);
		
		Carrier carrier = new Carrier();
		carrier.setCarrierName(carrierName);
		carrier.setCarrierAddress(carrierAddress);
		carrier.setCarrierTelephone(carrierTelephone);
		
		carrierMapper.saveCarrier(carrier);
		sqlSession.commit();
		
		MybatisUtils.closeSqlSession(sqlSession);
		
	}
	
	/**
	 * 删除承运商信息
	 * @param carrierID
	 */
	public void deleteCarrierByID(int carrierID) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		CarrierMapper carrierMapper = sqlSession.getMapper(CarrierMapper.class);
		
		carrierMapper.deleteCarrierByID(carrierID);
		sqlSession.commit();
		
		MybatisUtils.closeSqlSession(sqlSession);
	}
	
	/**
	 * 查询所有承运商信息
	 * @return
	 */
	public List<Carrier> findAllCarrier() {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		CarrierMapper carrierMapper = sqlSession.getMapper(CarrierMapper.class);
		
		List<Carrier> carrierList = carrierMapper.findAllCarrier();
		
		return carrierList;
	}
	
	/**
	 * 更新承运商信息
	 * @param map
	 */
	public void updateCarrierInfo(Map<String, String> map) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		CarrierMapper carrierMapper = sqlSession.getMapper(CarrierMapper.class);
		
		carrierMapper.updateCarrierInfo(map);
		
		sqlSession.commit();
		
		MybatisUtils.closeSqlSession(sqlSession);
	}
	
	/**
	 * 更新车辆信息中承运商名字
	 * @param map
	 */
	public void updateCarCarrierName(Map<String, String> map) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		CarrierMapper carrierMapper = sqlSession.getMapper(CarrierMapper.class);
		
		carrierMapper.updateCarCarrierName(map);
		
		sqlSession.commit();
		
		MybatisUtils.closeSqlSession(sqlSession);
		
	}
	
	/**
	 * 更新任务单中运营商名字
	 * @param map
	 */
	public void updateTaskCarrierName(Map<String, String>map) {
		
		SqlSession sqlSession = MybatisUtils.getSqlSession(sqlSessionFactory);
		CarrierMapper carrierMapper = sqlSession.getMapper(CarrierMapper.class);
		
		carrierMapper.updateTaskCarrierName(map);
		
		sqlSession.commit();
		
		MybatisUtils.closeSqlSession(sqlSession);
		
	}
}
