package air.kanna.spider.novel.util;

public class StringUtil {
    //回车（换行）
    public static final String ENTER = ((char)0xd) + "" + ((char)0xa);
    
    //视为空格的字符
	public static final char[] SPACES = {
			' ',
			'\t',
			(char)0xa,
			(char)0xd,
			(char)0x3000};
	
	/**
	 * 传入的字符串是否为空或者是空串
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
		return str == null || str.length() <= 0;
	}
	public static boolean isNotNull(String str){
	    return !isNull(str);
	}
	
	/**
	 * 传入的字符串是否为：
	 * 1、空
	 * 2、空串
	 * 3、全部都是视为空格的字符
	 * @param str
	 * @return
	 */
	public static boolean isSpace(String str){
		if(isNull(str)){
			return true;
		}
		for(int i=0; i<str.length(); i++){
			if(!isSpaceChar(str.charAt(i))){
				return false;
			}
		}
		
		return true;
	}
	public static boolean isNotSpace(String str){
	    return !isSpace(str);
	}
	
	/**
	 * 从字符串的首尾删除被视为空格的字符
	 * @param org
	 * @return
	 */
	public static String trim(String org){
		if(isNull(org)){
			return org;
		}
		int idx = -1;
		
		for(idx=0; idx<org.length(); idx++){
			if(!isSpaceChar(org.charAt(idx))){
				org = org.substring(idx);
				break;
			}
		}
		
		for(idx = org.length() - 1; idx>=0; idx--){
			if(!isSpaceChar(org.charAt(idx))){
				org = org.substring(0, idx + 1);
				break;
			}
		}
		
		return org;
	}
	
	/**
	 * 是否为被视为空格的字符
	 * @param ch
	 * @return
	 */
	public static boolean isSpaceChar(char ch){
		for(int i=0; i<SPACES.length; i++){
			if(ch == SPACES[i]){
				return true;
			}
		}
		
		return false;
	}
	public static boolean isNotSpaceChar(char ch){
	    return !isSpaceChar(ch);
	}
	
	
	public static void main(String[] args){
		String[] tests = {
				"  safj sdfj",
				"sljf sdfjl \t  dslkjf\t",
				"\t sdfjlsdsldjf  "
		};
		
		for(String str : tests){
			System.out.println(trim(str));
		}
	}
}
