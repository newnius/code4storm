package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.Author;
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

import java.util.List;
import java.util.Map;

/**
 * Created by newnius on 10/17/16.
 *
 */
public class Neo4jAuthorCreator implements IRichBolt{
    private static final long serialVersionUID = -2573592865000534209L;
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
        String asin = (String)input.getValue(0);
        Author author = (Author)input.getValue(1);

        logger.info(""+asin);

        try {
            Session session = Neo4jPool.getSession();
            session.run("CREATE ( :Author { id:{id}, name:{name}, awards_and_honors:{awards_and_honors}, birth_place:{birth_place}, country:{country}, " +
                            "date_of_birth:{date_of_birth}, favorited:{favorited}, gender:{gender}, members:{members}, nationality:{nationality}, occupations:{occupations}," +
                            "organizations:{organizations}, place_of_residences:{place_of_residences}, popularity:{popularity}, rating:{rating}, reviews:{reviews} } )",
                    Values.parameters(
                            "id", author.getAuthorId(),
                            "name", author.getBook_author(),
                            "awards_and_honors", author.getAwards_an_honors(),
                            "birth_place", author.getBirthplace(),
                            "country", author.getCountry(),
                            "date_of_birth", author.getDate_of_birth(),
                            "favorited", author.getFavorited(),
                            "gender", author.getGender().get(0),
                            "members", author.getMembers(),
                            "nationality", author.getNationality(),
                            "occupations", author.getOccupations(),
                            "organizations", author.getOrganizations(),
                            "place_of_residences", author.getPlaces_of_residence(),
                            "popularity", author.getPopularity(),
                            "rating", author.getRating(),
                            "reviews", author.getReviews()
                    ));
            session.close();
            logger.info("add author");
        }catch (Exception ex){
            logger.warn(ex.getMessage());
        }
        try {
            List<String> tags = author.getTags();
            for (String tag : tags) {
                try {
                    Session session = Neo4jPool.getSession();
                    session.run("CREATE ( :Tag {name:{name}} )", Values.parameters("name", tag));
                    session.close();
                }catch (Exception ex){
                    logger.warn(ex.getMessage());
                }
                try {
                    Session session = Neo4jPool.getSession();
                    session.run(
                            "MATCH (author:Author {id:{id}})\n"
                                    + "WITH author\n"
                                    + "Match (tag:Tag {name:{name}})\n"
                                    + "CREATE UNIQUE (author)-[:AUTHOR_HAS_TAG]->(tag)"
                            ,
                            Values.parameters(
                                    "id", author.getAuthorId(),
                                    "name", tag
                            ));
                    session.close();
                    logger.info("add tag " + tag);

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch (Exception ex){
            logger.warn(ex.getMessage());
        }

        try{
            Session session = Neo4jPool.getSession();
            session.run(
                    "MATCH (book:Book {asin:{asin}})\n"
                            +"WITH book\n"
                            +"Match (author:Author {id:{id}})\n"
                            +"CREATE UNIQUE (book)-[:BOOK_WRITTEN_BY]->(author)"
                    ,
                    Values.parameters(
                            "asin", asin,
                            "id", author.getAuthorId()
                    ));
            logger.info("add written_by");
            session.close();

            collector.ack(input);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("error when create node." + ex.getMessage());
            collector.fail(input);
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
