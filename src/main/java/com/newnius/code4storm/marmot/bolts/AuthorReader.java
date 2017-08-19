package com.newnius.code4storm.marmot.bolts;

import com.newnius.code4storm.marmot.model.Author;
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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AuthorReader implements IRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3740317406882068376L;

	private static final Logger logger = LoggerFactory.getLogger(AuthorReader.class);;
	private Connection conn;
	private OutputCollector collector;
	private PreparedStatement psAuthor;
	private PreparedStatement psProperty;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://mysql/book?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root","123456");

			String sqlAuthor = "select author_id from used_book_written_by_author where asin = ?";
			String sqlProperty = "SELECT `key`,`value` FROM used_author_property WHERE asin IN (SELECT asin FROM used_book_written_by_author WHERE author_id = ?)";
			psAuthor = conn.prepareStatement(sqlAuthor);
			psProperty = conn.prepareStatement(sqlProperty);
		}catch (Exception ex){
			logger.error("database connection error_"+ex.getMessage());
		}
	}

	@Override
	public void execute(Tuple input) {
		Book book = (Book)input.getValueByField("book");
		try {
			psAuthor.setString(1, book.getAsin());
			ResultSet rs = psAuthor.executeQuery();
			if(rs.next()){
				Integer authorID = rs.getInt("author_id");

				psProperty.setInt(1, authorID);
				ResultSet rs2 = psProperty.executeQuery();


                List<String> placesOfResidences = new ArrayList<>();
                List<String> dateOfBirth = new ArrayList<>();
                List<String> nationality = new ArrayList<>();
                List<String> country = new ArrayList<>();
                List<String> birthPlaces = new ArrayList<>();
                List<String> awardsAndHonors = new ArrayList<>();
                List<String> organizations = new ArrayList<>();
                List<String> occupations = new ArrayList<>();

                List<String> tags = new ArrayList<>();

                String authorName = null;
                String members = null;
                String gender = null;
                String popularity = null;
                String favorited = null;
                String rating = null;
                String reviews = null;

				while(rs2.next()){
                    String key = rs2.getString("key");
                    String value = rs2.getString("value");

                    switch (key){
                        case "favorited":
                            favorited = value;
                            break;
                        case "gender":
                            gender = value;
                            break;
                        case "members":
                            members = value;
                            break;
                        case "name":
                            authorName = value;
                            break;
                        case "popularity":
                            popularity = value;
                            break;
                        case "rating":
                            rating = value;
                            break;
                        case "reviews":
                            reviews = value;
                            break;

                        case "awards_and_honors":
                            awardsAndHonors.add(value);
                            break;
                        case "birth_place":
                            birthPlaces.add(value);
                            break;
                        case "country":
                            country.add(value);
                            break;
                        case "date_of_birth":
                            dateOfBirth.add(value);
                            break;
                        case "nationality":
                            nationality.add(value);
                            break;
                        case "occupation":
                            occupations.add(value);
                            break;
                        case "organization":
                            organizations.add(value);
                            break;
                        case "place_of_residence":
                            placesOfResidences.add(value);
                            break;

                        case "tag":
                            tags.add(value);
                    }
				}

                List<String> genders = new ArrayList<>();
                genders.add(gender);

                Author author = new Author();
                author.setAuthorId(authorID);
                author.setFavorited(favorited);
                author.setGender(genders);
                author.setMembers(members);
                author.setBook_author(authorName);
                author.setPopularity(popularity);
                author.setRating(rating);
                author.setReviews(reviews);
                author.setAwards_an_honors(awardsAndHonors);
                author.setBirthplace(birthPlaces);
                author.setCountry(country);
                author.setDate_of_birth(dateOfBirth);
                author.setNationality(nationality);
                author.setOccupations(occupations);
                author.setOrganizations(organizations);
                author.setPlaces_of_residence(placesOfResidences);

                author.setTags(tags);

                collector.emit(new Values(book.getAsin(), author));
                //collector.emit(input, new Values(book.getAsin(), author));
			}
			collector.ack(input);

		}catch(Exception ex){
			ex.printStackTrace();
			logger.warn(ex.getMessage());
            collector.fail(input);
		}
	}

	@Override
	public void cleanup() {
		try {
			conn.close();
		}
		catch (SQLException e){
			logger.error(e.getMessage());
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("asin", "author"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
