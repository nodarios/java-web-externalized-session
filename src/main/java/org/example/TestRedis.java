package org.example;

import redis.clients.jedis.*;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.*;

public class TestRedis {

    public static void main(String[] args) {
        try {
            SSLSocketFactory sslSocketFactory = getSslSocketFactory();

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

    private static SSLSocketFactory getSslSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            //CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //// make sure file exists
            //InputStream caInput = TestRedis.class.getClassLoader().getResourceAsStream("ca.crt");
            //X509Certificate caCert = (X509Certificate) cf.generateCertificate(caInput);
            //
            //KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            //ks.load(null, null);
            //ks.setCertificateEntry("caCert", caCert);
            //
            //TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            //tmf.init(ks);

            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            //sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
            sslContext.init(null, trustAllCerts, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            //LOGGER.error("error initialization ssl context", e);
            System.out.println("error initialization ssl context" + e);
        }
        return sslSocketFactory;
    }

}

