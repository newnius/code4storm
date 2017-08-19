package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.Book;
import com.newnius.code4storm.marmot.model.RankList;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;

/**
 * Created by newnius on 9/29/16.
 *
 */
public class RankListSaver implements IRichBolt{
    private static final long serialVersionUID = -3318356710890582436L;

    private static final Logger logger = LoggerFactory.getLogger(RankListSaver.class);

    private Connection conn;
    private OutputCollector collector;
    private PreparedStatement psInsertList;
    private PreparedStatement psSelectList;
    private PreparedStatement psInsertRank;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://mysql/book?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root","123456");
            String sql = "insert ignore into list (`list_name`) values(?)";
            psInsertList = conn.prepareStatement(sql);

            sql = "SELECT list_id from list where list_name = ? ";
            psSelectList = conn.prepareStatement(sql);

            sql = "insert ignore into sales_rank (`asin`, `list_id`, `rank`) values(?, ?, ?)";
            psInsertRank = conn.prepareStatement(sql);
        }catch (Exception ex){
            logger.error(ex.getMessage()+ex.toString());
        }
    }

    @Override
    public void execute(Tuple tuple) {
        Book book = (Book) tuple.getValueByField("book");
        RankList list = (RankList) tuple.getValueByField("list");
        Integer rank = (Integer) tuple.getValueByField("rank");


        try {
            psInsertList.setString(1, list.getName());
            psInsertList.executeUpdate();


            psSelectList.setString(1, list.getName());
            ResultSet rs = psSelectList.executeQuery();

            Integer list_id = null;
            if (rs.next()) {
                list_id = rs.getInt("list_id");
            }
            rs.close();


            psInsertRank.setString(1, book.getAsin());
            psInsertRank.setInt(2, list_id);
            psInsertRank.setInt(3, rank);
            psInsertRank.executeUpdate();

            collector.ack(tuple);

        }catch(Exception ex){
            logger.error(ex.getMessage()+ex.toString(), ex);
            collector.fail(tuple);
        }


    }

    @Override
    public void cleanup() {
        try{
            conn.close();
        }
        catch (SQLException e){
            logger.error(e.getMessage()+e.toString());
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
