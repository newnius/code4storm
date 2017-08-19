package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.Book;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;


public class BookSaver implements IRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3740317406882068376L;

	private static final Logger logger = LoggerFactory.getLogger(BookSaver.class);;
	private Connection conn;
	private OutputCollector collector;
	private String sql;
	private PreparedStatement preparedStatement;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://mysql/book?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root","123456");

			sql = "insert ignore into book (asin, imurl, title, description, price) values(?,?,?,?,?)";
			preparedStatement = conn.prepareStatement(sql);
		}catch (Exception ex){
			logger.error("database connection error_"+ex.getMessage());
		}
	}

	@Override
	public void execute(Tuple input) {
		Book book = (Book)input.getValueByField("book");

		try {
			preparedStatement.setString(1, book.getAsin());

			if(book.getImUrl() != null){
				preparedStatement.setString(2, book.getImUrl());
			}else{
				preparedStatement.setNull(2, Types.VARCHAR);
			}

			if(book.getTitle() != null){
				preparedStatement.setString(3, book.getTitle());
			}else{
				preparedStatement.setNull(3, Types.VARCHAR);
			}

			if(book.getDescription() != null){
				preparedStatement.setString(4, book.getDescription());
			}else{
				preparedStatement.setNull(4, Types.VARCHAR);
			}

			if(book.getPrice() != null){
				preparedStatement.setFloat(5, book.getPrice());
			}else {
				preparedStatement.setNull(5, Types.DECIMAL);
			}

			preparedStatement.executeUpdate();
			collector.ack(input);

		}catch(Exception ex){
			//ex.printStackTrace();
			collector.fail(input);
		}
	}

	@Override
	public void cleanup() {
		try {
			conn.close();
		}
		catch (SQLException e){
			logger.error(e.getMessage());
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
