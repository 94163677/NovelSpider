package air.kanna.spider.syosetu.spider.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import air.kanna.spider.syosetu.spider.SourceParser;
import air.kanna.spider.syosetu.spider.util.Logger;
import air.kanna.spider.syosetu.spider.util.LoggerProvider;

public class HtmlSourceParser implements SourceParser {
	private static final Logger logger = LoggerProvider.getLogger(HtmlSourceParser.class);
	
	@Override
	public Document parseSourceData(String source, String... params) {
		if(source == null || source.length() <= 0){
			throw new NullPointerException("Source is null");
		}
		
		try{
			return Jsoup.parse(source);
		}catch(Exception e){
			logger.error("parse to html Document error: " + source, e);
		}
		
		return null;
	}
}
