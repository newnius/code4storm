package com.newnius.code4storm.marmot.bolts;


import com.newnius.code4storm.marmot.model.Book;
import com.newnius.code4storm.marmot.model.Category;
import com.newnius.code4storm.marmot.model.RankList;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.List;
import java.util.Map;

public class BookInfoDivider implements IRichBolt {
	private OutputCollector collector;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		Book book = (Book)input.getValueByField("book");

		collector.emit("book", input, new Values(book));

		if(book.getCategories() != null){
			List<List<String>> categoriess = book.getCategories();
			for(List<String> categories: categoriess){
				for(String category: categories){
					collector.emit("book_belong_to_category", input, new Values(book, new Category(category)));
				}
			}
		}

		if(book.getSalesRank() != null){
			Map<String,Integer> ranks = book.getSalesRank();
			for(Map.Entry<String, Integer> entry: ranks.entrySet()){
					collector.emit("book_belong_to_list", input, new Values(book, new RankList().setName(entry.getKey()), entry.getValue()));
			}
		}

		// Acknowledge the tuple
		collector.ack(input);
	}

	@Override
	public void cleanup() {

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("book_belong_to_category", new Fields("book", "category"));
		declarer.declareStream("book", new Fields("book"));
		declarer.declareStream("book_belong_to_list", new Fields("book", "list", "rank"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
