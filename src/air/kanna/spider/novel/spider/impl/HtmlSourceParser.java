package air.kanna.spider.novel.spider.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import air.kanna.spider.novel.spider.SourceParser;
import air.kanna.spider.novel.util.log.Logger;
import air.kanna.spider.novel.util.log.LoggerProvider;

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
