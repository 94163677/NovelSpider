package air.kanna.spider.novel.download.impl;

import java.io.File;

import air.kanna.spider.novel.model.NovelChapter;
import air.kanna.spider.novel.download.NovelDownloader;
import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.spider.ProcessListener;
import air.kanna.spider.novel.spider.SourceDataGetter;
import air.kanna.spider.novel.util.StringUtil;

public abstract class BaseSyosetuNovelDownloader implements NovelDownloader {

    protected ProcessListener process = null;
    protected boolean isStop = false;
    protected SourceDataGetter sourceGetter = null;
    
    protected void checkParams(Novel novel, File path, int model, int maxLength){
        if(sourceGetter == null){
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
    
    protected String getOutFileTitle(Novel novel, NovelChapter chapter, int num){
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
    
    @Override
    public void setSourceGetter(SourceDataGetter sourceGetter) {
        if(sourceGetter != null) {
            this.sourceGetter = sourceGetter;
        }
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
