package com.newnius.code4storm.marmot;


import com.newnius.code4storm.marmot.spouts.ImportBookWrittenByAuthor;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;


public class NormalJavaTopo {
	public static void main(String[] args) {

		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("normal", new ImportBookWrittenByAuthor(), 1);
		// Configuration
		Config conf = new Config();
		String topologyName = args[1];

		//conf.setDebug(false);
		// Topology run
		//conf.setNumWorkers(2);
		//conf.setMaxSpoutPending(1000);
		conf.put("jsonfile", args[0]);

		// cluster mode
		try {
			StormSubmitter.submitTopology(topologyName, conf, builder.createTopology());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
