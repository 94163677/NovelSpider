package air.kanna.spider.novel.spider;

import java.util.List;

import air.kanna.spider.novel.exception.NovelParseException;
import air.kanna.spider.novel.model.Novel;

public interface NovelSpider {

	public static final String CHARSET = "UTF-8";
	public static final String ENTER = ((char)0xd) + "" + ((char)0xa);
	public static final String MAIN_URL = "https://ncode.syosetu.com/$1";
	
	public static final String DEFAULT_CHAPTER_TITLE = "DEFAULT TITLE";
	
	public List<Novel> getSyosetuNovel()throws NovelParseException;
}
