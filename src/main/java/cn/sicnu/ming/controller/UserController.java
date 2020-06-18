package cn.sicnu.ming.controller;

import cn.sicnu.ming.entity.User;
import cn.sicnu.ming.service.UserService;
import cn.sicnu.ming.util.ConstUtil;
import cn.sicnu.ming.util.Md5HashUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
}

