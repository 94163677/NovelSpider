package air.kanna.spider.novel.model;

public class NovelSection {
	
	private String sectionTitle;
	private String sectionNum;
	private String createTime;
	private String updateTime;
	private String sectionContent;
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("NovelSection(").append("Title: ");
		sb.append(sectionTitle).append(", Num: ").append(sectionNum);
		sb.append(", Create: ").append(createTime);
		sb.append(", Update: ").append(updateTime);
		sb.append(')');
		
		return sb.toString();
	}
	
	public String getSectionTitle() {
		return sectionTitle;
	}
	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}
	public String getSectionNum() {
		return sectionNum;
	}
	public void setSectionNum(String sectionNum) {
		this.sectionNum = sectionNum;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getSectionContent() {
		return sectionContent;
	}
	public void setSectionContent(String sectionContent) {
		this.sectionContent = sectionContent;
	}
	
}
