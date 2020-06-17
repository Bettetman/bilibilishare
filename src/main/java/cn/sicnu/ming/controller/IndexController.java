package cn.sicnu.ming.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author frank ming
 * @createTime 2020617 12:22
 * @description 首页信息
 */
@Controller
public class IndexController {

    @RequestMapping({"/","/index"})
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;

    }
}
