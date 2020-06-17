package cn.sicnu.ming.config;

import cn.sicnu.ming.util.ConstUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author frank ming
 * @createTime 20200618 0:00
 * @description 邮件配置类
 */
@Configuration
public class MailConfig {

    @Bean
    public MailSender mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(ConstUtil.MAIL_HOST);                  //指定又来发送Email的邮件服务器主机名
        mailSender.setPort(587);                            //默认端口，标准的SMTP端口
        mailSender.setUsername(ConstUtil.Mail_INFO);        //用户名
        mailSender.setPassword(ConstUtil.MAIL_PSWORD);         //密码
        return mailSender;
    }
}
