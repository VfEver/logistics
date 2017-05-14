package com.logistics.utils;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * mybatis utils class
 * @author zys
 *
 */
public class MybatisUtils {

	/**
	 * 获取SqlSesion
	 * @param sqlSesssionFactory
	 * @return
	 */
	public static SqlSession getSqlSession(SqlSessionFactory sqlSesssionFactory) {
		
		if (sqlSesssionFactory != null) {
			return sqlSesssionFactory.openSession();
		} else {
			return null;
		}
	}
	
	/**
	 * 关闭SqlSession
	 * @param sqlSession
	 */
	public static void closeSqlSession(SqlSession sqlSession) {
		if (sqlSession != null) {
			sqlSession.close();
		}
	}
}
