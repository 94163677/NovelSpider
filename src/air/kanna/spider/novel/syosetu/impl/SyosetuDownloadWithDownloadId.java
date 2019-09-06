package air.kanna.spider.novel.syosetu.impl;

import java.io.File;
import java.util.List;

import air.kanna.spider.novel.download.impl.BaseHtmlNovelDownloader;
import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.model.NovelSection;
import air.kanna.spider.novel.util.Entry;
import air.kanna.spider.novel.util.StringUtil;
import air.kanna.spider.novel.util.Timer;
import air.kanna.spider.novel.util.log.Logger;
import air.kanna.spider.novel.util.log.LoggerProvider;

public class SyosetuDownloadWithDownloadId 
        extends BaseHtmlNovelDownloader{
	private static final Logger logger = LoggerProvider.getLogger(SyosetuDownloadWithDownloadId.class);

	static final String DOWNLOAD_URL = "https://ncode.syosetu.com/txtdownload/dlstart/ncode/$1/?no=$2&hankaku=0&code=utf-8&kaigyo=crlf";

	private void saveEntrysToFile(List<Entry> list, File path){
		if(list == null || list.size() <= 0){
			return;
		}
		
		String dir = path.getAbsolutePath();
		if(!dir.endsWith(File.separator)){
			dir += File.separator;
		}
		
		for(int i=0; i<list.size(); i++){
			Entry entry = list.get(i);
			if(entry == null
					|| entry.key == null
					|| entry.key.length() <= 0){
				logger.warn("Cannot save Entry because is null at " + i);
				continue;
			}
			try{
				logger.info("Before Init Name: " + entry.key);
				saveEntryToFile(dir + clearForbiddenCharacter(entry.key), entry);
			}catch(Exception e){
				logger.error("Cannot save Entry at " + i, e);
			}
		}
	}
	
	@Override
	protected void checkParams(Novel novel, File path, int model, int maxLength){
	    super.checkParams(novel, path, model, maxLength);
	    if(StringUtil.isNull(novel.getDownloadId())) {
            throw new IllegalArgumentException("SyosetuNovel's downloadId is null");
        }
	}

	@Override
	protected String getSectionString(Novel novel, NovelSection section){
		String url = DOWNLOAD_URL
				.replace("$1", novel.getDownloadId())
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
		String result = sourceGetter.getSourceData(url, "UTF-8");
		
		return result;
	}
	
	
}
