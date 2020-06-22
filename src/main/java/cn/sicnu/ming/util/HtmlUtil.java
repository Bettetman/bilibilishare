package cn.sicnu.ming.util;

import cn.sicnu.ming.entity.ArcType;

import java.util.List;

/**
 * @author frank ming
 * @createTime 20200622 15:39
 * @description 网页工具类
 */
public class HtmlUtil {

    /**
     * 拼接类型列表的编码
     */
    public static String getArcTypeStr(String type, List<ArcType> arcTypeList){
        StringBuffer arcTypeCode = new StringBuffer();
        if("all".equals(type)){
            arcTypeCode.append("<li class=\"layui-hide-xs layui-this\"><a href=\"/\">首页</a></li>");
        }else{
            arcTypeCode.append("<li><a href=\"/\">首页</a></li>");
        }
        for(ArcType arcType:arcTypeList){
            if(type.equals((arcType.getArcTypeId().toString()))){
                arcTypeCode.append("<li class=\"layui-hide-xs layui-this\"><a href=\"/article/"+
                        arcType.getArcTypeId()+"/1\">"+arcType.getArcTypeName()+"</a></li>");
            }else{
                arcTypeCode.append("<li><a href=\"/article/"+
                        arcType.getArcTypeId()+"/1\">"+arcType.getArcTypeName()+"</a></li>");
            }
        }
        return arcTypeCode.toString();
    }
}
