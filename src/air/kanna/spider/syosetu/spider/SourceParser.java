package air.kanna.spider.syosetu.spider;

/**
 * 
 * @author kan-na
 *
 */
public interface SourceParser {

	public Object parseSourceData(String source, String... params);

}
