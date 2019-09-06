package air.kanna.spider.novel.kakuyomu.impl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import air.kanna.spider.novel.download.impl.BaseHtmlNovelDownloader;
import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.model.NovelSection;
import air.kanna.spider.novel.spider.SourceParser;
import air.kanna.spider.novel.spider.impl.HtmlSourceParser;
import air.kanna.spider.novel.util.ElementUtil;
import air.kanna.spider.novel.util.StringUtil;
import air.kanna.spider.novel.util.Timer;
import air.kanna.spider.novel.util.log.Logger;
import air.kanna.spider.novel.util.log.LoggerProvider;

public class KakuyomuDownloadWithHtml 
        extends BaseHtmlNovelDownloader {
    private static final Logger logger = LoggerProvider.getLogger(KakuyomuDownloadWithHtml.class);
    
    private static final String DOWNLOAD_URL = "https://kakuyomu.jp/works/$1/episodes/$2";
    
    private SourceParser parser = null;
    
    public KakuyomuDownloadWithHtml() {
        parser = new HtmlSourceParser();
    }
    
    @Override
    protected String getSectionString(Novel novel, NovelSection section){
        String url = DOWNLOAD_URL
                .replace("$1", novel.getNovelId())
                .replace("$2", section.getSectionNum());
        StringBuilder sb = new StringBuilder();
        
        sb.append("Download <").append(novel.getNovelTitle()).append("> at section(");
        sb.append(section.getSectionTitle()).append(") with url: ").append(url);
        logger.info(sb.toString());
        
        try{
            Thread.sleep(Timer.getWaitingTime());
        }catch(Exception e){
            e.printStackTrace();
        }
        
        String result = getSectionStringFromHtml(sourceGetter.getSourceData(url, "UTF-8"));
        if(result == null) {
            sb.insert(0, "Error ");
            logger.error(sb.toString());
        }
        
        return result;
    }
    
    private String getSectionStringFromHtml(String html) {
        Document document = (Document)parser.parseSourceData(html);
        if(document == null) {
            return null;
        }
        
        StringBuilder source = new StringBuilder();
        Elements title = document.getElementsByClass("widget-episodeTitle");
        Elements sourceElem = document.select(".widget-episodeBody p");
        
        source.append(StringUtil.ENTER)
            .append(title.first().text())
            .append(StringUtil.ENTER)
            .append(StringUtil.ENTER);

        for(Element node : sourceElem){
            source.append(ElementUtil.getStringFromElement((Element)node));
            source.append(StringUtil.ENTER);
        }
        
        return source.toString();
    }
}
