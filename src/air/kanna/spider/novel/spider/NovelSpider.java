package air.kanna.spider.novel.spider;

import java.util.List;

import air.kanna.spider.novel.exception.NovelParseException;
import air.kanna.spider.novel.model.Novel;

public interface NovelSpider {
	String getCharset();
	String getBaseUrl();
	List<Novel> getNovel()throws NovelParseException;
}
