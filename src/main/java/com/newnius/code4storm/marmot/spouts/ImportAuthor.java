package com.newnius.code4storm.marmot.spouts;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newnius.code4storm.marmot.model.Author;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;


public class ImportAuthor implements IRichSpout {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3543153685348487418L;

	private SpoutOutputCollector collector;
	private BufferedReader br;

	private PreparedStatement ps;

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ImportAuthor.class);

	private boolean completed = false;

	@SuppressWarnings("rawtypes")
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		try {
			br= new BufferedReader(new FileReader(conf.get("jsonfile").toString()));

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://db_mysql/book?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root", "123456");
			String sql = "insert into used_author_property(`asin`, `key`, `value`) values(?, ?, ?)";
			ps = conn.prepareStatement(sql);
		}catch (Exception ex){
			logger.error("database connection error_"+ex.getMessage());
		}
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
        logger.info("ahahaha");
		if(completed){
			try {
				Thread.sleep(5000);
				return ;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return ;
		}
        completed = true;
		try {
			String json = "";
			String str;
			while( (str=br.readLine())!=null ){
				json+=str;
			}

			List<Author> authors = new Gson().fromJson(json, new TypeToken<List<Author>>(){}.getType());

			for(Author author: authors){

                if(author.getBook_name() != null) {
                    //title
                    ps.setString(1, author.getBook_id().get(0));
                    ps.setString(2, "title");
                    ps.setString(3, author.getBook_name());
                    ps.execute();
                }

                //place of residents
                if(author.getPlaces_of_residence() != null) {
                    for (String place : author.getPlaces_of_residence()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "place_of_residence");
                        ps.setString(3, place);
                        ps.execute();
                    }
                }

                //date of birth
                if(author.getDate_of_birth() != null) {
                    for (String date : author.getDate_of_birth()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "date_of_birth");
                        ps.setString(3, date);
                        ps.execute();
                    }
                }

                //reviews
                if(author.getReviews() != null) {
                    ps.setString(1, author.getBook_id().get(0));
                    ps.setString(2, "reviews");
                    ps.setString(3, author.getReviews());
                    ps.execute();
                }

                //members
                if(author.getMembers() != null) {
                    ps.setString(1, author.getBook_id().get(0));
                    ps.setString(2, "members");
                    ps.setString(3, author.getMembers());
                    ps.execute();
                }

                //education
                if(author.getEducation() != null) {
                    for (String education : author.getEducation()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "education");
                        ps.setString(3, education);
                        ps.execute();
                    }
                }

                //date of death
                if(author.getDate_of_death() != null) {
                    for (String date : author.getDate_of_death()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "date_of_death");
                        ps.setString(3, date);
                        ps.execute();
                    }
                }

                //nationality
                if(author.getNationality() != null) {
                    for (String nationality : author.getNationality()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "nationality");
                        ps.setString(3, nationality);
                        ps.execute();
                    }
                }

                //favorited
                if(author.getFavorited() != null) {
                    ps.setString(1, author.getBook_id().get(0));
                    ps.setString(2, "favorited");
                    ps.setString(3, author.getFavorited());
                    ps.execute();
                }


                //relationships
                if(author.getRelationships() != null) {
                    for (String relationship : author.getRelationships()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "relationship");
                        ps.setString(3, relationship);
                        ps.execute();
                    }
                }

                //country
                if(author.getCountry() != null) {
                    for (String country : author.getCountry()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "country");
                        ps.setString(3, country);
                        ps.execute();
                    }
                }

                //place of death
                if(author.getPlace_of_death() != null) {
                    for (String place : author.getPlace_of_death()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "place_of_death");
                        ps.setString(3, place);
                        ps.execute();
                    }
                }

                //tags
                if(author.getTags() != null) {
                    for (String tag : author.getTags()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "tag");
                        ps.setString(3, tag);
                        ps.execute();
                    }
                }

                //burial location
                if(author.getBurial_location() != null) {
                    for (String location : author.getBurial_location()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "burial_location");
                        ps.setString(3, location);
                        ps.execute();
                    }
                }

                //short biography
                if(author.getShort_biography() != null) {
                    for (String biography : author.getShort_biography()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "short_biography");
                        ps.setString(3, biography);
                        ps.execute();
                    }
                }

                //rating
                if(author.getRating() != null) {
                    ps.setString(1, author.getBook_id().get(0));
                    ps.setString(2, "rating");
                    ps.setString(3, author.getRating());
                    ps.execute();
                }

                //author name
                if(author.getBook_author() != null) {
                    ps.setString(1, author.getBook_id().get(0));
                    ps.setString(2, "name");
                    ps.setString(3, author.getBook_author());
                    ps.execute();
                }

                //birth place
                if(author.getBirthplace() != null) {
                    for (String place : author.getBirthplace()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "birth_place");
                        ps.setString(3, place);
                        ps.execute();
                    }
                }

                //awards and honors
                if(author.getAwards_an_honors() != null) {
                    for (String award : author.getAwards_an_honors()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "awards_and_honors");
                        ps.setString(3, award);
                        ps.execute();
                    }
                }

                //legal name
                if(author.getLegal_name() != null) {
                    for (String name : author.getLegal_name()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "legal_name");
                        ps.setString(3, name);
                        ps.execute();
                    }
                }

                //gender
                if(author.getGender() != null) {
                    for (String gender : author.getGender()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "gender");
                        ps.setString(3, gender);
                        ps.execute();
                    }
                }

                //organizations
                if(author.getOrganizations() != null) {
                    for (String organization : author.getOrganizations()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "organization");
                        ps.setString(3, organization);
                        ps.execute();
                    }
                }

                //popularity
                if(author.getPopularity() != null) {
                    ps.setString(1, author.getBook_id().get(0));
                    ps.setString(2, "popularity");
                    ps.setString(3, author.getPopularity());
                    ps.execute();
                }

                //events
                if(author.getEvents() != null) {
                    ps.setString(1, author.getBook_id().get(0));
                    ps.setString(2, "events");
                    ps.setString(3, author.getEvents());
                    ps.execute();
                }

                //canonical name
                if(author.getCanonical_name() != null) {
                    for (String canonicalName : author.getCanonical_name()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "canonical_name");
                        ps.setString(3, canonicalName);
                        ps.execute();
                    }
                }

                //occupations
                if(author.getOccupations() != null) {
                    for (String occupation : author.getOccupations()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "occupation");
                        ps.setString(3, occupation);
                        ps.execute();
                    }
                }

                //other names
                if(author.getOther_names() != null) {
                    for (String otherName : author.getOther_names()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "other_name");
                        ps.setString(3, otherName);
                        ps.execute();
                    }
                }

                //agents
                if(author.getAgents() != null) {
                    for (String agent : author.getAgents()) {
                        ps.setString(1, author.getBook_id().get(0));
                        ps.setString(2, "agent");
                        ps.setString(3, agent);
                        ps.execute();
                    }
                }

            }

			ps.close();
		}catch (Exception ex){
			ex.printStackTrace();
            logger.warn(ex.getMessage());
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
