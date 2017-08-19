package com.newnius.code4storm.marmot;


import com.newnius.code4storm.marmot.bolts.*;
import com.newnius.code4storm.marmot.spouts.*;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;


public class Neo4jCreateRelationsTopo {

	public static void main(String[] args) {

		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("mysql-related-books-reader", new MysqlRelatedBooksReader(), 1);
		builder.setSpout("mysql-book-belong-to-category-reader", new MysqlBookBelongToCategoryReader(), 1);
		builder.setSpout("mysql-book-in-ranklist-reader", new MysqlBookInRankListReader(), 1);
		builder.setSpout("mysql-review-reader", new MysqlReviewReader(), 1);

		builder.setBolt("related-books-relation-creator", new Neo4jRelatedBooksRelationCreator(), 5).shuffleGrouping("mysql-related-books-reader");
		builder.setBolt("book-belong-to-category-relation-creator", new Neo4jBookBelongToCategoryRelationCreator(), 5).shuffleGrouping("mysql-book-belong-to-category-reader");
		builder.setBolt("book-in-ranklist-relation-creator", new Neo4jBookInRankListRelationCreator(), 5).shuffleGrouping("mysql-book-in-ranklist-reader");
		builder.setBolt("book-review-reviewer-relation-creator", new Neo4jBookReviewReviewerRelationCreator(), 40).shuffleGrouping("mysql-review-reader");


		// Configuration
		Config conf = new Config();
		String topologyName = args[0];

		conf.setDebug(false);
		// Topology run
		conf.setMaxSpoutPending(2000);
		conf.setMessageTimeoutSecs(3000);

		//conf.setNumWorkers(4);

		// cluster mode
		try {
			StormSubmitter.submitTopology(topologyName, conf, builder.createTopology());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
