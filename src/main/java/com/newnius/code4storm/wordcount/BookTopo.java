package com.newnius.code4storm.wordcount;

import com.newnius.code4storm.wordcount.bolts.BookGetter;
import com.newnius.code4storm.wordcount.bolts.ReviewBookTtitleRecorder;
import com.newnius.code4storm.wordcount.bolts.ReviewCounter;
import com.newnius.code4storm.wordcount.bolts.ReviewJsonDecoder;
import com.newnius.code4storm.wordcount.spouts.JsonReader;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;


public class BookTopo {
	public static void main(String[] args) {
		reviewBookTopo(args);
	}

	public static void reviewBookTopo(String[] args) {
		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("json-reader", new JsonReader());
		builder.setBolt("review-json-decoder", new ReviewJsonDecoder(), 2).shuffleGrouping("json-reader");
		builder.setBolt("book-getter", new BookGetter()).shuffleGrouping("review-json-decoder");
		builder.setBolt("review-book-recorder", new ReviewBookTtitleRecorder(), 1).shuffleGrouping("book-getter");
		
	
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

		cluster.submitTopology("book-review-topo", conf, builder.createTopology());
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cluster.shutdown();
	}

	public static void reviewCountTopo(String[] args) {
		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("json-reader", new JsonReader());
		builder.setBolt("review-json-decoder", new ReviewJsonDecoder(), 2).shuffleGrouping("json-reader");
		builder.setBolt("review-counter", new ReviewCounter(), 5).fieldsGrouping("review-json-decoder",
				new Fields("asin"));
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

		cluster.submitTopology("book-counter-topo", conf, builder.createTopology());
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cluster.shutdown();
	}

}
