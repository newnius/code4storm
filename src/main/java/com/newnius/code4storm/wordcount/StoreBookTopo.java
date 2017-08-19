package com.newnius.code4storm.wordcount;

import com.newnius.code4storm.wordcount.bolts.BookJsonDecoder;
import com.newnius.code4storm.wordcount.bolts.BookSaver;
import com.newnius.code4storm.wordcount.spouts.JsonReader;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;


public class StoreBookTopo {
	public static void main(String[] args) {

		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("json-reader", new JsonReader());
		builder.setBolt("book-json-decoder", new BookJsonDecoder(), 2).shuffleGrouping("json-reader");
		builder.setBolt("book-saver", new BookSaver(), 5).shuffleGrouping("book-json-decoder");
		// Configuration
		Config conf = new Config();
		conf.put("jsonfile", args[0]);
		conf.put("host", "192.168.56.110");
		conf.put("port", "6379");
		conf.setDebug(false);
		// Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

		// local mode
		LocalCluster cluster = new LocalCluster();

		cluster.submitTopology("book-saver-topo", conf, builder.createTopology());

//		try {
//			Thread.sleep(1000000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		cluster.shutdown();

		/*
		 * //cluster mode builder.createTopology(); try {
		 * StormSubmitter.submitTopology("Getting-Started-Toplogie", conf,
		 * builder.createTopology()); } catch (AlreadyAliveException |
		 * InvalidTopologyException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

	}
}
