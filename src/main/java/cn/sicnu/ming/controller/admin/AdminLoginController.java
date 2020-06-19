package cn.sicnu.ming.controller.admin;

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

        @RequestMapping("/toAdminUserCenterPage")
        public String adminLogin(){
            return "/admin/index";
        }

}
