package com.newnius.code4storm.marmot.spouts;

import com.newnius.code4storm.marmot.model.Book;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by newnius on 10/17/16.
 *
 */
public class MysqlBookReader implements IRichSpout{
    private SpoutOutputCollector collector;
    private Logger logger;
    private Connection conn;
    private int offset = 0;
    private int batchSize = 1000;
    private boolean finished = false;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        this.logger = LoggerFactory.getLogger(getClass());

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://mysql/book?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root", "123456");
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
        if(finished){
            try{
                Thread.sleep(20);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return ;
        }


        try {
            String sql = "select asin, imUrl, title, description, price from `used_books` limit " +offset +", " + batchSize;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            int cnt = 0;
            while(rs.next()){
                Book book = new Book(rs.getString(1));
                book.setImUrl(rs.getString(2));
                book.setTitle(rs.getString(3));
                book.setDescription(rs.getString(4));
                book.setPrice(rs.getFloat(5));
                cnt++;
                collector.emit(new Values(book), book);
            }
            statement.close();
            if(cnt == batchSize) {
                offset += batchSize;
            }else{
                finished = true;
            }
        }catch (Exception ex){
            logger.error("error when reading record. "+ex.getMessage());
        }
    }

    @Override
    public void ack(Object msgId) {
        logger.info("ack "+((Book)msgId).getAsin());
    }

    @Override
    public void fail(Object msgId) {
        collector.emit(new Values((Book)msgId), msgId);
        logger.info("fail "+ ((Book) msgId).getAsin());
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("book"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
