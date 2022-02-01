package xyz.dg.dgpethome.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @program: dgpethome
 * @description: 邮件工具类
 * @author: ruihao_ji
 * @create: 2022-02-01 20:49
 **/
@Component
public class MailUtils {
    @Resource
    private JavaMailSenderImpl mailSender;
    @Value("${spring.mail.username}")
    private String mailfrom;

    // 发送简单邮件
    public void sendSimpleEmail(String mailto, String title, String content) throws MessagingException {
        //  定制邮件发送内容
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
        // SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailfrom);
        message.setTo(mailto);
        message.setSubject(title);
        message.setText(content,true);
        // 发送邮件
        mailSender.send(mimeMessage);
    }
}
