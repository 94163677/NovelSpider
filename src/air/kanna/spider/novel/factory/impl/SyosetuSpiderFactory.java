package air.kanna.spider.novel.factory.impl;

import air.kanna.spider.novel.download.NovelDownloader;
import air.kanna.spider.novel.factory.NovelSpiderFactory;
import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.spider.NovelSpider;
import air.kanna.spider.novel.spider.SourceDataGetter;
import air.kanna.spider.novel.spider.impl.HtmlSourceParser;
import air.kanna.spider.novel.spider.impl.HttpsSourceDataGetter;
import air.kanna.spider.novel.syosetu.impl.SyosetuDownloadWithDownloadId;
import air.kanna.spider.novel.syosetu.impl.SyosetuDownloadWithHtml;
import air.kanna.spider.novel.syosetu.impl.SyosetuNovelSpider;
import air.kanna.spider.novel.util.StringUtil;

public class SyosetuSpiderFactory implements NovelSpiderFactory{
    
    private NovelSpider spider = null;
    private NovelDownloader downloaderId = null, downloaderHtml = null;
    
    public SyosetuSpiderFactory(){
        SourceDataGetter sourceGetter = new HttpsSourceDataGetter();
        HtmlSourceParser sourceParser = new HtmlSourceParser();
        SyosetuNovelSpider spider = new SyosetuNovelSpider();
        
        spider.setSrcGetter(sourceGetter);
        spider.setSrcParser(sourceParser);
        
        this.spider = spider;
        
        downloaderId = new SyosetuDownloadWithDownloadId();
        downloaderHtml = new SyosetuDownloadWithHtml();
        
        downloaderId.setSourceGetter(sourceGetter);
        downloaderHtml.setSourceGetter(sourceGetter);
    }
    
    @Override
    public NovelSpider getSpider(String url, String... params) {
        return spider;
    }
    
    @Override
    public NovelDownloader getDownloader(Novel novel, String... params) {
        if(novel == null) {
            return downloaderHtml;
        }
        if(StringUtil.isNotNull(novel.getDownloadId())) {
            return downloaderId;
        }
        return downloaderHtml;
    }
}
