package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.Book;
import com.newnius.code4storm.marmot.model.Review;
import com.newnius.code4storm.marmot.util.CRDAO;
import com.newnius.code4storm.marmot.util.CRLogger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import redis.clients.jedis.Jedis;

import java.sql.ResultSet;
import java.util.Map;


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


		try {

			String sql = "select teaching_no, right(verify_card_no, 6) as password from student,tax_student where student.student_no = tax_student.student_no order by teaching_no";

			String args[] = {};
			CRDAO crdao = CRDAO.getInstance();
			crdao.setAutoCommit(false);
			ResultSet rs = crdao.executeQuery(sql, args);
			if(rs!=null && !rs.wasNull() ){
				while(rs.next()){
					int teachingNo = rs.getInt("teaching_no");
					String password = rs.getString("password");
					CRLogger.info("Main", teachingNo+":" + password);

					//Thread.sleep(100);

				}
			}else{
				CRLogger.warn("Main", "rs is null");
			}

			crdao.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}



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
