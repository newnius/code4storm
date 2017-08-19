package com.newnius.code4storm.marmot;


import com.newnius.code4storm.marmot.bolts.*;
import com.newnius.code4storm.marmot.spouts.*;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;


public class Neo4jCreateNodesTopo {

	public static void main(String[] args) {

		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("mysql-book-reader", new MysqlBookReader(), 1);
		builder.setSpout("mysql-review-reader", new MysqlReviewReader(), 1);
		builder.setSpout("mysql-reviewer-reader", new MysqlReviewerReader(), 1);
		builder.setSpout("mysql-category-reader", new MysqlCategoryReader(), 1);
		builder.setSpout("mysql-ranklist-reader", new MysqlRankListReader(), 1);

		builder.setBolt("book-node-creator", new Neo4jBookNodeCreator(), 5).shuffleGrouping("mysql-book-reader");
		builder.setBolt("review-node-creator", new Neo4jReviewNodeCreator(), 40).shuffleGrouping("mysql-review-reader");
		builder.setBolt("reviewer-node-creator", new Neo4jReviewerNodeCreator(), 30).shuffleGrouping("mysql-reviewer-reader");
		builder.setBolt("category-node-creator", new Neo4jCategoryNodeCreator(), 2).shuffleGrouping("mysql-category-reader");
		builder.setBolt("ranklist-node-creator", new Neo4jRankListNodeCreator(), 1).shuffleGrouping("mysql-ranklist-reader");


		// Configuration
		Config conf = new Config();
		String topologyName = args[0];

		conf.setDebug(false);
		// Topology run
		conf.setMaxSpoutPending(1000);
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
