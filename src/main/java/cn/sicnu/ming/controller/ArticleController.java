package cn.sicnu.ming.controller;

import cn.sicnu.ming.entity.Article;
import cn.sicnu.ming.lucene.ArticleIndex;
import cn.sicnu.ming.service.ArcTypeService;
import cn.sicnu.ming.service.ArticleService;
import cn.sicnu.ming.util.ConstUtil;
import cn.sicnu.ming.util.HtmlUtil;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author frank ming
 * @createTime 20200622 19:02
 * @description 首页的文章控制
 */
@RestController
@RequestMapping(value = "/article")
public class ArticleController {

    @Autowired
    private ArcTypeService arcTypeService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleIndex articleIndex;
    /**
     * 分页查询资源
     * @param type
     * @param currentPage
     * @return
     */
    @RequestMapping("/{type}/{currentPage}")
    public ModelAndView index(@PathVariable(value = "type",required = false) String type, @PathVariable(value = "currentPage",required = false) Integer currentPage){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        //类型的html代码
        List arcTypeList = arcTypeService.listAll(Sort.Direction.ASC,"sort");
        mav.addObject("arcTypeStr", HtmlUtil.getArcTypeStr(type,arcTypeList));
        //资源列表
        Map<String, Object> map = articleService.list(type,currentPage, ConstUtil.PAGE_SIZE);
        mav.addObject("articleList",map.get("data"));
        //分页代码
        mav.addObject("pageStr",HtmlUtil.getPagation("/article/"+type,Integer.parseInt(String.valueOf(map.get("count"))),currentPage,"该分类还没有数据..."));
        return mav;
    }

    /**
     * 关键字分词搜索
     */
    @RequestMapping("/search")
    public ModelAndView search(String keywords,@RequestParam(value = "page",required = false) Integer page) throws ParseException, InvalidTokenOffsetsException, org.apache.lucene.queryparser.classic.ParseException, IOException {
        if(page == null){
            page = 1;
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        //类型的html代码
        List arcTypeList = arcTypeService.listAll(Sort.Direction.ASC,"sort");
        mav.addObject("arcTypeStr", HtmlUtil.getArcTypeStr("all",arcTypeList));
        //资源列表
        List<Article> articleList = articleIndex.search(keywords);
        Integer toIndex = articleList.size() >= page*ConstUtil.PAGE_SIZE?page*ConstUtil.PAGE_SIZE:articleList.size();
        mav.addObject("articleList",articleList.subList((page-1)*ConstUtil.PAGE_SIZE,toIndex));
        mav.addObject("keywords",keywords);
        //分页代码
        int totalPage = articleList.size()%ConstUtil.PAGE_SIZE==0?articleList.size()/ConstUtil.PAGE_SIZE:articleList.size()/ConstUtil.PAGE_SIZE+1;
        String targetUrl = "/article/search?keywords="+keywords;
        String msg = "没有关键字\"<font style=\"border:0px;color:red;font-weight:bold;padding-left:3px;padding-right:3px\">"+keywords+"</font>\"的相关资源，请联系站长！";
        mav.addObject("pageStr",HtmlUtil.getPagation(targetUrl,totalPage,page,msg));
        return mav;
    }



    //资源详情显示
    @RequestMapping(value = "/detail/{articleId}")
    public ModelAndView detail(@PathVariable(value = "articleId",required = true)String articleId) throws IOException, org.apache.lucene.queryparser.classic.ParseException {
        ModelAndView mav = new ModelAndView();
        String repalce = articleId.replace(".html","");
        articleService.updateClick(Integer.parseInt(repalce));
        Article article = articleService.getById(Integer.parseInt(repalce));
        if (article.getState() != 2) {
            return null;
        }
        mav.addObject("article",article);
        //类型的html代码
        List arcTypeList = arcTypeService.listAll(Sort.Direction.ASC,"sort");
        mav.addObject("arcTypeStr", HtmlUtil.getArcTypeStr(article.getArcType().getArcTypeId().toString(),arcTypeList));
        //通过lucene分词查找相似资源
        List<Article> articleList = articleIndex.searchNoHighLightet(article.getName().replace("视频","")
                .replace("教程","").replace("下载",""));
        if (articleList != null&&articleList.size()>0) {
            mav.addObject("similarityArticleList",articleList);
        }
        mav.setViewName("/detail");
        return mav;
    }

    /**
     * 判断资源是否为免费资源
     */
    @ResponseBody
    @RequestMapping("/isFree")
    public boolean isFree(Integer articleId){
        Article article = articleService.getById(articleId);
        return article.isFree();
    }

}
