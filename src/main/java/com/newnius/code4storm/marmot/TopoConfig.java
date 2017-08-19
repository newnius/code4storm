package com.newnius.code4storm.marmot;

public class TopoConfig {
	public static final String TOPOLOGY_NAME = "StreamSpider";

	public static String redis_host = "redis";

	// public static String redis_host = "192.168.56.110";

	public static int redis_port = 6379;

	// public static String mongodb_host = "192.168.56.110";

	public static String mongodb_host = "mongodb";

	public static int mongodb_port = 27017;

	public static String mysql_host = "mysql";

	public static int mysql_port = 3306;

	public static final String PATTERN_URL = "https?://\\S+";// "https?://[-_/:.a-zA-Z0-9]+";

	public static final int DEFAULT_PAGE_CACHE_SECOND = 2 * 60;

	public static final long PATTERNS_CACHE_MILLISECOND = 1 * 60 * 1000;

	public static final int LIMITATION_RESET_INTERVAL = 5 * 60;

	public static final int DEFAULT_LIMITATION = -1;

	public static final int DEFAULT_FREQUENCY = 10 * 60;// ;1 * 60 * 60;

	public static final int PRIORITY_LOWEST = 1;

	public static final int PRIORITY_HIGHEST = 5;


	public static final String NEO4J_URL = "bolt://neo4j";
	public static final String NEO4J_USER = "neo4j";
	public static final String NEO4J_PASS = "123456";
	public static final int NEO4J_POOL_SIZE = 50;

}
