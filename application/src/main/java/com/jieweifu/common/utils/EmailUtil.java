package com.jieweifu.common.utils;

import org.apache.commons.lang.RandomStringUtils;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author Jin
 * @date 2019/2/25
 */
public class EmailUtil {
    /**
     * 发送验证码
     * @param email
     * @return
     */
    public static String sendEmailCode(String email) {
        try {
            Session session = null;
            Properties props = new Properties();
            // 发送方邮件服务器地址 TODO：配置单独拿出来
            props.put("mail.smtp.host", "smtp.163.com");
            //props.setProperty("mail.transport.protocol", "smtp");
            // SMTP端口号
            props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            // 设置成需要邮件服务器认证
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true");
            session = Session.getInstance(props);
            session.setDebug(true);
            Message message = new MimeMessage(session);
            // 设置发件人 TODO: 发件人配置单独拿出来
            message.setFrom(new InternetAddress("17701642178@163.com"));
            // 设置收件人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 设置标题
            message.setSubject("英索纳-注册码");
            // 邮件内容
            String value = RandomStringUtils.random(4, false, true);
            message.setContent("<p>尊敬的用户：</p><p>您好!</p><p>您正在注册英索纳！"
                    +"您的验证码是：" + value + "。</p><p>请在2分钟内使用！</p>"
                    +"<p>江苏英索纳智能科技有限公司</p>", "text/html;charset=utf-8");
            // 发送邮件
            Transport transport = session.getTransport("smtp");
            // 以SMTP方式登录邮箱
            //transport.connect("smtp.163.com", "13013623154@163.com", "xxx");
            transport.connect("smtp.163.com", "17701642178@163.com",
                    "Richard0");
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }
}
