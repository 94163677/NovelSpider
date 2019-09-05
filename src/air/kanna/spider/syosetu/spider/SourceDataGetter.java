package air.kanna.spider.syosetu.spider;

/**
 * 用于从URL获取实际的内容
 * @author kan-na
 *
 */
public interface SourceDataGetter {

	/**
	 * 从URL中获取实际内容，以字符串方式返回
	 * @param url 要获取的URL
	 * @param charset 返回数据的字符编码
	 * @param params 其他参数
	 * @return URL返回的字符串，失败返回null
	 */
	public String getSourceData(String url, String charset, String... params);
	
}
