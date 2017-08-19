package com.newnius.code4storm.marmot.util;

import com.newnius.code4storm.marmot.TopoConfig;
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
            return Neo4jPool.init(new URI(TopoConfig.NEO4J_URL), AuthTokens.basic(TopoConfig.NEO4J_USER, TopoConfig.NEO4J_PASS), Config.build().withMaxIdleSessions(TopoConfig.NEO4J_POOL_SIZE).toConfig());
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
