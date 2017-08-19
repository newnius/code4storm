package com.newnius.code4storm.dblp;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.FileInputStream;
import java.util.*;

/**
 * Created by newnius on 5/18/17.
 *
 */
public class ToMysql {
    public static void main(String[] args) {
        String confFile = "dbcp.properties";
        String configFile = confFile;
        Properties dbProperties = new Properties();
        try {
            dbProperties.load(CRDAO.class.getClassLoader().getResourceAsStream(configFile));
            CRDAO.init(dbProperties);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        String[] confs = {
                "sigmod", "icdt", "icde", "vldb",
                "kdd", "sdm", "icdma", "wsdm",
                "cvpr", "iccv", "kr", "ijcai",
                "mobicom", "sigcomm", "infocom", "sensys"

        };


        int index = 0;
        /* loop 1: create nodes */
        for (String conf : confs) {
            List<Inproceedings> inproceedingss = getList("/mnt/dblp/" + conf);
            //List<Inproceedings> inproceedingss = getList("/home/newnius/Downloads/dblp/" + conf);
            System.out.println(conf + ": " + inproceedingss.size());
            createAuthorWritePaper(inproceedingss);
            createPaper(conf, inproceedingss);
            index++;
        }

        while(true){
            try {
                System.out.println("Finished");
                Thread.sleep(3600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    public static void createAuthorWritePaper(List<Inproceedings> inproceedingss){
        /*
        CREATE TABLE `author_write_paper`(
            `author` varchar(256),
             INDEX(`author`),
            `paper` varchar(256) not null,
             INDEX(`paper`),
             UNIQUE(`author`, `paper`)
        )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;
        * */



        String sql = "INSERT INTO `author_write_paper` (`author`, `paper`) VALUES (?, ?)";
        for (Inproceedings inproceedings: inproceedingss) {
            for (String author : inproceedings.getAuthors()) {
                String[] params = {author, inproceedings.getKey()};
                try {
                    CRDAO dao = CRDAO.getInstance();
                    dao.executeUpdate(sql, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createPaper(String conf, List<Inproceedings> inproceedingss){
        /*
        CREATE TABLE `paper`(
            `key` varchar(256) PRIMARY KEY,
            `year` varchar(64),
            `title` varchar(256),
            `url` varchar(256),
            `conf` varchar(64),
             INDEX(`conf`)
        )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;
        * */

        String sql = "INSERT INTO `paper` (`key`, `year`, `title`, `url`, `conf`) VALUES (?, ?, ?, ?, ?)";
        for (Inproceedings paper: inproceedingss) {
            String[] params = {paper.getKey(), paper.getYear(), paper.getTitle(), paper.getUrl(), conf};
            try {
                CRDAO dao = CRDAO.getInstance();
                dao.executeUpdate(sql, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
