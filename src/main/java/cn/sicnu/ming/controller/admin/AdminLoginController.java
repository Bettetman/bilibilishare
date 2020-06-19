package cn.sicnu.ming.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author frank ming
 * @createTime 20200620 0:48
 * @description 管理员控制类
 */
@Controller
public class AdminLoginController {

        @RequiresPermissions("进入管理界面")
        @RequestMapping("/toAdminUserCenterPage")
        public String adminLogin(){
            return "/admin/index";
        }

}
