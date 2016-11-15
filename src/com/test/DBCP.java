package com.test;

import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class DBCP extends HttpServlet
{
	
	@Override
	public void init() throws ServletException{
		initConnectionPool();
	}
	
	public void initConnectionPool(){
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		
			String DB_URL = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
			String DB_USER = "system";
			String DB_PASSWORD = "system";
			
			//커넥션팩토리 생성
			ConnectionFactory connFactory = new DriverManagerConnectionFactory(DB_URL, DB_USER, DB_PASSWORD);
			
			//DBCP가 커넥션 풀에 커넥션을 보관할때 사용하는 PoolableConnectionFactory 생성
			PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connFactory, null);
			
			//커넥션이 유효한지 확인할때 사용 하는 쿼리
			poolableConnFactory.setValidationQuery("select 1");
			
			//커넥션 풀의 설정 정보 생성
			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			
			//유휴 커넥션 검사주기
			poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 1L);
			
			//풀에 있는 커넥션이 유효한지 검사 유무 설정
			poolConfig.setTestWhileIdle(true);
			
			//커넥션 최소갯수 설정
			poolConfig.setMinIdle(4);
			
			//커넥션 최대 갯수 설정
			poolConfig.setMaxTotal(50);
			
			//커넥션 풀 생성
			GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<PoolableConnection>(poolableConnFactory,poolConfig);
			
			//poolableConnectionFactory에도 커넥션 풀을 연결
			poolableConnFactory.setPool(connectionPool);
			
			//커넥션 풀을 제공하는 jdbc 드라이버를 등록
			Class.forName("org.apache.commons.dbcp2.PoolingDriver");
			
			PoolingDriver driver = (PoolingDriver)DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			
			//위에서 커넥션 풀 드라이버에 생성한 커넥션 풀을 등록한다
			driver.registerPool("cp", connectionPool);
		
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
			
		}
	}
}
