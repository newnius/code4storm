package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.Review;
import com.newnius.code4storm.marmot.util.Neo4jPool;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by newnius on 10/17/16.
 *
 */
public class Neo4jReviewNodeCreator implements IRichBolt{
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

        try {
            Session session = Neo4jPool.getSession();
            session.run("CREATE ( :Review { id:{id}, text:{text}, helpful_upvote:{helpful_upvote}, helpful_sum:{helpful_sum}, overall:{overall}, summary:{summary}, time:{time} } )",
                    Values.parameters(
                            "id", review.getReviewID(),
                            "text", review.getReviewText(),
                            "helpful_upvote", review.getHelpful().get(0),
                            "helpful_sum", review.getHelpful().get(1),
                            "overall", review.getOverall(),
                            "summary", review.getSummary(),
                            "time", review.getUnixReviewTime()
                    ));
            session.close();
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("error when create node." + ex.getMessage());
        }
        collector.ack(input);
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
