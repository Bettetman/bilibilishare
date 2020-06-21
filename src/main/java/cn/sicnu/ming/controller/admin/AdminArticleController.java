package cn.sicnu.ming.controller.admin;

import cn.sicnu.ming.entity.Article;
import cn.sicnu.ming.entity.Message;
import cn.sicnu.ming.service.ArticleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author frank ming
 * @createTime 20200621 15:39
 * @description 资源控制类
 */
@RestController
@RequestMapping("/admin/article")
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/list")
    @RequiresPermissions("分页查询资源信息列表")
    public Map<String,Object> list(Article s_article,
                                   @RequestParam(value = "nickname",required = false)String nickname,
                                   @RequestParam(value = "publishDates",required = false)String publishDates,
                                   @RequestParam(value = "page",required = false)Integer page,
                                   @RequestParam(value = "pageSize",required = false)Integer pageSize){
        Map<String ,Object> map =new HashMap<>();
        String s_bpublishDate = null;                               //开始时间
        String s_epublishDate = null;                               //结束时间
        if(!StringUtils.isEmpty(publishDates)){
            String[] str = publishDates.split(" - ");        //拆分时间段
            s_bpublishDate = str[0];
            s_epublishDate = str[1];
        }
        map.put("data",articleService.list(s_article,nickname,s_bpublishDate,s_epublishDate,page,pageSize, Sort.Direction.DESC,"publishDate"));
        map.put("total",articleService.getCount(s_article,nickname,s_bpublishDate,s_epublishDate));
        map.put("errorNo",0);
        return map;
    }
    /**
     * 根据id查看资源信息
     */
    @RequestMapping(value = "/findById")
    @RequiresPermissions(value = "查看资源信息")
    public Map<String,Object> findById(@RequestParam(value = "articleId") Integer articleId) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> tempMap = new HashMap<>();
        Article article = articleService.getById(articleId);
        tempMap.put("articleId",article.getArticleId());
        tempMap.put("name",article.getName());
        tempMap.put("arcType",article.getArcType().getArcTypeId());
        tempMap.put("points",article.getPoints());
        tempMap.put("content",article.getContent());
        tempMap.put("download",article.getDownload());
        tempMap.put("password",article.getPassword());
        tempMap.put("click",article.getClick());
        tempMap.put("keywords",article.getKeywords());
        tempMap.put("description",article.getDescription());
        map.put("data",tempMap);
        map.put("errorNo",0);
        return map;
    }

    /**
     * 根据id批量删除资源信息
     */
    @RequestMapping(value = "/delete")
    @RequiresPermissions(value = "删除资源信息")
    public Map<String,Object> delete(@RequestParam(value = "articleId") String ids){
        Map<String ,Object> map =new HashMap<>();
        String[] idsStr = ids.split(",");
        for (int i=0;i<idsStr.length;i++){
            articleService.delete(Integer.parseInt(idsStr[i]));                         //批量删除资源
        }
        map.put("errorNo",0);
        return map;
    }
    /**
     * 审核资源
     */
    @RequestMapping(value = "/updateState")
    @RequiresPermissions(value = "审核资源")
    public Map<String,Object> updateState(Article article){
        Map<String ,Object> map =new HashMap<>();
        Article oldArticle = articleService.getById(article.getArticleId());        //查找到资源
        oldArticle.setCheckDate(new Date());
        if(article.getState()==2){                                                  //审核通过
            oldArticle.setState(2);
        }else if(article.getState()==3){                                            //审核不通过
            oldArticle.setState(3);
            oldArticle.setReason(article.getReason());                              //审核不通过原因
        }
        articleService.save(oldArticle);
        map.put("errorNo",0);
        return map;
    }

}
