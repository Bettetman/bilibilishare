package cn.sicnu.ming.controller;

import cn.sicnu.ming.service.ArcTypeService;
import cn.sicnu.ming.service.ArticleService;
import cn.sicnu.ming.util.ConstUtil;
import cn.sicnu.ming.util.HtmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * @author frank ming
 * @createTime 2020617 12:22
 * @description 首页信息
 */
@Controller
public class IndexController {

    @Autowired
    private ArcTypeService arcTypeService;

    @Autowired
    private ArticleService articleService;

    /**
     * 首页
     */
    @RequestMapping(value = "/")
    public ModelAndView index(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        //类型的html代码
        List arcTypeList = arcTypeService.listAll(Sort.Direction.ASC,"sort");
        mav.addObject("arcTypeStr", HtmlUtil.getArcTypeStr("all",arcTypeList));
        return mav;
    }

}
