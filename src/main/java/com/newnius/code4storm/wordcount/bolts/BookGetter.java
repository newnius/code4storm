package com.newnius.code4storm.wordcount.bolts;

import java.util.Map;

import com.newnius.code4storm.wordcount.models.Book;
import com.newnius.code4storm.wordcount.models.Review;


import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import redis.clients.jedis.Jedis;


public class BookGetter implements IRichBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4682897189901284036L;
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
		Review review = (Review)input.getValueByField("review");
		
		Map<String, String> bookMap = jedis.hgetAll("book_"+review.getAsin());
		Book book = new Book(review.getAsin());
		book.setTitle(bookMap.get("title"));
		jedis.close();
		collector.emit(new Values(book, review));
		
		// Acknowledge the tuple
		collector.ack(input);
	}

	@Override
	public void cleanup() {
		jedis.close();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("book","review"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
