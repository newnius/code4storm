package com.newnius.code4storm.marmot;


import com.newnius.code4storm.marmot.bolts.ReviewJsonDecoder;
import com.newnius.code4storm.marmot.bolts.ReviewSaver;
import com.newnius.code4storm.marmot.spouts.JsonReader;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;


public class SaveReviewTopo {
	public static void main(String[] args) {

		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("json-reader", new JsonReader(), 1);
		builder.setBolt("review-json-decoder", new ReviewJsonDecoder(), 1).shuffleGrouping("json-reader");
		builder.setBolt("review-saver", new ReviewSaver(), 30).shuffleGrouping("review-json-decoder", "review");
		// Configuration
		Config conf = new Config();
		conf.put("jsonfile", args[0]);
		String topologyName = args[1];

		conf.setDebug(false);
		// Topology run
		//conf.setNumWorkers(2);
		conf.setMaxSpoutPending(1000);

		// cluster mode
		try {
			StormSubmitter.submitTopology(topologyName, conf, builder.createTopology());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
