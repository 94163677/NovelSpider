package air.kanna.spider.syosetu.spider.util;

public interface LoggerFactory {
	public Logger getLogger(Class cls);
	public Logger getLogger(String name);
}
