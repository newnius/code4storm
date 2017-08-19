package com.newnius.code4storm.marmot.bolts;

import com.google.gson.Gson;
import com.newnius.code4storm.marmot.model.Book;
import com.newnius.code4storm.marmot.model.BookReviewPair;
import com.newnius.code4storm.marmot.model.Review;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReviewBookTitleRecorder implements IRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2573395699227036642L;
	private OutputCollector collector;
	private List<BookReviewPair> reviewBookTitles;
	private Logger logger;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.reviewBookTitles = new ArrayList<>();
		this.logger = LoggerFactory.getLogger(getClass());
	}

	@Override
	public void execute(Tuple input) {
		Book book = (Book) input.getValueByField("book");
		Review review = (Review) input.getValueByField("review");

		reviewBookTitles.add(new BookReviewPair(review, book));

		// collector.emit(new Values(""));
		collector.ack(input);
	}

	@Override
	public void cleanup() {
		String json = new Gson().toJson(reviewBookTitles);
		// write to file
		logger.info(json);
		try {
			FileWriter fw = new FileWriter("reviewBooks.out");
			fw.write(json);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// declarer.declare(new Fields(""));

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
