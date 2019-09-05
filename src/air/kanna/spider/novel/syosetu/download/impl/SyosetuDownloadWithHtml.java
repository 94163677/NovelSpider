package air.kanna.spider.novel.syosetu.download.impl;

import java.io.File;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import air.kanna.spider.novel.model.NovelSection;
import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.spider.SourceDataGetter;
import air.kanna.spider.novel.spider.SourceParser;
import air.kanna.spider.novel.spider.impl.HtmlSourceParser;
import air.kanna.spider.novel.util.Timer;
import air.kanna.spider.novel.util.log.Logger;
import air.kanna.spider.novel.util.log.LoggerProvider;

public class SyosetuDownloadWithHtml extends SyosetuDownloadWithDownloadId {
    private static final Logger logger = LoggerProvider.getLogger(SyosetuDownloadWithHtml.class);
    
    private static final String DOWNLOAD_URL = "https://ncode.syosetu.com/$1/$2/";
    
    private SourceParser parser = null;
    
    public SyosetuDownloadWithHtml() {
        parser = new HtmlSourceParser();
    }
    
    @Override
    protected String getSectionString(SourceDataGetter getter, Novel novel, NovelSection section){
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
        
        String result = getSectionStringFromHtml(getter.getSourceData(url, "UTF-8"));
        if(result == null) {
            sb.insert(0, "Error ");
            logger.error(sb.toString());
        }
        
        return result;
    }
    
    @Override
    protected void checkParams(SourceDataGetter getter, Novel novel, File path, int model, int maxLength){
        try {
            super.checkParams(getter, novel, path, model, maxLength);
        }catch(IllegalArgumentException ie) {
            if(!ie.getMessage().contains("downloadId")) {
                throw ie;
            }
        }
    }
    
    private String getSectionStringFromHtml(String html) {
        Document document = (Document)parser.parseSourceData(html);
        if(document == null) {
            return null;
        }
        
        StringBuilder source = new StringBuilder();
        Elements title = document.getElementsByClass("novel_subtitle");
        Elements sourceElem = document.select("#novel_honbun");
        
        source.append("\r\n").append(title.get(0).text()).append("\r\n\r\n");

        for(Node node : sourceElem.get(0).childNodes()){
            if(!(node instanceof Element)) {
                continue;
            }
            source.append(getStringFromElement((Element)node));
            source.append("\r\n");
        }
        
        return source.toString();
    }
    
    private String getStringFromElement(Element element) {
        StringBuilder sb = new StringBuilder();
        getStringFromElement(element, sb);
        return sb.toString();
    }
    
    private void getStringFromElement(Element element, StringBuilder sb) {
        if("br".equalsIgnoreCase(element.tagName())) {
            sb.append("\r\n");
            return;
        }
        for(Node node : element.childNodes()){
            if(node instanceof TextNode) {
                sb.append(((TextNode)node).text());
                continue;
            }
            if(node instanceof Element) {
                getStringFromElement((Element)node, sb);
            }else {
                logger.warn("Cannot support type: " + node.getClass());
            }
        }
    }
}
