package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.Review;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;


public class ReviewSaver implements IRichBolt {

	private static final long serialVersionUID = 3740317406882068376L;

	private static final Logger logger = LoggerFactory.getLogger(ReviewSaver.class);;
	private Connection conn;
	private OutputCollector collector;
	private PreparedStatement ps;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://mysql/book?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root","123456");

			String sql = "insert into review (reviewer, reviewer_id, asin, review_text, helpful_upvote, helpful_sum, overall, summary, unix_review_time) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
		}catch (Exception ex){
			logger.error("database connection error_"+ex.getMessage());
		}
	}

	@Override
	public void execute(Tuple input) {
		Review review = (Review)input.getValueByField("review");

		try {
			if(review.getReviewerName() != null){
				ps.setString(1, review.getReviewerName());
			}else{
				ps.setNull(1, Types.VARCHAR);
			}

			if(review.getReviewerID() != null){
				ps.setString(2, review.getReviewerID());
			}else{
				ps.setNull(2, Types.VARCHAR);
			}

			if(review.getAsin() != null){
				ps.setString(3, review.getAsin());
			}else{
				ps.setNull(3, Types.VARCHAR);
			}

			if(review.getReviewText() != null){
				ps.setString(4, review.getReviewText());
			}else{
				ps.setNull(4, Types.VARCHAR);
			}

			if(review.getHelpful() != null){
				ps.setInt(5, review.getHelpful().get(0));
				ps.setInt(6, review.getHelpful().get(1));
			}else{
				ps.setNull(5, Types.INTEGER);
				ps.setNull(6, Types.INTEGER);
			}


			if(review.getOverall() != null){
				ps.setDouble(7, review.getOverall());
			}else{
				ps.setNull(7, Types.DOUBLE);
			}

			if(review.getSummary() != null){
				ps.setString(8, review.getSummary());
			}else{
				ps.setNull(8, Types.VARCHAR);
			}

			if(review.getUnixReviewTime() != null){
				ps.setInt(9, review.getUnixReviewTime());
			}else{
				ps.setNull(9, Types.INTEGER);
			}
			ps.executeUpdate();
			collector.ack(input);

		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
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
