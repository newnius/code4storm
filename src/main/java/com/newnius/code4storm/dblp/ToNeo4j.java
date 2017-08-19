package com.newnius.code4storm.dblp;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by newnius on 4/26/17.
 *
 */
public class ToNeo4j {
    public static void main(String[] args){
        //ToMysql.go();


        String[] files = {
                "sigmod", "icdt", "icde", "vldb",
                "kdd", "sdm", "icdma", "wsdm",
                "cvpr", "iccv", "kr", "ijcai",
                "mobicom", "sigcomm", "infocom", "sensys"

        };

        String[] fields = {"Database", "Data Miming", "AI", "Network"};


        Neo4jPool.init();
        int index = 0;
        /* loop 1: create nodes */
        for (String file : files) {
            List<Inproceedings>  inproceedingss = getList("/mnt/dblp/"+file);
            System.out.println(file+": "+inproceedingss.size());
            createAuthors(inproceedingss);
            createPaper(inproceedingss);
            createConf(fields[index/4], file);
            index++;
        }

        /* loop 2: create relationships */
        for (String file : files) {
            List<Inproceedings>  inproceedingss = getList("/mnt/dblp/"+file);
            System.out.println(file+": "+inproceedingss.size());
            linkAP(inproceedingss);
            linkPP(inproceedingss);
            linkPC(file, inproceedingss);
        }

        longSleep();
    }

    public static List<Inproceedings> getList(String filepath){
        List<Inproceedings>  inproceedingss = new ArrayList<>();
        try {
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(new FileInputStream(filepath));

            Element root = doc.getRootElement();
            List list = root.getChildren("inproceedings");
            for (Object aList : list) {
                Element element = (Element) aList;
                String mdate = element.getAttributeValue("mdate");
                String key = element.getAttributeValue("key");
                List<String> authors = new ArrayList<>();
                Map<String, String> cites = new HashMap<>();
                List<Element> authorsEle = element.getChildren("author");
                for (Element author: authorsEle){
                    authors.add(author.getText());
                }
                List<Element> citesEle = element.getChildren("cite");
                for (Element cite: citesEle){
                    String label = cite.getAttributeValue("label");
                    String value = cite.getText();
                    if(!value.equals("...")) {
                        cites.put(label, value);
                    }
                }
                String title = element.getChildText("title");
                String pages = element.getChildText("pages");
                String year = element.getChildText("year");
                String bookTitle = element.getChildText("bookTitle");
                String ee = element.getChildText("ee");// actually is list
                String crossRef = element.getChildText("crossref");
                String url = element.getChildText("url");
                String cdrom = element.getChildText("cdrom");

                Inproceedings inproceedings = new Inproceedings();
                inproceedings.setMdate(mdate);
                inproceedings.setKey(key);
                inproceedings.setAuthors(authors);
                inproceedings.setCites(cites);
                inproceedings.setTitle(title);
                inproceedings.setPages(pages);
                inproceedings.setYear(year);
                inproceedings.setBookTitle(bookTitle);
                inproceedings.setEe(ee);
                inproceedings.setCrossRef(crossRef);
                inproceedings.setUrl(url);
                inproceedings.setCdrom(cdrom);

                inproceedingss.add(inproceedings);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return inproceedingss;
    }


    public static void createAuthors(List<Inproceedings> inproceedingss){
        for (Inproceedings inproceedings: inproceedingss){
            try (Session session = Neo4jPool.getSession()) {
                for (String author : inproceedings.getAuthors()) {
                    session.run("CREATE ( :Author { name:{name} } )",
                            Values.parameters(
                                    "name", author
                            ));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void createPaper(List<Inproceedings> inproceedingss){
        for (Inproceedings inproceedings: inproceedingss){
            try (Session session = Neo4jPool.getSession()) {
                session.run("CREATE ( :Paper { key:{key}, year:{year}, title:{title}, url:{url} } )",
                    Values.parameters(
                            "key", inproceedings.getKey(),
                            "year", inproceedings.getYear(),
                            "title", inproceedings.getTitle(),
                            "url", inproceedings.getUrl()
                    ));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }


    public static void linkAP(List<Inproceedings> inproceedingss){
        for (Inproceedings inproceedings: inproceedingss) {
            for (String author: inproceedings.getAuthors()) {
                try (Session session = Neo4jPool.getSession()) {
                    session.run(
                        "MATCH (author:Author {name:{name}})\n"
                                + "WITH author\n"
                                + "Match (paper:Paper {key:{key}})\n"
                                + "CREATE UNIQUE (author)-[:WRITE]->(paper)"
                        ,
                        Values.parameters(
                                "name", author,
                                "key", inproceedings.getKey()
                                ));
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

        }
    }

    public static void linkPP(List<Inproceedings> inproceedingss){
        for (Inproceedings inproceedings: inproceedingss) {
            Map<String, String> citesMap = inproceedings.getCites();
            for (Map.Entry<String, String> entry: citesMap.entrySet()) {
                try (Session session = Neo4jPool.getSession()) {
                    session.run(
                        "MATCH (paper1:Paper {key:{key1}})\n"
                                + "WITH paper1\n"
                                + "Match (paper2:Paper {key:{key2}})\n"
                                + "CREATE UNIQUE (paper1)-[:CITE]->(paper2)"
                        ,
                        Values.parameters(
                                "key1", inproceedings.getKey(),
                                "key2", entry.getValue()
                        ));
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void linkPC(String conf, List<Inproceedings> inproceedingss){
        for (Inproceedings inproceedings: inproceedingss) {
            try (Session session = Neo4jPool.getSession()) {
                session.run(
                    "MATCH (paper:Paper {key:{key}})\n"
                            + "WITH paper\n"
                            + "Match (conf:Conf {name:{name}})\n"
                            + "CREATE UNIQUE (paper)-[:BELONG]->(conf)"
                    ,
                    Values.parameters(
                            "key", inproceedings.getKey(),
                            "name", conf
                    ));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static void createConf(String field, String name){
        try (Session session = Neo4jPool.getSession()) {
            session.run("CREATE ( :Conf { name:{name}, field:{field} } )",
                    Values.parameters(
                            "name", name,
                            "field", field
                    ));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public static void longSleep(){
        while(true){
            try {
                System.out.println("Finished");
                Thread.sleep(3600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
