package cn.sicnu.ming.util;

/**
 * @author frank ming
 * @createTime 20200623 0:46
 * @description 字符转换类
 */
public class StringUtil {

    /**
     * 去除html标签
     */
    public static String stripHtml(String content){
        //<p>段落替换成换行
        content = content.replaceAll("<p.*?>","\r\n");
        //<br><br/>替换成换行
        content = content.replaceAll("<br\\s*/?>","\r\n");
        //去掉其他<>间的东西
        content = content.replaceAll("\\<.*?>","");
        //去掉空格
        content = content.replaceAll(" ","");
        return content;
    }

    //转义大于小于
    public static String esc(String content){
        return content.replace("<","&lt;").replace(">","&gt;");
    }
}
