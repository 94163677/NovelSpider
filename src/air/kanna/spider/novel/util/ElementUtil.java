package air.kanna.spider.novel.util;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import air.kanna.spider.novel.util.log.Logger;
import air.kanna.spider.novel.util.log.LoggerProvider;

public class ElementUtil {
    private static final Logger logger = LoggerProvider.getLogger(ElementUtil.class);
    
    public static String getStringFromElement(Elements elements) {
        StringBuilder sb = new StringBuilder();

        for(Element ele : elements) {
            getStringFromElement(ele, sb);
            sb.append(StringUtil.ENTER);
        }
        
        return sb.toString();
    }
    
    public static String getStringFromElement(Element element) {
        StringBuilder sb = new StringBuilder();
        getStringFromElement(element, sb);
        return sb.toString();
    }
    
    public static void getStringFromElement(Element element, StringBuilder sb) {
        if("br".equalsIgnoreCase(element.tagName())) {
            sb.append(StringUtil.ENTER);
            return;
        }
        for(Node node : element.childNodes()){
            if(node instanceof TextNode) {
                sb.append(((TextNode)node).text());
                continue;
            }
            if(node instanceof Element) {
                getStringFromElement((Element)node, sb);
            }else {
                logger.warn("Cannot support type: " + node.getClass());
            }
        }
    }
}
