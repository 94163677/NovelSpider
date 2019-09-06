package air.kanna.spider.novel.syosetu.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import air.kanna.spider.novel.download.impl.BaseNovelDownloader;
import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.model.NovelChapter;
import air.kanna.spider.novel.model.NovelSection;
import air.kanna.spider.novel.util.Entry;
import air.kanna.spider.novel.util.StringUtil;
import air.kanna.spider.novel.util.Timer;
import air.kanna.spider.novel.util.log.Logger;
import air.kanna.spider.novel.util.log.LoggerProvider;

public class SyosetuDownloadWithDownloadId 
        extends BaseNovelDownloader{
	private static final Logger logger = LoggerProvider.getLogger(SyosetuDownloadWithDownloadId.class);

	private static final String DOWNLOAD_URL = "https://ncode.syosetu.com/txtdownload/dlstart/ncode/$1/?no=$2&hankaku=0&code=utf-8&kaigyo=crlf";
	private static final char[] FORBIDDEN = {'\\', '/', ':', '*', '?', '\"', '<', '>', '|'};
	
	@Override
	public String download(Novel novel, File path, int model, int maxLength){
		checkParams(novel, path, model, maxLength);

		isStop = false;
		StringBuilder sb = new StringBuilder();
		
		logger.info("Start Download " + novel.getNovelTitle());
		
		if(model == MODEL_LENGTH){
			getNovelStringByLength(novel, path, maxLength, sb);
		}else
		if(novel.getNovelContent().size() == 1
				&& novel.getNovelContent().get(0)
					.getChapterTitle().equalsIgnoreCase(
							NovelChapter.DEFAULT_CHAPTER_TITLE)){
			logger.warn("SyosetuNovel download is switch to MODEL_LENGTH");
			getNovelStringByLength(novel, path, maxLength, sb);
		}else{
			getNovelStringByChapter(novel, path, sb);
		}
		
		if(sb.length() <= 0){
			logger.info("Finish Download " + novel.getNovelTitle());
			return null;
		}
		
			
		logger.error("Download " + novel.getNovelTitle() + " faild.");
		return sb.toString();
	}
	
	private List<Entry> getNovelStringByChapter(
			Novel novel, 
			File file, 
			StringBuilder faild){
		
		List<Entry> result = new ArrayList<Entry>();
		StringBuilder content;
		String dir = file.getAbsolutePath();
		
		if(!dir.endsWith(File.separator)){
			dir += File.separator;
		}
		
		for(int i=0; i<novel.getNovelContent().size(); i++){
			NovelChapter chapter = novel.getNovelContent().get(i);
			Entry entry = new Entry();
			content = new StringBuilder();
			
			entry.key = getOutFileTitle(novel, chapter, (i + 1));
			
			for(int k=0; k<chapter.getChapterContent().size(); k++){
				NovelSection section = chapter.getChapterContent().get(k);
				
				if(isStop){
					return result;
				}
				if(process != null){
					process.next(section.getSectionTitle());
				}
				
				String contString = getSectionString(novel, section);
				if(contString == null || contString.length() <= 0){
					throw new NullPointerException("Cannot get Section: " + chapter.getChapterContent().get(k).getSectionTitle());
				}
				content.append(contString);
				content.append(StringUtil.ENTER)
					.append(StringUtil.ENTER)
					.append(StringUtil.ENTER);
			}
			
			entry.value = content.toString();
			
			try{
				logger.info("Before Init Name: " + entry.key);
				saveEntryToFile(dir + clearForbiddenCharacter(entry.key), entry);
			}catch(Exception e){
				faild.append("Cannot save Entry: ").append(entry.key)
					.append(StringUtil.ENTER);
				logger.error("Cannot save Entry: " + entry.key);
			}
		}
		
		return result;
	}
	
	private List<Entry> getNovelStringByLength(
			Novel novel, 
			File file, 
			int maxLength,
			StringBuilder faildDesc){
		
		List<Entry> result = new ArrayList<Entry>();
		Entry entry = new Entry();
		StringBuilder content = new StringBuilder();
		String dir = file.getAbsolutePath();
		int count = 1;
		
		if(maxLength < MIN_LENGTH){
			maxLength = DEFAULT_LENGTH;
		}
		if(!dir.endsWith(File.separator)){
			dir += File.separator;
		}

		for(int i=0; i<novel.getNovelContent().size(); i++){
			NovelChapter chapter = novel.getNovelContent().get(i);
			
			entry.key = getOutFileTitle(novel, null, count++);
			
			for(int k=0; k<chapter.getChapterContent().size(); k++){
				NovelSection section = chapter.getChapterContent().get(k);
				if(isStop){
					return result;
				}
				if(process != null){
					process.next(section.getSectionTitle());
				}
				String contString = getSectionString(novel, section);
				if(contString == null || contString.length() <= 0){
					throw new NullPointerException("Cannot get Section: " + chapter.getChapterContent().get(k).getSectionTitle());
				}
				content.append(contString);
				content.append(StringUtil.ENTER)
					.append(StringUtil.ENTER)
					.append(StringUtil.ENTER);
				
				if(content.length() >= maxLength){
					
					entry.value = content.toString();
					result.add(entry);
					
					try{
						logger.info("Before Init Name: " + entry.key);
						saveEntryToFile(dir + clearForbiddenCharacter(entry.key), entry);
					}catch(Exception e){
					    faildDesc.append("Cannot save Entry: ").append(entry.key)
							.append(StringUtil.ENTER);
						logger.error("Cannot save Entry: " + entry.key);
					}
					
					content = new StringBuilder();
					entry = new Entry();
					
					entry.key = getOutFileTitle(novel, null, count++);
				}
			}
		}
		
		if(content.length() > 0){
			entry.value = content.toString();
			result.add(entry);
			
			try{
				saveEntryToFile(dir + entry.key, entry);
			}catch(Exception e){
			    faildDesc.append("Cannot save Entry: ").append(entry.key)
					.append(StringUtil.ENTER);
				logger.error("Cannot save Entry: " + entry.key);
			}
		}
		
		return result;
	}
	
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

	private void saveEntryToFile(String path, Entry entry)throws IOException{
		File file = new File(path + ".txt");
		Writer save = new OutputStreamWriter(
				new BufferedOutputStream(
						new FileOutputStream(file),
						10240),
				"UTF-8");
		
		save.write(entry.value);
		save.flush();
		save.close();
		
		logger.info("Save to File success: " + file.getAbsolutePath());
	}
	
	private String clearForbiddenCharacter(String fileName){
		if(StringUtil.isNull(fileName)){
			return fileName;
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<fileName.length(); i++){
			char ch = fileName.charAt(i);
			boolean isForbidden = false;
			
			for(int j=0; j<FORBIDDEN.length; j++){
				if(FORBIDDEN[j] == ch){
					isForbidden = true;
				}
			}
			
			if(isForbidden){
				sb.append(' ');
			}else{
				sb.append(ch);
			}
		}
		
		return sb.toString();
	}
	
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
