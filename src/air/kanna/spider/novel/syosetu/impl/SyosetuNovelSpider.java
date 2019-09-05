package air.kanna.spider.novel.syosetu.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import air.kanna.spider.novel.exception.NovelParseException;
import air.kanna.spider.novel.model.NovelChapter;
import air.kanna.spider.novel.model.NovelSection;
import air.kanna.spider.novel.spider.impl.BaseNovelSpider;
import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.util.StringUtil;
import air.kanna.spider.novel.util.log.Logger;
import air.kanna.spider.novel.util.log.LoggerProvider;

public class SyosetuNovelSpider extends BaseNovelSpider {
	private static final Logger logger = LoggerProvider.getLogger(SyosetuNovelSpider.class);
	private static final String MAIN_URL = "https://ncode.syosetu.com/$1";
	
	private String url = null;
	private String source = null;
	
	@Override
	public String getMainUrl() {
	    return MAIN_URL;
	}
	
	@Override
	public List<Novel> getNovel() throws NovelParseException{
		String checked = check();
		List<Novel> result = new ArrayList<Novel>();
		
		if(checked != null){
			throw new NovelParseException("Check error: " + checked);
		}
		
		Document html = getDocument();
		if(html == null){
			throw new NovelParseException("Cannot Parse Source", url, source);
		}
		
		Novel novel = getSyosetuNovel(html);
		if(novel != null){
			result.add(novel);
		}
		
		logger.info("Parse Novel Information success: " + novel.toString());
		
		return result;
	}
	
	private Novel getSyosetuNovel(Document html)throws NovelParseException{
		Novel novel = new Novel();

		novel.setNovelId(novelId);
		novel.setNovelTitle(getStringFromElements(html.getElementsByClass("novel_title"), "Title"));
		novel.setAuthor(getStringFromElements(html.select(".novel_writername a"), "Author"));
		novel.setSummary(getStringFromElements(html.getElementById("novel_ex"), "Summary"));
		try{
		    novel.setDownloadId(getDownloadId(html.select("#novel_footer a")));
		}catch(Exception e) {
		    logger.warn("Cannot get Download Id in " + novel.getNovelId());
		}
		
		novel.getNovelContent().addAll(getNovelChapter(html));
		return novel;
	}
	
	private List<NovelChapter> getNovelChapter(Document html)throws NovelParseException{
		Elements list = html.getElementsByClass("index_box");
		if(list == null || list.size() <= 0){
			throw new NovelParseException(("Cannot get Novel Chapter Element"), url, source);
		}
		
		list = list.first().children();
		if(list == null || list.size() <= 0){
			throw new NovelParseException(("Cannot get Novel Chapter Element"), url, source);
		}
		
		List<NovelChapter> chapters = new ArrayList<NovelChapter>();
		Elements chaptersElem = html.select(".chapter_title");
		NovelChapter current = null;
		boolean hasChapter = (chaptersElem != null && chaptersElem.size() > 0);
		int chpIndex = 1;
		
		if(!hasChapter){
			current = new NovelChapter();
			current.setChapterIndex(1);
			current.setChapterTitle(NovelChapter.DEFAULT_CHAPTER_TITLE);
		}
		for(Element item : list){
			if(item == null){
				continue;
			}
			if(hasChapter && item.attr("class").equalsIgnoreCase("chapter_title")){
				if(current != null){
					chapters.add(current);
				}
				current = new NovelChapter();
				current.setChapterIndex(chpIndex);
				current.setChapterTitle(StringUtil.trim(item.text()));
				chpIndex++;
			}
			if(item.attr("class").equalsIgnoreCase("novel_sublist2")){
				if(current == null){
					logger.warn("Getting NovelSection before getting NovelChapter: " + item.text());
					continue;
				}
				try{
					NovelSection section = getNovelSection(item);
					current.getChapterContent().add(section);
				}catch(Exception e){
					logger.warn("Parse NovelSection error", e);
				}
			}
		}
		
		if(current != null){
			chapters.add(current);
		}
		
		return chapters;
	}
	
	private NovelSection getNovelSection(Element item) throws NovelParseException{
		if(item == null || item.children().size() < 2){
			throw new NovelParseException("Element is null");
		}
		Elements childen = item.select("a");
		if(childen == null || childen.first() == null){
			throw new NovelParseException("Cannot get Section Title Element");
		}
		
		NovelSection section = new NovelSection();
		Element element = childen.first();
		String tempStr = element.text();
		
		//getSectionTitle
		if(tempStr == null || tempStr.length() <= 0){
			throw new NovelParseException("Cannot get Section Title String");
		}
		section.setSectionTitle(StringUtil.trim(tempStr));
		
		//getSectionNum
		tempStr = element.attr("href");
		if(tempStr == null || tempStr.length() <= 0){
			throw new NovelParseException("Cannot get Section Link String");
		}
		int idx = tempStr.lastIndexOf('/', tempStr.length() - 2);
		if(idx < 0 || idx >= (tempStr.length() - 1)){
			throw new NovelParseException("Cannot get Section Num String");
		}
		if(tempStr.endsWith("/")){
			tempStr = tempStr.substring(idx + 1, tempStr.length() - 1);
		}else{
			tempStr = tempStr.substring(idx + 1);
		}
		section.setSectionNum(tempStr);
		
		//getCreateTime and getUpdateTime
		element = item.child(1);
		childen = element.select("span");
		
		if(childen == null || childen.size() <= 0){
			//getCreateTime
			tempStr = element.text();
			if(tempStr != null && tempStr.length() >= 16){
				section.setCreateTime(tempStr.substring(0, 16));
			}
		}else{
			//getCreateTime
			tempStr = element.text();
			if(tempStr != null && tempStr.length() >= 16){
				section.setCreateTime(tempStr.substring(0, 16));
			}
			
			//getUpdateTime
			tempStr = childen.first().attr("title");
			if(tempStr != null && tempStr.length() > 0){
				idx = tempStr.lastIndexOf(' ');
				if(idx >= 0){
					tempStr = StringUtil.trim(tempStr.substring(0, idx));
				}else{
					tempStr = StringUtil.trim(tempStr);
				}
				section.setUpdateTime(tempStr);
			}
		}
		
		return section;
	}
	
	private String getDownloadId(Elements foot) throws NovelParseException{
		if(foot == null){
			throw new NovelParseException(("Cannot get Novel Download Element"), this.url, source);
		}
		
		Element download = null;
		for(Element link : foot){
			if(link == null){
				continue;
			}
			String url = link.attr("href");
			if(url == null || url.length() <= 0){
				continue;
			}
			
			if(url.toLowerCase().indexOf("txtdownload") >= 0){
				download = link;
				break;
			}
		}
		
		if(download == null){
			throw new NovelParseException(("Cannot get Novel Download Element"), this.url, source);
		}
		
		String url = download.attr("href");
		if(url == null || url.length() <= 0){
			throw new NovelParseException(("Cannot get Novel Download String"), this.url, source);
		}
		
		int idx = url.lastIndexOf('/', url.length() - 2);
		if(idx < 0 || (idx + 1) >= url.length()){
			throw new NovelParseException(("Cannot Parse Novel Download String"), this.url, source);
		}
		
		if(url.endsWith("/")){
			return url.substring(idx + 1, url.length() - 1);
		}else{
			return url.substring(idx + 1);
		}
	}
	
	private String getStringFromElements(Elements list, String title) throws NovelParseException{
		return getStringFromElements(list, 0, title);
	}
	
	private String getStringFromElements(Elements list, int idx, String title) throws NovelParseException{
		if(list == null){
			throw new NovelParseException(("Cannot get Novel " + title + " Element"), url, source);
		}
		if(idx < 0 || idx >= list.size()){
			throw new IndexOutOfBoundsException("Elements index error: " + idx + ", Max is: " + list.size());
		}
		
		return getStringFromElements(list.get(idx), title);
	}
	
	private String getStringFromElements(Element element, String title) throws NovelParseException{
		if(element == null){
			throw new NovelParseException(("Cannot get Novel " + title + " String"), url, source);
		}
		
		String elementStr = element.text();
		if(elementStr == null || elementStr.length() <= 0){
			throw new NovelParseException(("Cannot get Novel " + title + " String"), url, source);
		}
		
		return StringUtil.trim(elementStr);
	}

	private Document getDocument()throws NovelParseException{
		String url = getMainUrl().replace("$1", novelId);
		this.url = url;
		logger.info("Novel URL is: " + url);
		
		String source = srcGetter.getSourceData(url, getCharset());
		if(source == null || source.length() <= 0){
			throw new NovelParseException("Cannot get Source form url: " + url, url, null);
		}
		
		logger.info("Get Source Success");
		this.source = source;
		Document doc = (Document)srcParser.parseSourceData(source);
		logger.info("Parse Source Success");
		
		return doc;
	}

}
