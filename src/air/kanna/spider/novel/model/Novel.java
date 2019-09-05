package air.kanna.spider.novel.model;

import java.util.ArrayList;
import java.util.List;

public class Novel {
	
	private String novelId;
	private String downloadId;
	
	private String novelTitle;
	private String author;
	private String summary;
	
	private List<NovelChapter> novelContent;

	public Novel(){
		novelContent = new ArrayList<NovelChapter>();
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("NovelChapter(").append("Id: ");
		sb.append(novelId).append(", DownloadId: ");
		sb.append(downloadId).append(", Title: ");
		sb.append(novelTitle).append(", Author: ");
		sb.append(author);
		
		if(novelContent != null && novelContent.size() > 0){
			sb.append(", Chapter:[");
			for(NovelChapter chapter : novelContent){
				sb.append(chapter.toString());
				sb.append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(']');
		}
		
		sb.append(')');

		return sb.toString();
	}
	
	public String getNovelId() {
		return novelId;
	}

	public void setNovelId(String novelId) {
		this.novelId = novelId;
	}

	public String getDownloadId() {
		return downloadId;
	}

	public void setDownloadId(String downloadId) {
		this.downloadId = downloadId;
	}

	public String getNovelTitle() {
		return novelTitle;
	}

	public void setNovelTitle(String novelTitle) {
		this.novelTitle = novelTitle;
	}

	public List<NovelChapter> getNovelContent() {
		return novelContent;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}

