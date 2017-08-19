package com.newnius.code4storm.marmot;


import com.newnius.code4storm.marmot.bolts.*;
import com.newnius.code4storm.marmot.spouts.JsonReader;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;


public class SaveBookTopo {

	public static void main(String[] args) {

		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("json-reader", new JsonReader(), 1);
		builder.setBolt("book-json-decoder", new BookJsonDecoder(), 1).shuffleGrouping("json-reader");
		builder.setBolt("book-info-divider", new BookInfoDivider(), 1).shuffleGrouping("book-json-decoder", "book");
		builder.setBolt("book-saver", new BookSaver(), 20).shuffleGrouping("book-info-divider", "book");
		builder.setBolt("category-saver", new CategorySaver(), 15).shuffleGrouping("book-info-divider", "book_belong_to_category");
		builder.setBolt("list-saver", new RankListSaver(), 15).shuffleGrouping("book-info-divider", "book_belong_to_list");
		// Configuration
		Config conf = new Config();
		conf.put("jsonfile", args[0]);
		String topologyName = args[1];

		conf.setDebug(false);
		// Topology run
		conf.setMaxSpoutPending(1000);
		//conf.setNumWorkers(4);

		// cluster mode
		try {
			StormSubmitter.submitTopology(topologyName, conf, builder.createTopology());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
