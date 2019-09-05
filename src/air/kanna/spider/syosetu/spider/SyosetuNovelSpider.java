package air.kanna.spider.syosetu.spider;

import java.util.List;

import air.kanna.spider.syosetu.model.SyosetuNovel;
import air.kanna.spider.syosetu.spider.exception.SyosetuNovelParseException;

public interface SyosetuNovelSpider {

	public static final String CHARSET = "UTF-8";
	public static final String ENTER = ((char)0xd) + "" + ((char)0xa);
	public static final String MAIN_URL = "https://ncode.syosetu.com/$1";
	
	public static final String DEFAULT_CHAPTER_TITLE = "DEFAULT TITLE";
	
	public List<SyosetuNovel> getSyosetuNovel()throws SyosetuNovelParseException;
}
