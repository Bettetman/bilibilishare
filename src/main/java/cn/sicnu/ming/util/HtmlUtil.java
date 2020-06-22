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

    /**
     * 拼接分页代码
     * @param targetUrl 请求路径
     * @param count     总记录数
     * @param currentPage   当前页
     * @param msg           没查到数据的时候的显示内容
     * @return
     */
    public static String getPagation(String targetUrl,int count,int currentPage, String msg){
        //总页数
        int totalPage = count%ConstUtil.PAGE_SIZE == 0?count/ConstUtil.PAGE_SIZE:(count/ConstUtil.PAGE_SIZE)+1;
        StringBuffer pageCode = new StringBuffer();
        pageCode.append("<div class=\"laypage-main\">");
        if(totalPage>0){
            pageCode.append("<a href=\""+targetUrl+"/1\">首页</a>");
        }
        if(currentPage!=1){
            pageCode.append("<a style=\"display: inline-block;\" href=\""+targetUrl+"/" + (currentPage - 1) + "\">上一页</a>");
        }
        for(int i = currentPage - 3;i <=currentPage +3;i++){
            if(i<1||i>totalPage){
                continue;
            }
            if(i == currentPage){
                pageCode.append("<span class=\"laypage-curr\">" + i +"</span>");
            }else{
                pageCode.append("<a href=\""+targetUrl+"/"+i+"\">"+i+"</a>");
            }
        }
        if(currentPage<totalPage){
            pageCode.append("<a style=\"display: inline-block;\" href=\""+targetUrl+"/" + (currentPage + 1) + "\">下一页</a>");
        }
        if(totalPage>0){
            pageCode.append("<a href=\""+targetUrl+"/"+totalPage+"\">尾页</a>");
        }else{
            pageCode.append("<span>"+msg+"</span>");
        }
        pageCode.append("</div>");
        return pageCode.toString();
    }
}
