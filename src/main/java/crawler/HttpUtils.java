package crawler;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Author: stk
 * Date: 4/15/17
 * Time: 2:59 PM
 */
public class HttpUtils {
    private static Logger logger = Logger.getLogger(HttpUtils.class);

    public static String sendGet(String url) {
        String result = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
            } else {
                logger.warn("Http warn: response not 200: " + url);
                System.out.println("Http warn: response not 200: " + url);
            }
            response.close();
            httpClient.close();
        } catch (IOException e) {
            logger.error("Http error: " + url, e);
            System.out.println("Http error: " + url);
            e.printStackTrace();
        }
        return result;
    }

    public static String[] threadGet(List<String> urls) {
        String[] results = new String[urls.size()];
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        try (CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build()) {
            GetThread[] getThreads = new GetThread[urls.size()];
            for (int i = 0; i < urls.size(); i++) {
                HttpGet httpGet = new HttpGet(urls.get(i));
                getThreads[i] = new GetThread(httpClient, httpGet, i + 1);
            }
            for (GetThread gt : getThreads) gt.start();
            for (GetThread gt : getThreads) gt.join();
            for (int i = 0; i < urls.size(); i++) results[i] = getThreads[i].call();
        } catch (Exception e) {
            logger.error("Thread GET error.", e);
            e.printStackTrace();
        }
        return results;
    }

    static class GetThread extends Thread implements Callable<String> {
        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;
        private final int id;
        private String result = null;

        public GetThread(CloseableHttpClient httpClient, HttpGet httpget, int id) {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            this.httpget = httpget;
            this.id = id;
        }

        public void run() {
            try (CloseableHttpResponse response = httpClient.execute(httpget, context)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    result = EntityUtils.toString(response.getEntity());
                } else {
                    logger.warn("Http warn: response not 200: [Thread " + id + "]: " + httpget.getURI());
                    System.out.println("Http warn: response not 200: [Thread " + id + "]: " + httpget.getURI());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String call() {
            return result;
        }
    }
}
