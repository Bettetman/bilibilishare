package cn.sicnu.ming.controller.admin;

import cn.sicnu.ming.entity.Article;
import cn.sicnu.ming.service.ArticleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
