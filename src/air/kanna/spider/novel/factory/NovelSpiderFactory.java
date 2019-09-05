package air.kanna.spider.novel.factory;

import air.kanna.spider.novel.download.NovelDownloader;
import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.spider.NovelSpider;

public interface NovelSpiderFactory {

    NovelSpider getSpider(String url, String... params);
    
    NovelDownloader getDownloader(Novel novel, String... params);
    
}
