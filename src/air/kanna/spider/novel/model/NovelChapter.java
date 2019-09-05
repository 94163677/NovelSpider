package air.kanna.spider.novel.model;

import java.util.ArrayList;
import java.util.List;

public class NovelChapter {
    public static final String DEFAULT_CHAPTER_TITLE = "DEFAULT TITLE";
    
	private String chapterTitle;
	private int chapterIndex;
	private List<NovelSection> chapterContent;
	
	public NovelChapter(){
		chapterContent = new ArrayList<NovelSection>();
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("NovelChapter(").append("Title: ");
		sb.append(chapterTitle).append(", index: ");
		sb.append(chapterIndex);
		
		if(chapterContent != null && chapterContent.size() > 0){
			sb.append(", Sections:[");
			for(NovelSection section : chapterContent){
				sb.append(section.toString());
				sb.append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(']');
		}
		
		sb.append(')');
		
		return sb.toString();
	}
	
	public String getChapterTitle() {
		return chapterTitle;
	}
	public void setChapterTitle(String chapterTitle) {
		this.chapterTitle = chapterTitle;
	}
	public int getChapterIndex() {
		return chapterIndex;
	}
	public void setChapterIndex(int chapterIndex) {
		this.chapterIndex = chapterIndex;
	}
	public List<NovelSection> getChapterContent() {
		return chapterContent;
	}
}
