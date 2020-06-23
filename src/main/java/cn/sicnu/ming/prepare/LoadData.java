package cn.sicnu.ming.prepare;

import cn.sicnu.ming.service.ArcTypeService;
import cn.sicnu.ming.service.ArticleService;
import cn.sicnu.ming.service.UserService;
import cn.sicnu.ming.util.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * @author frank ming
 * @createTime 20200619 2:15
 * @description 加载app启动需要的数据
 */
@Component("LoadData")
public class LoadData  implements CommandLineRunner {

    @Autowired
    private ServletContext application;

    @Autowired
    private ArcTypeService arcTypeService;

    @Autowired
    private ArticleService articleService;
//
//    @Autowired
//    private LinkService linkService;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        loadDate();
    }

    /**
     * 加载数据到application缓存中
     */
    public void loadDate(){
       application.setAttribute(ConstUtil.ARC_TYPE_LIST,arcTypeService.listAll(Sort.Direction.ASC,"sort"));       //所有资源分类
  //       application.setAttribute(ConstUtil.LINK_LIST,linkService.listAll(Sort.Direction.ASC,"sort"));              //所有友情链接
        application.setAttribute(ConstUtil.CLICK_ARTICLE,articleService.getHotArticle(ConstUtil.ARTICLE_NUM));                    //热门资源
        application.setAttribute(ConstUtil.NEW_ARTICLE,articleService.getNewArticle(ConstUtil.ARTICLE_NUM));                      //最新资源
        application.setAttribute(ConstUtil.RANDOM_ARTICLE,articleService.getRandomArticle(ConstUtil.ARTICLE_NUM));                //随机资源（热搜推荐）
    }
}
