package air.kanna.spider.novel.exception;

public class NovelParseException extends RuntimeException {
	private static final long serialVersionUID = 5918971809505186048L;

	private String url = null;
	private String source = null;
	
	public NovelParseException(String message) {
		super(message);
	}
	
	public NovelParseException(String message, String url, String source) {
		super(message);
		this.url = url;
		this.source = source;
	}

	public String getUrl() {
		return url;
	}

	public String getSource() {
		return source;
	}
}
