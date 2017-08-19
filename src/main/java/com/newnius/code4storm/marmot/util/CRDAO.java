package com.newnius.code4storm.marmot.util;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;


/**
 * 
 * provide simple interface for database access
 * 
 * @author Newnius
 * @version 0.1.0(JAVA SE)
 * Dependencies
 * 	com.newnius.util.CRLogger
 *  
 *  Before call getInstance(), you have to call init(Properties) once
 *  example code as follows:
 *  <code>
   		String confFile = "dbcp.properties";
		String configFile = confFile;
		Properties dbProperties = new Properties();
		try {
			dbProperties.load(CRDAO.class.getClassLoader().getResourceAsStream(configFile));
			CRDAO.init(dbProperties);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
 *  </code>
 * 
 * @FIXME query on autoCommit connection will cause result set has been closed exception
 * 
 */
public class CRDAO {
	private static final String TAG = "CRDBManager";
	private static Logger logger;
	private static DataSource dataSource;
	private boolean autoCommit = true;
	private Connection connection;
	private PreparedStatement preparedStatement;

	private CRDAO() {
		connection = getConnection();
	}
	
	public void setAutoCommit(boolean autoCommit){
		this.autoCommit = autoCommit;
	}
	
	public static void init(Properties dbProperties) {
		logger = LoggerFactory.getLogger(TAG);
		try {
			dataSource = BasicDataSourceFactory.createDataSource(dbProperties);
			logger.info("CRDAO initailed.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return conn;
	}

	
	public static CRDAO getInstance() throws Exception{
		if (dataSource == null) {
			throw new Exception("dataSource is not initailed. Please class CRDAO.inint(Properties dbProperties) first");
		}
		return new CRDAO();
	}

	public ResultSet executeQuery(String sql, String[] args) {
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					preparedStatement.setString(i + 1, args[i]);
				}
			}
			logger.debug(preparedStatement.toString());
			return preparedStatement.executeQuery();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if(autoCommit)
				commit();
		}
	}

	public int executeUpdate(String sql, String[] args) {
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					preparedStatement.setString(i + 1, args[i]);
				}
			}
			logger.debug(preparedStatement.toString());
			return preparedStatement.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		} finally {
			if (autoCommit)
				commit();
		}
	}

	public void commit() {
		if (connection != null) {
			try {
				if(!connection.getAutoCommit())
					connection.commit();
				connection.close();
				connection = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (preparedStatement != null) {
			try {
				preparedStatement.close();
				preparedStatement = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
