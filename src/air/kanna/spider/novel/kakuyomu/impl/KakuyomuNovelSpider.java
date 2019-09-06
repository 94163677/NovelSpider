package air.kanna.spider.novel.kakuyomu.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import air.kanna.spider.novel.exception.NovelParseException;
import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.model.NovelChapter;
import air.kanna.spider.novel.model.NovelSection;
import air.kanna.spider.novel.spider.impl.BaseHtmlNovelSpider;
import air.kanna.spider.novel.util.ElementUtil;
import air.kanna.spider.novel.util.StringUtil;
import air.kanna.spider.novel.util.log.Logger;
import air.kanna.spider.novel.util.log.LoggerProvider;

public class KakuyomuNovelSpider extends BaseHtmlNovelSpider {
    private static final Logger logger = LoggerProvider.getLogger(KakuyomuNovelSpider.class);
    private static final String MAIN_URL = "https://kakuyomu.jp/works";
    
    @Override
    public String getMainUrl() {
        return MAIN_URL + "/$1";
    }
    
    @Override
    public String getBaseUrl() {
        return MAIN_URL;
    }

    @Override
    protected Novel getOneNovel(Document html) throws NovelParseException {
        Novel novel = new Novel();
        
        novel.setNovelId(novelId);
        novel.setNovelTitle(getStringFromElements(html.select("#workTitle a"), "Title"));
        novel.setAuthor(getStringFromElements(html.select("#workAuthor-activityName a"), "Author"));
        novel.setSummary(ElementUtil.getStringFromElement(html.select("p#introduction")));
        
        novel.getNovelContent().addAll(getNovelChapter(html));
        
        return novel;
    }

    private List<NovelChapter> getNovelChapter(Document html)throws NovelParseException{
        Elements list = html.getElementsByClass("widget-toc-items");
        if(list == null || list.size() <= 0){
            throw new NovelParseException(("Cannot get Novel Chapter Element"), url, source);
        }
        
        list = list.first().children();
        if(list == null || list.size() <= 0){
            throw new NovelParseException(("Cannot get Novel Chapter Element"), url, source);
        }
        
        List<NovelChapter> chapters = new ArrayList<NovelChapter>();
        Elements chaptersElem = html.select(".widget-toc-chapter");
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
            if(hasChapter && (item.attr("class").indexOf("widget-toc-chapter") >= 0)){
                if(current != null){
                    chapters.add(current);
                }
                current = new NovelChapter();
                current.setChapterIndex(chpIndex);
                current.setChapterTitle(getStringFromElements(item.select("span"), "ChapterTitle"));
                chpIndex++;
                continue;
            }
            if(item.attr("class").equalsIgnoreCase("widget-toc-episode")){
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
        if(item == null || item.children().size() < 1){
            throw new NovelParseException("Element is null");
        }
        Elements childen = item.select("a");
        if(childen == null || childen.first() == null){
            throw new NovelParseException("Cannot get Section Title Element");
        }
        Element link = childen.first();
        
        NovelSection section = new NovelSection();
        Element element = link.select("span").first();
        String tempStr = element.text();
        
        //getSectionTitle
        if(StringUtil.isNull(tempStr)){
            throw new NovelParseException("Cannot get Section Title String");
        }
        section.setSectionTitle(StringUtil.trim(tempStr));
        
        //getSectionNum
        tempStr = link.attr("href");
        if(StringUtil.isNull(tempStr)){
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
        element = link.select("time").first();
        tempStr = element.attr("datetime");
        if(StringUtil.isNull(tempStr)) {
            logger.warn("Cannot get CreateTime at " + section.getSectionTitle());
            return section;
        }
        
        section.setCreateTime(tempStr);
        section.setUpdateTime("");
        
        return section;
    }
}
