package com.newnius.code4storm.dblp;

import org.neo4j.driver.v1.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by newnius on 10/21/16.
 *
 */
public class Neo4jPool {
    private static Driver driver;

    public static int init(){
        try {
            return Neo4jPool.init(new URI("bolt://192.168.1.221"), AuthTokens.basic("neo4j", "123456"), Config.build().withMaxIdleSessions(50).toConfig());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int init(URI url, AuthToken authToken, Config config){
        if(driver ==null) {
            driver = GraphDatabase.driver(url, authToken, config);
            return 1;
        }
        return 0;
    }

    public static Session getSession(){
        return driver.session();
    }

}
