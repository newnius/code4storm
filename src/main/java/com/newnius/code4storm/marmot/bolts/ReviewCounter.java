package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.Review;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class ReviewCounter implements IRichBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4378583837354066763L;
	private OutputCollector collector;
	private Jedis jedis;
	private Map<String, Integer> cnts;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.jedis = new Jedis(stormConf.get("host").toString(), new Integer(stormConf.get("port").toString()));
		this.cnts = new HashMap<>();
	}

	@Override
	public void execute(Tuple input) {
		Review review = (Review) input.getValueByField("review");
		
		Integer cnt = cnts.get(review.getAsin());
		if(cnt==null){
			cnt = 0;
		}
		cnt++;
		cnts.put(review.getAsin(), cnt);
		
		// Acknowledge the tuple
		collector.ack(input);
	}

	/*
	 * cleanup() is not guaranteed to be called
	 * 
	 * */
	@Override
	public void cleanup() {
		for(Map.Entry<String, Integer> entry: cnts.entrySet()){
			jedis.set("book_" + entry.getKey() + ".reviews", entry.getValue()+"");
		}
		jedis.close();
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
