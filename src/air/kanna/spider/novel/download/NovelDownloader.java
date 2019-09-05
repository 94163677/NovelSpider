package air.kanna.spider.novel.download;

import java.io.File;

import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.spider.ProcessListener;
import air.kanna.spider.novel.spider.SourceDataGetter;

public interface NovelDownloader {

    static final int MODEL_CHAPTER = 45;
    static final int MODEL_LENGTH = 68;
    
    static final int DEFAULT_NUMBER_LENGTH = 4;
    static final int MIN_LENGTH = 10000;
    static final int DEFAULT_LENGTH = 130000;
    
    String download(Novel novel, File path, int model, int maxLength);

    void setSourceGetter(SourceDataGetter sourceGetter);
    
    void setProcess(ProcessListener process);

    void setStop(boolean isStop);
}
