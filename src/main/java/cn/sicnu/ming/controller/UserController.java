package cn.sicnu.ming.controller;

import cn.sicnu.ming.entity.User;
import cn.sicnu.ming.service.UserService;
import cn.sicnu.ming.service.impl.UserServiceImpl;
import cn.sicnu.ming.util.Md5HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.awt.image.ImageProducer;
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
}
