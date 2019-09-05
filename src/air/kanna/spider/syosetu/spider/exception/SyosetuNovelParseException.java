package air.kanna.spider.syosetu.spider.exception;

public class SyosetuNovelParseException extends RuntimeException {
	private static final long serialVersionUID = 5918971809505186048L;

	private String url = null;
	private String source = null;
	
	public SyosetuNovelParseException(String message) {
		super(message);
	}
	
	public SyosetuNovelParseException(String message, String url, String source) {
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
