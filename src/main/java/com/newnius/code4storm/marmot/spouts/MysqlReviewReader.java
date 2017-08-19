package com.newnius.code4storm.marmot.spouts;

import com.newnius.code4storm.marmot.model.Review;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by newnius on 10/17/16.
 *
 */
public class MysqlReviewReader implements IRichSpout{
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
                Thread.sleep(21);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return ;
        }


        try {
            /*
             * Pay attention :
             * When offset is rather big, it is not suitable to use "limit {offset}, {size}"
             * say, 10000000.
             * Because it could be rather slow.
             * At this situation, the right way should be "where asin > {$max_asin} limit {size}"
             *
             */
            String sql = "select review_id, reviewer_id, reviewer, asin, review_text, helpful_upvote, helpful_sum, overall, summary, unix_review_time from `used_reviews` limit " +offset +", " + batchSize;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            int cnt = 0;
            while(rs.next()){
                Review review = new Review();
                review.setReviewID(rs.getInt(1));
                review.setReviewerID(rs.getString(2));
                review.setReviewerName(rs.getString(3));
                review.setAsin(rs.getString(4));
                review.setReviewText(rs.getString(5));

                List<Integer> helpful = new ArrayList<>();
                helpful.add(rs.getInt(6));
                helpful.add(rs.getInt(7));
                review.setHelpful(helpful);

                review.setOverall(rs.getDouble(8));
                review.setSummary(rs.getString(9));
                review.setUnixReviewTime(rs.getInt(10));

                cnt++;
                collector.emit(new Values(review), review);
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
        logger.info("ack "+ ((Review) msgId).getReviewID());
    }

    @Override
    public void fail(Object msgId) {
        collector.emit(new Values((Review)msgId), msgId);
        logger.info("fail "+ ((Review) msgId).getReviewID());
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("review"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
