package com.newnius.code4storm.wordcount;

import com.newnius.code4storm.wordcount.bolts.BookJsonDecoder;
import com.newnius.code4storm.wordcount.bolts.BookSaver;
import com.newnius.code4storm.wordcount.spouts.JsonReader;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;


public class SaveBookTopo {
	public static void main(String[] args) throws AuthorizationException {
		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("json-reader", new JsonReader(), 1);
		builder.setBolt("book-json-decoder", new BookJsonDecoder(), 1).shuffleGrouping("json-reader");
		// builder.setBolt("word-counter", new WordCounter(),
		// 2).shuffleGrouping("word-normalizer");
		builder.setBolt("book-saver", new BookSaver(), 2).shuffleGrouping("book-json-decoder");
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

		// cluster mode
		try {
			StormSubmitter.submitTopology("save-book-topo", conf, builder.createTopology());
		} catch (AlreadyAliveException | InvalidTopologyException e) {
			e.printStackTrace();
		}

	}

}
