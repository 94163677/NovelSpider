package air.kanna.spider.syosetu.spider.download;

import java.io.File;

import air.kanna.spider.syosetu.model.NovelChapter;
import air.kanna.spider.syosetu.model.SyosetuNovel;
import air.kanna.spider.syosetu.spider.ProcessListener;
import air.kanna.spider.syosetu.spider.SourceDataGetter;
import air.kanna.spider.syosetu.spider.util.StringUtil;

public abstract class BaseSyosetuNovelDownloader implements SyosetuNovelDownloader {

    protected ProcessListener process = null;
    protected boolean isStop = false;
    
    protected void checkParams(SourceDataGetter getter, SyosetuNovel novel, File path, int model, int maxLength){
        if(getter == null){
            throw new NullPointerException("SourceDataGetter is null");
        }
        if(novel == null 
                || novel.getNovelContent() == null
                || novel.getNovelContent().size() <= 0){
            throw new NullPointerException("SyosetuNovel is null");
        }
        if(StringUtil.isNull(novel.getNovelTitle())){
            throw new IllegalArgumentException("SyosetuNovel's name is null");
        }
        if(StringUtil.isNull(novel.getDownloadId())) {
            throw new IllegalArgumentException("SyosetuNovel's downloadId is null");
        }
        
        if(path == null){
            throw new NullPointerException("Output Path is null");
        }
        if(!path.isDirectory() || !path.exists()){
            if(!path.mkdirs()){
                throw new IllegalArgumentException("Output Path is not exist or cannot create: " + path.getAbsolutePath());
            }
        }
        
        switch(model){
            case MODEL_CHAPTER: break;
            case MODEL_LENGTH: break;
            default: throw new IllegalArgumentException("Download is error: " + model);
        }
        
        if(model == MODEL_LENGTH && maxLength < MIN_LENGTH){
            throw new IllegalArgumentException("Max Length must > " + MIN_LENGTH + ", but: " + maxLength);
        }
    }
    
    protected String getOutFileTitle(SyosetuNovel novel, NovelChapter chapter, int num){
        StringBuilder title = new StringBuilder();
        
        title.append('[').append(novel.getAuthor()).append(']');
        title.append(novel.getNovelTitle());
        
        String numStr = "" + num;
        for(int i=0; i<(DEFAULT_NUMBER_LENGTH - numStr.length()); i++){
            title.append('0');
        }
        title.append(numStr);
        
        if(chapter != null){
            title.append('-').append(chapter.getChapterTitle());
        }
        
        return title.toString();
    }
    
    public ProcessListener getProcess() {
        return process;
    }

    @Override
    public void setProcess(ProcessListener process) {
        this.process = process;
    }

    public boolean isStop() {
        return isStop;
    }

    @Override
    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }
}
