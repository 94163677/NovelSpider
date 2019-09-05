package air.kanna.spider.syosetu.spider.download;

import java.io.File;

import air.kanna.spider.syosetu.model.SyosetuNovel;
import air.kanna.spider.syosetu.spider.ProcessListener;
import air.kanna.spider.syosetu.spider.SourceDataGetter;

public interface SyosetuNovelDownloader {

    static final int MODEL_CHAPTER = 45;
    static final int MODEL_LENGTH = 68;
    
    static final int DEFAULT_NUMBER_LENGTH = 4;
    static final int MIN_LENGTH = 10000;
    static final int DEFAULT_LENGTH = 130000;
    
    String download(SourceDataGetter getter, SyosetuNovel novel, File path, int model, int maxLength);

    void setProcess(ProcessListener process);

    void setStop(boolean isStop);
}
