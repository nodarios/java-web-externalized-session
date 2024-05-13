package org.example.redis;

import redis.clients.jedis.Jedis;

public final class RedisConfig {

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private static Jedis jedis = null;

    private RedisConfig() {}

    // TODO use connection pool here
    public static Jedis getConnection() {
        if (jedis == null) {
            jedis =  new Jedis(REDIS_HOST, REDIS_PORT);
        }
        return jedis;
    }

}
