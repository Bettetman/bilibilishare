package cn.sicnu.ming.controller;

import cn.sicnu.ming.entity.ArcType;
import cn.sicnu.ming.entity.Article;
import cn.sicnu.ming.entity.User;
import cn.sicnu.ming.entity.UserDownload;
import cn.sicnu.ming.service.ArcTypeService;
import cn.sicnu.ming.service.ArticleService;
import cn.sicnu.ming.service.UserDownService;
import cn.sicnu.ming.service.UserService;
import cn.sicnu.ming.util.CheckBaiduLinkAvailableUtil;
import cn.sicnu.ming.util.ConstUtil;
import cn.sicnu.ming.util.Md5HashUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author frank ming
 * @createTime 20200617 16:58
 * @description 用户控制层
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private JavaMailSender mailSender;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArcTypeService arcTypeService;

    @Autowired
    private UserDownService userDownService;

    @ResponseBody
    @RequestMapping("/register")
    public Map<String, Object> register(@Valid User user, BindingResult result) {
        HashMap<String, Object> map = new HashMap<>();
        if (result.hasErrors()) {
            map.put("success", false);
            map.put("errorInfo", result.getFieldError().getDefaultMessage());
        } else if (userService.findByUserName(user.getUserName()) != null) {
            map.put("success", false);
            map.put("errorInfo", "用户名已经存在，请重新注册");
        } else if (userService.findByEmail(user.getEmail()) != null) {
            map.put("success", false);
            map.put("errorInfo", "邮箱已经注册，请重新注册");
        } else {
            user.setPassword(Md5HashUtil.md5x2(user.getPassword()));//md5加密
            user.setRegistrationDate(new Date());
            user.setHeadPortrait("tou.jpg");
            user.setLatelyLoginTime(new Date());
            userService.save(user);
            map.put("success", true);
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/login")
    public Map<String, Object> login(User user, HttpSession httpSession) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(user.getUserName())) {
            map.put("success", false);
            map.put("errorInfo", "请正确输入用户名");
        } else if (StringUtils.isEmpty(user.getPassword())) {
            map.put("success", false);
            map.put("errorInfo", "请正确输入密码");
        } else {
            try {
                Subject sub = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), Md5HashUtil.md5x2(user.getPassword()));
                sub.login(token);
                String userName = SecurityUtils.getSubject().getPrincipal().toString();
                User currentUser = userService.findByUserName(userName);
                if (currentUser.isOff()) {
                    map.put("success", false);
                    map.put("errorInfo", "该账号被封禁，请联系管理员");
                    sub.logout();
                } else {
                    currentUser.setRegistrationDate(new Date());
                    userService.save(currentUser);
                    httpSession.setAttribute(ConstUtil.CURRCENT_USER, currentUser);
                    map.put("success", true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                map.put("success", false);
                map.put("errorInfo", "账号或密码不正确");
            }
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/sendEmail")
    public Map<String, Object> sendEmail(String email, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        User user = userService.findByEmail(email);
        if (StringUtils.isEmpty(email)) {
            map.put("success", false);
            map.put("errorInfo", "邮件不能为空");
        } else {
            String mailCode = Md5HashUtil.getSixRandom();
            //发送邮件的信息设置
            SimpleMailMessage message = new SimpleMailMessage();         //消息构造器
            message.setFrom(ConstUtil.Mail_INFO);                        //发件人
            message.setTo(email);                                        //收件人
            message.setSubject("bilibili素材资源下载网站-用户找回密码");    //主题
            String text = "您好，" + user.getNickname() + ":这封信是由【当好一个up网站】发送的。\n" +
                    "您收到这封邮件，是由于这个邮箱地址在【当好一个up网站】被登记为用户邮箱，且该用户请求使用 Email 重置密码功能所致。\n" +
                    "\n" +
                    "重要提醒！如果您没有提交密码重置的请求或不是【当好一个up网站】的注册用户，请立即忽略并删除这封邮件。只有在您确认需要重置密码的情况下，才需要继续阅读下面的内容。\n" +
                    "\n" +
                    "密码重置验证码：" + mailCode + " ，该验证码在您提交请求后的 2分钟内有效。\n" +
                    "\n" +
                    "输入验证码以后，在打开的页面中输入新的密码后提交，您即可使用新的密码登录网站了。\n" +
                    "\n" +
                    "诚心感谢你对本网站的使用\n" +
                    "\n" +
                    "【当好一个up网站】管理员联系qq:11451287(加好友请说明来意),后期技术bug提交管理群:741922855";
            message.setText(text);                //正文内容
            mailSender.send(message);
            System.out.println(mailCode);
            //验证码存到session中去
            session.setAttribute(ConstUtil.MAIL_CODE_NAME, mailCode);
            session.setAttribute(ConstUtil.USER_ID_NAME, user.getUserId());
            map.put("success", true);
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/checkYzm")
    public Map<String, Object> checkYzm(String yzm, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(yzm)) {
            map.put("success", false);
            map.put("errorInfo", "验证码不能为空");
        } else {
            Integer userid = (Integer)session.getAttribute(ConstUtil.USER_ID_NAME);
            String mailcode = (String)session.getAttribute(ConstUtil.MAIL_CODE_NAME);
            if(!yzm.equals(mailcode)){
                map.put("success", false);
                map.put("errorInfo", "验证码不正确");
            }else {
                User user = userService.findById(userid);
                user.setPassword(Md5HashUtil.md5x2(ConstUtil.Deful_PASSWORD));
                user.setLatelyLoginTime(new Date());
                userService.save(user);
                map.put("success", true);
            }
        }
        return  map;
    }


    //后面是相关的后台跳转的
    @GetMapping("/articleManage")
    public String articleManager(){
        return "/user/articleManage";
    }

    //用户端的文章展示
    @ResponseBody
    @RequestMapping("/articleList")
    public Map<String,Object> articleList(Article s_articel, @RequestParam(value ="page",required = false)Integer page,
                                          @RequestParam(value ="limit",required = false)Integer pageSize, HttpSession session ){
        Map<String,Object> map = new HashMap<>();
        User currentUser = (User) session.getAttribute(ConstUtil.CURRCENT_USER);
        s_articel.setUser(currentUser);
        map.put("data",articleService.list(s_articel,null,null,null,page,pageSize, Sort.Direction.DESC,"publishDate"));
        map.put("count",articleService.getCount(s_articel,null,null,null));         //总记录数
        map.put("code",0);
        return map;
    }

    /**
     * 进入资源发布页面
     */
    @GetMapping("toAddArticle")
    public String toAddArticle(){
        return "/user/AddArticle";
    }


    /**
     * 添加资源
     */
    @ResponseBody
    @RequestMapping("/saveArticle")
    public Map<String,Object> saveArticle(Article article,HttpSession session) throws IOException {
        Map<String,Object> map = new HashMap<>();
        if (article.getPoints() < 0 || article.getPoints() > 10) {
            map.put("success",false);
            map.put("errorInfo","积分超出正常区间！");
            return map;
        }
        if(!CheckBaiduLinkAvailableUtil.check(article.getDownload())){
            map.put("success",false);
            map.put("errorInfo","无效的百度云链接，请检查链接再次发布");
            return map;
        }
        User currentUser = (User) session.getAttribute(ConstUtil.CURRCENT_USER);
        //资源的添加
        if(article.getArticleId()==null){
            article.setPublishDate(new Date());
            article.setUser(currentUser);
            if(article.getPoints()==0){
                article.setFree(true);                                  //积分为0 设置免费
            }
            article.setContent(article.getContent().replaceAll("<p>","").replaceAll("</p>",""));
            article.setState(1);//审核状态
            article.setPublishDate(new Date());
            article.setCheckDate(new Date());
            article.setClick(new Random().nextInt(5)+1);
            articleService.save(article);
            map.put("success",true);
        }else {
            //修改资源
            Article oldArticle = articleService.getById(article.getArticleId());    //获取实体
            if(oldArticle.getUser().getUserId().intValue()==currentUser.getUserId().intValue()){
                oldArticle.setName(article.getName());
                oldArticle.setPublishDate(new Date());
                oldArticle.setArcType(article.getArcType());
                oldArticle.setDownload(article.getDownload());
                oldArticle.setPassword(article.getPassword());
                oldArticle.setPoints(article.getPoints());
                oldArticle.setKeywords(article.getKeywords());
                oldArticle.setDescription(article.getDescription());
                oldArticle.setContent(article.getContent());
                oldArticle.setState(1);                             //用户点击修改后则需要重新审核，状态变为待审核
                articleService.save(oldArticle);
                map.put("success",true);
            }
        }
        return map;
    }
    //检查一下当前用户是否正确
    @ResponseBody
    @RequestMapping("/checkArticleUser")
    public Map<String,Object> checkArticleUser(Integer articleId,HttpSession session){
        Map<String,Object> map = new HashMap<>();
        User currentUser = (User) session.getAttribute(ConstUtil.CURRCENT_USER);
        Article article = articleService.getById(articleId);
        if (article.getUser().getUserId().intValue() == currentUser.getUserId().intValue()) {
            map.put("success",true);
        }else {
            map.put("success",false);
            map.put("errorInfo","您不是资源所有者，不能修改！");
        }
        return map;
    }
    //修改资源
    @GetMapping("/toEditArticle/{articleId}")
    public ModelAndView toEditArticle(@PathVariable(value="articleId",required = true)Integer articleId){
        ModelAndView mav = new ModelAndView();
        Article article = articleService.getById(articleId);
        mav.addObject("article",article);
        mav.setViewName("/user/editArticle");
        return mav;
    }
    //删除资源
    @ResponseBody
    @RequestMapping("/articleDelete")
    public Map<String, Object> articleDelete(Integer articleId,HttpSession session){
        Map<String,Object> map = new HashMap<>();
        User currentUser = (User) session.getAttribute(ConstUtil.CURRCENT_USER);
        Article article = articleService.getById(articleId);
        if (article.getUser().getUserId().intValue() == currentUser.getUserId().intValue()) {
            articleService.delete(articleId);
            map.put("success",true);
        }else {
            map.put("success",false);
            map.put("errorInfo","您不是资源所有者，不能删除！");
        }
        return map;
    }

    //判断某资源是否被当前用户下载过
    @ResponseBody
    @RequestMapping("/userDownloadExits")
    public boolean userDownloadExits(Integer articleId,HttpSession session){
        User currentUser = (User) session.getAttribute(ConstUtil.CURRCENT_USER);
        Integer count = userDownService.getCountByUidAndByAid(currentUser.getUserId(),articleId);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }


    //判断用户是否有足够的积分下载资源
    @ResponseBody
    @RequestMapping("/userPoints")
    public boolean userPoints(Integer points,HttpSession session){
        User currentUser = (User) session.getAttribute(ConstUtil.CURRCENT_USER);
        Integer userPoints = currentUser.getPoints();
        if(userPoints>points){             //积分足够
            return true;
        }else{                              //积分不够
            return false;
        }
    }


    //跳转到用户下载页面
    @RequestMapping("/userDownloadPage/{articleId}")
    public ModelAndView userDownloadPage(@PathVariable("articleId")Integer articleId,HttpSession session){
        Article article = articleService.getById(articleId);
        //查不到或审核不通过直接返回
        if(article==null||article.getState().intValue()!=2){
            return null;
        }
        User userCurrent = (User) session.getAttribute(ConstUtil.CURRCENT_USER);
        Integer count = userDownService.getCountByUidAndByAid(userCurrent.getUserId(),articleId);
        if (count == 0) {                                                       //未下载过
            if (!article.isFree()) {
                if (userCurrent.getPoints() - article.getPoints() < 0) {        //积分不够
                    return null;
                }
                //扣除积分，并存入数据库
                userCurrent.setPoints(userCurrent.getPoints() - article.getPoints());
                userService.save(userCurrent);
                //资源分享者获得相应积分
                User articleUser = article.getUser();
                articleUser.setPoints(articleUser.getPoints()+article.getPoints());
                userService.save(articleUser);
            }
            //保存用户下载相关信息
            UserDownload userDownload = new UserDownload();
            userDownload.setArticle(article);
            userDownload.setUser(userCurrent);
            userDownload.setDownloadDate(new Date());
            userDownService.save(userDownload);
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("article",article);
        mav.setViewName("/article/DownloadPage");
        return mav;
    }

    //vip下载
    @RequestMapping("/userVipDownloadPage/{articleId}")
    public ModelAndView userVipDownloadPage(@PathVariable("articleId")Integer articleId,HttpSession session){
        Article article = articleService.getById(articleId);
        //资源查不到或审核未通过,直接返回
        if(article==null||article.getState().intValue()!=2){
            return null;
        }
        User userCurrent = (User) session.getAttribute(ConstUtil.CURRCENT_USER);
        //用户不是VIP,直接返回
        if(!userCurrent.isVip()){
            return null;
        }
        Integer count = userDownService.getCountByUidAndByAid(userCurrent.getUserId(),articleId);
        if (count == 0) {              //未下载过
            //保存用户下载相关信息
            UserDownload userDownload = new UserDownload();
            userDownload.setArticle(article);
            userDownload.setUser(userCurrent);
            userDownload.setDownloadDate(new Date());
            userDownService.save(userDownload);
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("article",article);
        mav.setViewName("/article/DownloadPage");
        return mav;
    }
    @ResponseBody
    @RequestMapping("/isVIP")
    public boolean isVIP(HttpSession session){
        User currentUser = (User) session.getAttribute(ConstUtil.CURRCENT_USER);
        return currentUser.isVip();
    }
}

