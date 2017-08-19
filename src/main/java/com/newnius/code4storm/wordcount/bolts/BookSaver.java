package com.newnius.code4storm.wordcount.bolts;

import java.util.Map;

import com.newnius.code4storm.wordcount.models.Book;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

public class BookSaver implements IRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3740317406882068376L;
	
	
	private OutputCollector collector;
	private Jedis jedis;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.jedis = new Jedis(stormConf.get("host").toString(), new Integer(stormConf.get("port").toString()));
	}

	@Override
	public void execute(Tuple input) {
		Book book = (Book)input.getValueByField("book");
		
		String title = book.getTitle()==null ? "": book.getTitle();
		
		jedis.hset("book_"+book.getAsin(), "title", title);
		collector.ack(input);
	}

	@Override
	public void cleanup() {
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
