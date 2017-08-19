package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.Book;
import com.newnius.code4storm.marmot.model.Category;
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
import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by newnius on 9/28/16.
 */
public class CategorySaver implements IRichBolt{

    private static final long serialVersionUID = -169113083247495056L;

    private static final Logger logger  = LoggerFactory.getLogger(BookSaver.class);

    private Connection conn;
    private OutputCollector collector;
    private PreparedStatement psInsertCategory;
    private PreparedStatement psSelectCategoryName;
    private PreparedStatement psInsertRelation;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://mysql/book?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root","123456");
            String sql = "insert ignore into category (`category_name`) values(?)";
            psInsertCategory = conn.prepareStatement(sql);

            sql = "SELECT category_id from category where category_name = ? ";
            psSelectCategoryName = conn.prepareStatement(sql);

            sql = "insert ignore into book_belong_to_category (`asin`, `category_id`) values(?, ?)";
            psInsertRelation = conn.prepareStatement(sql);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void execute(Tuple tuple) {
        Category category = (Category) tuple.getValueByField("category");
        Book book = (Book) tuple.getValueByField("book");


        try {
            psInsertCategory.setString(1, category.getName());
            psInsertCategory.executeUpdate();

            psSelectCategoryName.setString(1, category.getName());
            ResultSet rs = psSelectCategoryName.executeQuery();

            Integer category_id = null;
            if (rs.next()) {
                category_id = rs.getInt("category_id");
            }
            rs.close();

            psInsertRelation.setString(1, book.getAsin());
            psInsertRelation.setInt(2, category_id);
            psInsertRelation.executeUpdate();

            collector.ack(tuple);
        }catch(Exception ex){
            logger.error(ex.getMessage()+ex.toString());
            collector.fail(tuple);
        }


    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
