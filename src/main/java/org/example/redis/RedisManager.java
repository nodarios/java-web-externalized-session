package org.example.redis;

import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Map;

public final class RedisManager {

    private static final int SESSION_EXPIRATION_SECONDS = 120;

    private RedisManager() {}

    public static void setSessionAttribute(String sessionId, String key, String value) {
        try (Jedis jedis = RedisConfig.getConnection()) {
            jedis.hset(sessionId, key, value);
            jedis.expire(sessionId, SESSION_EXPIRATION_SECONDS);
            System.out.println("new expiry date: " + new Date(jedis.pexpireTime(sessionId)));
        }
    }

    public static String getSessionAttribute(String sessionId, String key) {
        try (Jedis jedis = RedisConfig.getConnection()) {
            return jedis.hget(sessionId, key);
        }
    }

    public static Map<String, String> getSessionAttributes(String sessionId) {
        try (Jedis jedis = RedisConfig.getConnection()) {
            return jedis.hgetAll(sessionId);
        }
    }

    public static boolean sessionExists(String sessionId) {
        boolean sessionExists;
        try (Jedis jedis = RedisConfig.getConnection()) {
            sessionExists = jedis.hlen(sessionId) != 0;
        }
        return sessionExists;
    }

    public static void invalidateSession(String sessionId) {
        try (Jedis jedis = RedisConfig.getConnection()) {
            jedis.del(sessionId);
        }
    }

}
