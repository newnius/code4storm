package com.newnius.code4storm.marmot;


import com.newnius.code4storm.marmot.bolts.*;
import com.newnius.code4storm.marmot.spouts.*;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;


public class Neo4jCreateAuthorNodesAndRelationshipsTopo {

	public static void main(String[] args) {

		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("mysql-book-reader", new MysqlBookReader(), 1);

		builder.setBolt("author-reader", new AuthorReader(), 10).shuffleGrouping("mysql-book-reader");

		builder.setBolt("neo4j-author-creator", new Neo4jAuthorCreator(), 10).shuffleGrouping("author-reader");


		// Configuration
		Config conf = new Config();
		String topologyName = args[0];

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
