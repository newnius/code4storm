package com.newnius.code4storm.marmot;


import com.newnius.code4storm.marmot.bolts.*;
import com.newnius.code4storm.marmot.spouts.JsonReader;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;


public class SaveRelatedBooksTopo {
	public static void main(String[] args) {

		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("json-reader", new JsonReader(), 1);
		builder.setBolt("book-json-decoder", new BookJsonDecoder(), 1).shuffleGrouping("json-reader");
		builder.setBolt("related-books-saver", new RelatedBooksSaver(), 20).shuffleGrouping("book-json-decoder", "book");
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
