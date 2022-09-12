/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.capstone.utility;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailSender {

    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;
    @Value("${mail.smtp.auth}")
    private String auth;
    @Value("${mail.smtp.starttls.enable}")
    private String enable;
    @Value("${mail.smtp.host}")
    private String host;
    @Value("${mail.smtp.port}")
    private String port;


    public boolean sendMailChangePassword(String to, String OTP) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", enable);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setContent(message, "text/plain; charset=UTF-8");
            message.setFrom(new InternetAddress(username));
            System.out.println(username);
            System.out.println(password);
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject("Mail đổi mật khẩu cho tài khoản School Management System");
            message.setText("Đây là mail nhận otp để đổi mật khẩu \nMã OTP là : " + OTP);

            Transport.send(message);

            System.out.println("Done");

        } catch (Exception e) {
            e.printStackTrace();
            return true;


        }
        return true;
    }

}
