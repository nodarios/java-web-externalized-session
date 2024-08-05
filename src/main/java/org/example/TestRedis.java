package org.example;

import redis.clients.jedis.*;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

public class TestRedis {

    public static void main(String[] args) {
        try {
            // Load CA certificate
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = TestRedis.class.getClassLoader().getResourceAsStream("ca.crt");
            X509Certificate caCert = (X509Certificate) cf.generateCertificate(caInput);

            // Initialize SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, null);
            ks.setCertificateEntry("caCert", caCert);

            // Set up TrustManager
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Configure Jedis pool with SSL
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            String host = "127.0.0.1";
            int port = 6379;
            int timeout = 2000;
            String password = "yourpassword";
            int database = 0;
            boolean ssl = true;

            JedisPool jedisPool = new JedisPool(poolConfig, host, port, timeout, password, database, null, ssl, sslSocketFactory, null, null);

            // Test the connection
            try (Jedis jedis = jedisPool.getResource()) {
                System.out.println("Connected to Redis with SSL: " + jedis.ping());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

