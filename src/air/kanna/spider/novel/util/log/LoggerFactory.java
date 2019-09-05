package air.kanna.spider.novel.util.log;

public interface LoggerFactory {
	public Logger getLogger(Class cls);
	public Logger getLogger(String name);
}
