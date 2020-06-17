package cn.sicnu.ming.controller;

import cn.sicnu.ming.entity.User;
import cn.sicnu.ming.service.UserService;
import cn.sicnu.ming.util.Md5HashUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author frank ming
 * @createTime 2020061717 16:58
 * @description 用户控制层
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    public UserService userService;

    @ResponseBody
    @RequestMapping("/register")
    public Map<String,Object> register(@Valid User user, BindingResult result){
        HashMap<String, Object> map = new HashMap<>();
        if(result.hasErrors()){
            map.put("success",false);
            map.put("errorInfo",result.getFieldError().getDefaultMessage());
        }else if(userService.findByUserName(user.getUserName())!=null){
            map.put("success",false);
            map.put("errorInfo","用户名已经存在，请重新注册");
        }else if(userService.findByEmail(user.getEmail())!=null){
            map.put("success",false);
            map.put("errorInfo","邮箱已经注册，请重新注册");
        }else {
            user.setPassword(Md5HashUtil.md5x2(user.getPassword()));//md5加密
            user.setRegistrationDate(new Date());
            user.setHeadPortrait("tou.jpg");
            user.setLatelyLoginTime(new Date());
            userService.save(user);
            map.put("success",true);
        }
        return map;
    }
    @ResponseBody
    @PostMapping("/login")
    public Map<String,Object> login(User user, HttpSession httpSession){
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isEmpty(user.getUserName())){
            map.put("success",false);
            map.put("errorInfo","请正确输入用户名");
        }else if (StringUtils.isEmpty(user.getPassword())){
            map.put("success",false);
            map.put("errorInfo","请正确输入密码");
        }else {
            try {
                Subject sub = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), Md5HashUtil.md5x2(user.getPassword()));
                sub.login(token);
                String userName = SecurityUtils.getSubject().getPrincipal().toString();
                User currcentUser = userService.findByUserName(userName);
                if (currcentUser.isOff()) {
                    map.put("success", false);
                    map.put("errorInfo", "该账号被封禁，请联系管理员");
                    sub.logout();
                } else {
                    currcentUser.setRegistrationDate(new Date());
                    userService.save(currcentUser);
                    map.put("success", true);
                }
            }catch (Exception e){
                e.printStackTrace();
                map.put("success", false);
                map.put("errorInfo", "账号或密码不正确");
            }
        }
        return  map;
    }
}
