package com.newnius.code4storm.marmot.bolts;

import com.google.gson.Gson;
import com.newnius.code4storm.marmot.model.Review;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class ReviewJsonDecoder implements IRichBolt {
	private static final long serialVersionUID = 2959040273795469408L;
	private OutputCollector collector;
	private Logger logger;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.logger = LoggerFactory.getLogger(getClass());
	}

	@Override
	public void execute(Tuple input) {
		String json = input.getString(0);
		try {
			Review review = new Gson().fromJson(json, Review.class);
			collector.emit("review", input, new Values(review.getAsin(), review));
			collector.ack(input);
		} catch (Exception ex) {
			logger.warn("Error when parsing json, "+ json, ex);
			collector.fail(input);
		}
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("review", new Fields("asin", "review"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
