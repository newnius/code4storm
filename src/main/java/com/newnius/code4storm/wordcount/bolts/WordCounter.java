package com.newnius.code4storm.wordcount.bolts;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;


public class WordCounter implements IRichBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5209329159689241445L;
	Integer id;
	String name;
	Map<String, Integer> counters;
	private OutputCollector collector;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.counters = new HashMap<String, Integer>();
		this.collector = collector;
		this.name = context.getThisComponentId();
		this.id = context.getThisTaskId();

	}

	@Override
	public void execute(Tuple input) {
		String str = input.getString(0);
		/**
		 * If the word dosn't exist in the map we will create this, if not We
		 * will add 1
		 */
		if (!counters.containsKey(str)) {
			counters.put(str, 1);
		} else {
			Integer c = counters.get(str) + 1;
			counters.put(str, c);
		}
		Logger.getLogger(getClass()).info(str);
		// Set the tuple as Acknowledge
		collector.ack(input);
		
	}

	@Override
	public void cleanup() {
		Logger logger = Logger.getLogger("WordCounter");

		System.out.println("-- Word Counter [" + name + "-" + id + "] --");
		logger.info("-- Word Counter [" + name + "-" + id + "] --");
		for (Map.Entry<String, Integer> entry : counters.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
			logger.info(entry.getKey() + ": " + entry.getValue());
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
