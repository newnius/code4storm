package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.Review;
import com.newnius.code4storm.marmot.util.Neo4jPool;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by newnius on 10/17/16.
 *
 */
public class Neo4jBookReviewReviewerRelationCreator implements IRichBolt{
    private Logger logger;
    private OutputCollector collector;


    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.logger = LoggerFactory.getLogger(getClass());
        Neo4jPool.init();
    }

    @Override
    public void execute(Tuple input) {
        Review review = (Review) input.getValue(0);
        Session session = Neo4jPool.getSession();
        Transaction tx = session.beginTransaction();
        try {

            tx.run(
                    "MATCH (reviewer:Reviewer {id:{id}})\n"
                            +"WITH reviewer\n"
                            +"Match (book:Book {asin:{asin}})\n"
                            +"CREATE UNIQUE (reviewer)-[:BUY]->(book)"
                    ,
                    Values.parameters(
                            "id", review.getReviewerID(),
                            "asin", review.getAsin()
                    ));

            tx.run(
                    "MATCH (reviewer:Reviewer {id:{reviewer_id}})\n"
                            +"WITH reviewer\n"
                            +"Match (review:Review {id:{review_id}})\n"
                            +"CREATE UNIQUE (reviewer)-[:WRITE {time:{time}}]->(review)"
                    ,
                    Values.parameters(
                            "reviewer_id", review.getReviewerID(),
                            "review_id", review.getReviewID(),
                            "time", review.getUnixReviewTime()
                    ));

            tx.run(
                    "MATCH (review:Review {id:{id}})\n"
                            +"WITH review\n"
                            +"Match (book:Book {asin:{asin}})\n"
                            +"CREATE UNIQUE (review)-[:DESCRIBE]->(book)"
                    ,
                    Values.parameters(
                            "id", review.getReviewID(),
                            "asin", review.getAsin()
                    ));
            tx.success();
            collector.ack(input);
        }catch(Exception ex){
            ex.printStackTrace();
            tx.failure();
            collector.fail(input);
            logger.error("error when create node." + ex.getMessage());
        }finally {
            session.close();
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
