package com.logistics.service;

import java.util.List;

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
		carrier.setCarrierTelephhone(carrierTelephone);
		
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
}
