package air.kanna.spider.novel.factory.impl;

import air.kanna.spider.novel.download.NovelDownloader;
import air.kanna.spider.novel.factory.NovelSpiderFactory;
import air.kanna.spider.novel.kakuyomu.impl.KakuyomuDownloadWithHtml;
import air.kanna.spider.novel.kakuyomu.impl.KakuyomuNovelSpider;
import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.spider.NovelSpider;
import air.kanna.spider.novel.spider.SourceDataGetter;
import air.kanna.spider.novel.spider.impl.HtmlSourceParser;
import air.kanna.spider.novel.spider.impl.HttpsSourceDataGetter;

public class KakuyomuSpiderFactory implements NovelSpiderFactory{
    
    private NovelSpider spider = null;
    private NovelDownloader downloader = null;

    public KakuyomuSpiderFactory(){
        SourceDataGetter sourceGetter = new HttpsSourceDataGetter();
        HtmlSourceParser sourceParser = new HtmlSourceParser();
        KakuyomuNovelSpider spider = new KakuyomuNovelSpider();
        
        spider.setSrcGetter(sourceGetter);
        spider.setSrcParser(sourceParser);
        
        this.spider = spider;
        
        downloader = new KakuyomuDownloadWithHtml();        
        downloader.setSourceGetter(sourceGetter);
    }
    
    @Override
    public NovelSpider getSpider(String url, String... params) {
        return spider;
    }
    
    @Override
    public NovelDownloader getDownloader(Novel novel, String... params) {
        return downloader;
    }
}
