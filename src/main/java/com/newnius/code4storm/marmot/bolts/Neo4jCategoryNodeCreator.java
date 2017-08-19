package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.Category;
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
public class Neo4jCategoryNodeCreator implements IRichBolt{
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
        Category category = (Category) input.getValue(0);
        try {
            Session session = Neo4jPool.getSession();
            session.run("CREATE ( :Category { id:{id}, name:{name} } )",
                    Values.parameters(
                            "id", category.getCategoryID(),
                            "name", category.getName()
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
