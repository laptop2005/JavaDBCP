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
			
			//Ŀ�ؼ����丮 ����
			ConnectionFactory connFactory = new DriverManagerConnectionFactory(DB_URL, DB_USER, DB_PASSWORD);
			
			//DBCP�� Ŀ�ؼ� Ǯ�� Ŀ�ؼ��� �����Ҷ� ����ϴ� PoolableConnectionFactory ����
			PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connFactory, null);
			
			//Ŀ�ؼ��� ��ȿ���� Ȯ���Ҷ� ��� �ϴ� ����
			poolableConnFactory.setValidationQuery("select 1");
			
			//Ŀ�ؼ� Ǯ�� ���� ���� ����
			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			
			//���� Ŀ�ؼ� �˻��ֱ�
			poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 1L);
			
			//Ǯ�� �ִ� Ŀ�ؼ��� ��ȿ���� �˻� ���� ����
			poolConfig.setTestWhileIdle(true);
			
			//Ŀ�ؼ� �ּҰ��� ����
			poolConfig.setMinIdle(4);
			
			//Ŀ�ؼ� �ִ� ���� ����
			poolConfig.setMaxTotal(50);
			
			//Ŀ�ؼ� Ǯ ����
			GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<PoolableConnection>(poolableConnFactory,poolConfig);
			
			//poolableConnectionFactory���� Ŀ�ؼ� Ǯ�� ����
			poolableConnFactory.setPool(connectionPool);
			
			//Ŀ�ؼ� Ǯ�� �����ϴ� jdbc ����̹��� ���
			Class.forName("org.apache.commons.dbcp2.PoolingDriver");
			
			PoolingDriver driver = (PoolingDriver)DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			
			//������ Ŀ�ؼ� Ǯ ����̹��� ������ Ŀ�ؼ� Ǯ�� ����Ѵ�
			driver.registerPool("cp", connectionPool);
		
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
			
		}
	}
}
