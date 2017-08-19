package com.newnius.code4storm.marmot.bolts;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.newnius.code4storm.marmot.model.Book;
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

public class BookJsonDecoder implements IRichBolt {
	private static final long serialVersionUID = 6371753393736158600L;

	private static Logger log = LoggerFactory.getLogger(BookJsonDecoder.class);

	private static Gson gson = new Gson();

	private OutputCollector collector;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;

	}

	@Override
	public void execute(Tuple input) {
		String json = input.getString(0);
		try {
			Book book = gson.fromJson(json, Book.class);
			collector.emit("book", input, new Values(book));
			collector.ack(input);
		} catch (JsonSyntaxException ex) {
			log.warn("json decode exception_"+ex.getMessage());
			collector.fail(input);
		}
	}

	@Override
	public void cleanup() {

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("book", new Fields("book"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
