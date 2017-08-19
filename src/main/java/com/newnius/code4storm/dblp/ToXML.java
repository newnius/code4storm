package com.newnius.code4storm.dblp;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Created by newnius on 5/25/17.
 *
 */
public class ToXML {
    public static void main(String[] args){
        String configFile = "dbcp.properties";
        Properties dbProperties = new Properties();
        try {
            dbProperties.load(CRDAO.class.getClassLoader().getResourceAsStream(configFile));
            CRDAO.init(dbProperties);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }


        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("input.xml")));
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<PROX3DB>\n");
            bw.write("  <OBJECTS>\n");

            //object
            CRDAO dao = CRDAO.getInstance();
            dao.setAutoCommit(false);
            String sql = "SELECT * FROM `xml_id` ORDER BY `id`";
            String[] params = {};
            ResultSet rs = dao.executeQuery(sql, params);
            while(rs.next()){
                int id = rs.getInt("id");
                String name= rs.getString("name");
                bw.write("    <OBJECT ID=\""+id+"\"/>\n");
            }
            dao.commit();
            rs.close();
            bw.write("  </OBJECTS>\n");

            int cnt=1;

            bw.write("  <LINKS>\n");
            //links
            dao = CRDAO.getInstance();
            dao.setAutoCommit(false);
            sql = "SELECT `key`,`conf` FROM `paper` WHERE `key` IN (SELECT `name` FROM xml_id)";
            rs = dao.executeQuery(sql, params);
            while(rs.next()){
                String key = rs.getString("key");
                String conf= rs.getString("conf");
                int paperID = getID(key);
                int confID = getID(conf);
                if(paperID !=0 &&confID!=0){
                    bw.write("    <LINK ID=\""+cnt+"\" O1-ID=\""+paperID+"\" O2-ID=\""+confID+"\"/>\n");
                    cnt++;
                }
            }
            dao.commit();
            rs.close();

            dao = CRDAO.getInstance();
            dao.setAutoCommit(false);
            sql = "SELECT * FROM `author_write_paper` WHERE `paper` IN (SELECT `name` FROM `xml_id`)";
            rs = dao.executeQuery(sql, params);
            while(rs.next()){
                String author = rs.getString("author");
                String paper= rs.getString("paper");
                int authorID = getID(author);
                int paperID = getID(paper);
                if(paperID !=0 &&authorID!=0){
                    bw.write("    <LINK ID=\""+cnt+"\" O1-ID=\""+authorID+"\" O2-ID=\""+paperID+"\"/>\n");
                    cnt++;
                }
            }
            dao.commit();
            rs.close();
            bw.write("  </LINKS>\n");

            bw.write("  <ATTRIBUTES>\n");
            bw.write("    <ATTRIBUTE NAME=\"object-type\">\n");
            //attr
            dao = CRDAO.getInstance();
            dao.setAutoCommit(false);
            sql = "SELECT * FROM `xml_id` ORDER BY `id`";
            rs = dao.executeQuery(sql, params);
            while(rs.next()){
                int id = rs.getInt("id");
                bw.write("      <ATTR-VALUE ITEM-ID=\""+id+"\">\n");
                if(id<=16){//conf
                    bw.write("        <COL-VALUE>proceeding</COL-VALUE>\n");
                }else if(id<=16+1244){//author
                    bw.write("        <COL-VALUE>person</COL-VALUE>\n");
                }else{//paper
                    bw.write("        <COL-VALUE>paper</COL-VALUE>\n");
                }
                bw.write("      </ATTR-VALUE>\n");
            }
            dao.commit();
            rs.close();
            bw.write("    </ATTRIBUTE>\n");

            bw.write("    <ATTRIBUTE NAME=\"conference\">\n");
            dao = CRDAO.getInstance();
            dao.setAutoCommit(false);
            sql = "SELECT * FROM `xml_id` ORDER BY `id`";
            rs = dao.executeQuery(sql, params);
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                if(id<=16){//conf
                    bw.write("      <ATTR-VALUE ITEM-ID=\""+id+"\">\n");
                    bw.write("        <COL-VALUE>"+name+"</COL-VALUE>\n");
                    bw.write("      </ATTR-VALUE>\n");
                }
            }
            dao.commit();
            rs.close();
            bw.write("    </ATTRIBUTE>\n");


            bw.write("    <ATTRIBUTE NAME=\"name\">\n");
            dao = CRDAO.getInstance();
            dao.setAutoCommit(false);
            sql = "SELECT * FROM `xml_id` ORDER BY `id`";
            rs = dao.executeQuery(sql, params);
            while(rs.next()){
                int id = rs.getInt("id");
                String name= rs.getString("name");
                if(id>16 && id<=16+1244){//author
                    bw.write("      <ATTR-VALUE ITEM-ID=\""+id+"\">\n");
                    bw.write("        <COL-VALUE>"+name+"</COL-VALUE>\n");
                    bw.write("      </ATTR-VALUE>\n");
                }
            }
            dao.commit();
            rs.close();
            bw.write("    </ATTRIBUTE>\n");

            bw.write("  </ATTRIBUTES>\n");
            bw.write("</PROX3DB>");

            bw.close();

            Main.sleep();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int getID(String name){
        int id=0;
        try {
            CRDAO dao = CRDAO.getInstance();
            dao.setAutoCommit(false);
            String sql = "SELECT `id` FROM `xml_id` WHERE `name` = ?";
            String[] params = {name};
            ResultSet rs = dao.executeQuery(sql, params);
            while (rs.next()) {
                id = rs.getInt("id");
            }
            dao.commit();
            rs.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return id;
    }
}
