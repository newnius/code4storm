package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.RelatedBook;
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
public class Neo4jRelatedBooksRelationCreator implements IRichBolt{
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
        RelatedBook relatedBook = (RelatedBook) input.getValue(0);

        try {
            Session session = Neo4jPool.getSession();
            session.run(
                    "MATCH (book1:Book {asin:{asin}})\n"
                    +"WITH book1\n"
                    +"Match (book2:Book {asin:{related_book_asin}})\n"
                    +"CREATE UNIQUE (book1)-[:"+getRelationString(relatedBook.getRelatedType())+"]->(book2)"
                    ,
                    Values.parameters(
                            "asin", relatedBook.getAsin(),
                            "related_book_asin", relatedBook.getRelatedBookAsin()
                    ));
            session.close();
            collector.ack(input);
        }catch(Exception ex){
            ex.printStackTrace();
            collector.fail(input);
            logger.error("error when create node." + ex.getMessage());
        }
    }

    private String getRelationString(int relatedType){
        switch (relatedType){
            case 0:
                return "ALSO_VIEWED";
            case 1:
                return "BUY_AFTER_VIEWING";
            case 2:
                return "ALSO_BOUGHT";
            case 3:
                return "BOUGHT_TOGETHER";
            default:
                return "DEFAULT";
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
