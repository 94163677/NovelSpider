package air.kanna.spider.novel.syosetu.impl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
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

public class SyosetuDownloadWithHtml 
        extends BaseHtmlNovelDownloader {
    private static final Logger logger = LoggerProvider.getLogger(SyosetuDownloadWithHtml.class);
    
    private static final String DOWNLOAD_URL = "https://ncode.syosetu.com/$1/$2/";
    
    private SourceParser parser = null;
    
    public SyosetuDownloadWithHtml() {
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
        Elements title = document.getElementsByClass("novel_subtitle");
        Elements sourceElem = document.select("#novel_honbun");
        
        source.append(StringUtil.ENTER)
            .append(title.first().text())
            .append(StringUtil.ENTER)
            .append(StringUtil.ENTER);

        for(Node node : sourceElem.get(0).childNodes()){
            if(!(node instanceof Element)) {
                continue;
            }
            source.append(ElementUtil.getStringFromElement((Element)node));
            source.append(StringUtil.ENTER);
        }
        
        return source.toString();
    }
}
