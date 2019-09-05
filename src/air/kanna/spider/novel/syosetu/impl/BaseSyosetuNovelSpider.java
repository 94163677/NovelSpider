package air.kanna.spider.novel.syosetu.impl;

import air.kanna.spider.novel.spider.SourceDataGetter;
import air.kanna.spider.novel.spider.SourceParser;
import air.kanna.spider.novel.spider.NovelSpider;

public abstract class BaseSyosetuNovelSpider implements NovelSpider {
	protected SourceDataGetter srcGetter;
	protected SourceParser srcParser;
	
	protected String novelId;
	
	
	protected String check() {
		if(srcGetter == null){
			throw new NullPointerException("SourceDataGetter is null");
		}
		if(srcParser == null){
			throw new NullPointerException("SourceParser is null");
		}

		if(novelId == null || novelId.length() <= 0){
			return "Novel Id is null";
		}

		return null;
	}

	public SourceDataGetter getSrcGetter() {
		return srcGetter;
	}

	public void setSrcGetter(SourceDataGetter srcGetter) {
		this.srcGetter = srcGetter;
	}

	public SourceParser getSrcParser() {
		return srcParser;
	}

	public void setSrcParser(SourceParser srcParser) {
		this.srcParser = srcParser;
	}

	public String getNovelId() {
		return novelId;
	}

	public void setNovelId(String novelId) {
		this.novelId = novelId;
	}

}
