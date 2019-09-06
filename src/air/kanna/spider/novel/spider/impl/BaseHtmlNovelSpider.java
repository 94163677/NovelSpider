package air.kanna.spider.novel.spider.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import air.kanna.spider.novel.exception.NovelParseException;
import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.util.StringUtil;
import air.kanna.spider.novel.util.log.Logger;
import air.kanna.spider.novel.util.log.LoggerProvider;

public abstract class BaseHtmlNovelSpider extends BaseNovelSpider {
    private static final Logger logger = LoggerProvider.getLogger(BaseHtmlNovelSpider.class);

    protected String url = null;
    protected String source = null;
    
    protected abstract Novel getOneNovel(Document html)throws NovelParseException;
    public abstract String getBaseUrl();
    
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
        
        Novel novel = getOneNovel(html);
        if(novel != null){
            result.add(novel);
        }
        
        logger.info("Parse Novel Information success: " + novel.toString());
        
        return result;
    }
    
    public String getNovelIdFromUrl(String url) {
        if(StringUtil.isNull(url)) {
            return null;
        }
        
        String baseUrl = getBaseUrl();
        if(!url.startsWith(baseUrl)){
            return null;
        }
        if(url.length() == baseUrl.length()) {
            return null;
        }
        
        url = url.substring(baseUrl.length() + 1);
        int idx = url.indexOf('/');
        if(idx < 0) {
            return url;
        }else {
            return url.substring(0, idx);
        }
    }
    
    protected String getStringFromElements(Elements list, String title) throws NovelParseException{
        return getStringFromElements(list, 0, title);
    }
    
    protected String getStringFromElements(Elements list, int idx, String title) throws NovelParseException{
        if(list == null){
            throw new NovelParseException(("Cannot get Novel " + title + " Element"), url, source);
        }
        if(idx < 0 || idx >= list.size()){
            throw new IndexOutOfBoundsException("Elements index error: " + idx + ", Max is: " + list.size());
        }
        
        return getStringFromElements(list.get(idx), title);
    }
    
    protected String getStringFromElements(Element element, String title) throws NovelParseException{
        if(element == null){
            throw new NovelParseException(("Cannot get Novel " + title + " String"), url, source);
        }
        
        String elementStr = element.text();
        if(elementStr == null || elementStr.length() <= 0){
            throw new NovelParseException(("Cannot get Novel " + title + " String"), url, source);
        }
        
        return StringUtil.trim(elementStr);
    }
    
    protected Document getDocument()throws NovelParseException{
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
