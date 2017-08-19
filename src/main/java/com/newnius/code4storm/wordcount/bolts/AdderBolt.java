package com.newnius.code4storm.wordcount.bolts;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.security.InvalidParameterException;
import java.util.Map;



public class AdderBolt implements IRichBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5298442158708676938L;
	private OutputCollector collector;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
			String[] numbers = input.getString(1).split("\\+");
			Integer added = 0;
			if(numbers.length<2){
			throw new InvalidParameterException("Should be at least 2 numbers");
			}
			for(String num : numbers){
			added += Integer.parseInt(num);
			}
			collector.emit(new Values(input.getValue(0),added));
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("id", "result"));
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
