package com.newnius.code4storm.wordcount.bolts;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.newnius.code4storm.wordcount.models.Review;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;


@SuppressWarnings("serial")
public class ReviewJsonDecoder implements IRichBolt {
	private OutputCollector collector;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;

	}

	@Override
	public void execute(Tuple input) {
		String json = input.getString(0);
		if (json == null) {
			return;
		}
		try {
			Review review = new Gson().fromJson(json, Review.class);
			collector.emit(new Values(review.getAsin(), review));
			// Acknowledge the tuple
			collector.ack(input);
		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.getLogger(getClass().getName()).log(Level.WARNING, json);
			collector.fail(input);
		}
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("asin", "review"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
