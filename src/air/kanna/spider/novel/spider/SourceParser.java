package air.kanna.spider.novel.spider;

/**
 * 
 * @author kan-na
 *
 */
public interface SourceParser {

	public Object parseSourceData(String source, String... params);

}
