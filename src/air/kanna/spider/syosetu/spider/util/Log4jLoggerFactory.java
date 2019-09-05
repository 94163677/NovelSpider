package air.kanna.spider.syosetu.spider.util;

public class Log4jLoggerFactory implements LoggerFactory {

	@Override
	public Logger getLogger(Class cls) {
		Log4jLogger logger = new Log4jLogger(org.apache.log4j.Logger.getLogger(cls));
		return logger; 
	}

	@Override
	public Logger getLogger(String name) {
		Log4jLogger logger = new Log4jLogger(org.apache.log4j.Logger.getLogger(name));
		return logger; 
	}

}
