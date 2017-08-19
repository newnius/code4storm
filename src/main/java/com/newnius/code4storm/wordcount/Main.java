package com.newnius.code4storm.wordcount;

import org.apache.log4j.Logger;

import com.newnius.code4storm.wordcount.bolts.AdderBolt;
import com.newnius.code4storm.wordcount.bolts.WordCounter;
import com.newnius.code4storm.wordcount.bolts.WordNormalizer;
import com.newnius.code4storm.wordcount.spouts.WordReader;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.drpc.LinearDRPCTopologyBuilder;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;


public class Main {
	public static void main(String[] args) {
		
		//testDRPC();
		
		
		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("word-reader", new WordReader());
		builder.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader");
		// builder.setBolt("word-counter", new WordCounter(),
		// 2).shuffleGrouping("word-normalizer");
		builder.setBolt("word-counter", new WordCounter(), 2).fieldsGrouping("word-normalizer", new Fields("word"));
		// Configuration
		Config conf = new Config();
		conf.put("wordsFile", args[0]);
		conf.setDebug(false);
		// Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		
		//local mode
		 LocalCluster cluster = new LocalCluster();
		
		 cluster.submitTopology("Getting-Started-Toplogie", conf,
		 builder.createTopology());
		
		 try {
		 Thread.sleep(10000);
		 } catch (InterruptedException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		 cluster.shutdown();
		

		/*
		//cluster mode 
		builder.createTopology();
		try {
			StormSubmitter.submitTopology("Getting-Started-Toplogie", conf, builder.createTopology());
		} catch (AlreadyAliveException | InvalidTopologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

	}
	
	public static void testDRPC(){
		LocalDRPC drpc = new LocalDRPC();
		LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("add");
		builder.addBolt(new AdderBolt(),2);
		Config conf = new Config();
		conf.setDebug(true);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("drpc-adder-topology", conf,
		builder.createLocalTopology(drpc));
		String result = drpc.execute("add", "1+-1");
		Logger.getLogger("Result").info(result);
		result = drpc.execute("add", "1+1+5+10");
		Logger.getLogger("Result").info(result);
		cluster.shutdown();
		drpc.shutdown();
	}

}
