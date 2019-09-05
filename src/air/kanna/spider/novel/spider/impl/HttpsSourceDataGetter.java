package air.kanna.spider.novel.spider.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import air.kanna.spider.novel.spider.SourceDataGetter;
import air.kanna.spider.novel.util.StringUtil;
import air.kanna.spider.novel.util.Timer;
import air.kanna.spider.novel.util.log.Logger;
import air.kanna.spider.novel.util.log.LoggerProvider;

public class HttpsSourceDataGetter implements SourceDataGetter {
	private static final Logger logger = LoggerProvider.getLogger(HttpsSourceDataGetter.class);
	
	public static final int DEFAULT_RETRY = 3;
	
	private int retryTime = DEFAULT_RETRY;
	
	@Override
	public String getSourceData(String url, String charset, String... params) {
		if(url == null || url.length() <= 0){
			throw new NullPointerException("URL is null");
		}
		if(charset == null || charset.length() <= 0){
			throw new NullPointerException("CharSet is null");
		}
		if(!url.toLowerCase().startsWith("https")){
			throw new IllegalArgumentException("URL is not a https url");
		}
		
		for(int i=1; i<=retryTime; i++) {
    		try{
    			URL webUrl = new URL(url);
    			HttpsURLConnection conn = (HttpsURLConnection)webUrl.openConnection();
    			SSLSocketFactory ssl = AllTrustX509TrustManager.getSSLSocketFactory();
    			StringBuilder result = new StringBuilder();
    			
    			conn.setDoOutput(true);
    			conn.setDoInput(true);
    			
    			conn.setSSLSocketFactory(ssl);
    			conn.setRequestProperty("accept", "*/*");
    			conn.setRequestProperty("connection", "Keep-Alive");
    			conn.setRequestProperty("Content-Type", "application/json;charset=" + charset);
    			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
    			
    			conn.connect();
    			
    			BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
    			
    			for(String temp = read.readLine(); temp != null; temp = read.readLine()){
    				result.append(temp).append(StringUtil.ENTER);
    			}
    			
    			return result.toString();
    		}catch(Exception e){
    			logger.error("Cannot get Data from Url at " + i + "time: " + url, e);
    			try{
    	            Thread.sleep(Timer.getWaitingTime());
    	        }catch(Exception ee){
    	            ee.printStackTrace();
    	        }
    		}
		}
		
		return null;
	}
}
