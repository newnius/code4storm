package com.newnius.code4storm.marmot.spouts;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newnius.code4storm.marmot.model.BookAuthorPair;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;


public class ImportBookWrittenByAuthor implements IRichSpout {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3543053685348487418L;

	private SpoutOutputCollector collector;
	private BufferedReader br;

	private PreparedStatement psAuthor;
	private PreparedStatement psPair;
	private PreparedStatement psSelect;

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ImportBookWrittenByAuthor.class);

	private boolean completed = false;

	@SuppressWarnings("rawtypes")
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		try {
			br= new BufferedReader(new FileReader(conf.get("jsonfile").toString()));

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://mysql/book?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root", "123456");
			String sqlAuthor = "insert into used_author ( `author_name`) values( ?)";
			String sqlPair = "insert into used_book_written_by_author (`asin`, `author_id`) values(?, ?)";
			String sqlSelect = "select author_id from used_author where author_name = ?";
			psAuthor = conn.prepareStatement(sqlAuthor);
			psPair = conn.prepareStatement(sqlPair);
			psSelect = conn.prepareStatement(sqlSelect);
		}catch (Exception ex){
			logger.error("database connection error_"+ex.getMessage());
		}
	}

	@Override
	public void close() {

	}

	@Override
	public void activate() {

	}

	@Override
	public void deactivate() {

	}

	@Override
	public void nextTuple() {
        logger.info("ahahaha");
		if(completed){
			try {
				Thread.sleep(5001);
				return ;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return ;
		}
        completed = true;
		try {
			String json = "";
			String str;
			while( (str=br.readLine())!=null ){
				json+=str;
			}

			List<BookAuthorPair> pairs = new Gson().fromJson(json, new TypeToken<List<BookAuthorPair>>(){}.getType());

			for(BookAuthorPair pair: pairs){
                psSelect.setString(1, pair.getBook_author());
                ResultSet rs = psSelect.executeQuery();
                Integer authorID = null;
                if(rs.next()){
                    authorID = rs.getInt("author_id");
                }else{
                    psAuthor.setString(1, pair.getBook_author());
                    psAuthor.execute();
					psSelect.setString(1, pair.getBook_author());
					rs = psSelect.executeQuery();
					if(rs.next()){
						authorID = rs.getInt("author_id");
					}
				}

                psPair.setString(1, pair.getBook_id().get(0));
                psPair.setInt(2, authorID);
                psPair.execute();

            }

			psPair.close();
            psSelect.close();
            psAuthor.close();
		}catch (Exception ex){
			ex.printStackTrace();
            logger.warn(ex.getMessage());
		}
	}

	@Override
	public void ack(Object msgId) {
		//logger.info("ack "+msgId);
	}

	@Override
	public void fail(Object msgId) {
		collector.emit(new Values((String)msgId), msgId);
		//logger.info("fail "+msgId);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("json"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
