package cn.sicnu.ming.controller.admin;

import cn.sicnu.ming.entity.User;
import cn.sicnu.ming.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author frank ming
 * @createTime 20200624 21:17
 * @description 用户控制类
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/list")
    @RequiresPermissions("分页查询用户信息列表")
    public Map<String,Object> list(User user, @RequestParam(value = "latelyLoginTimes",required = false)String latelyLoginTimes,
                                   @RequestParam(value ="page",required = false)Integer page,
                                   @RequestParam(value ="pageSize",required = false)Integer pageSize){
        Map<String ,Object> map =new HashMap<>();
        String s_blatelyLoginTime = null;                               //开始时间
        String s_elatelyLoginTime = null;                               //结束时间
        if(!StringUtils.isEmpty(latelyLoginTimes)){
            String[] str = latelyLoginTimes.split(" - ");        //拆分时间段
            s_blatelyLoginTime = str[0];
            s_elatelyLoginTime = str[1];
        }
        map.put("data",userService.list(user,s_blatelyLoginTime,s_elatelyLoginTime,page,pageSize, Sort.Direction.DESC,"registrationDate"));
        map.put("total",userService.getCount(user,s_blatelyLoginTime,s_elatelyLoginTime));
        map.put("errorNo",0);
        return map;
    }

}
