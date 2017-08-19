package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.Book;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

public class RelatedBooksSaver implements IRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3740317406882068376L;
	private Logger logger;
	private OutputCollector collector;
	private PreparedStatement preparedStatement;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.logger = LoggerFactory.getLogger(RelatedBooksSaver.class);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://mysql/book?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root", "123456");
			String sql = "insert ignore into related_books (asin, related_book_asin, related_type) values(?,?,?)";
			preparedStatement = conn.prepareStatement(sql);
		}catch (Exception ex){
			ex.printStackTrace();
			logger.warn("Get connection fail. " + ex.getMessage());
		}
	}

	@Override
	public void execute(Tuple input) {
		Book book = (Book)input.getValueByField("book");
		try {
			if(book.getRelated() != null){
				Map<String, List<String>> relates = book.getRelated();
				for(Map.Entry<String, List<String>> entry: relates.entrySet()){
					for(String related_book_asin: entry.getValue()){
						preparedStatement.setString(1, book.getAsin());
						preparedStatement.setString(2, related_book_asin);
						preparedStatement.setInt(3, Book.getRelateType(entry.getKey()));
						preparedStatement.executeUpdate();
					}
				}
			}
			collector.ack(input);
		}catch(Exception ex){
			ex.printStackTrace();
			logger.warn("Insert related_books fail. " + ex.getMessage());
			collector.fail(input);
		}

	}

	@Override
	public void cleanup() {

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
