package com.newnius.code4storm.marmot.spouts;


import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;


public class JsonReader implements IRichSpout {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3543053685348487418L;

	private SpoutOutputCollector collector;
	private boolean completed = false;
	private BufferedReader reader;

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JsonReader.class);

	@SuppressWarnings("rawtypes")
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		try {
			FileReader fileReader = new FileReader(conf.get("jsonfile").toString());
			// Open the reader
			reader = new BufferedReader(fileReader);

		} catch (Exception e) {
			logger.error(e.getMessage()+e.toString());
			throw new RuntimeException("Error reading file[" + conf.get("jsonfile") + "]");
		}
		this.collector = collector;
	}

	@Override
	public void close() {

	}

	@Override
	public void activate() {

	}

	@Override
	public void deactivate() {

	}

	@Override
	public void nextTuple() {
		/**
		 * The nextuple it is called forever, so if we have been readed the file
		 * we will wait and then return
		 */
		if (completed) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// Do nothing
			}
			return;
		}
		String str;
		try {
			if ((str = reader.readLine()) != null) {
				this.collector.emit(new Values(str), str);
			}else{
				completed = true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+e.toString());
			throw new RuntimeException("Error reading tuple", e);
		}
		
	}

	@Override
	public void ack(Object msgId) {
		//logger.info("ack "+msgId);
	}

	@Override
	public void fail(Object msgId) {
		collector.emit(new Values((String)msgId), msgId);
		//logger.info("fail "+msgId);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("json"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
