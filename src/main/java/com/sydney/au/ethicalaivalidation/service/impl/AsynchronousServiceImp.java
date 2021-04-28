package com.sydney.au.ethicalaivalidation.service.impl;

import com.sydney.au.ethicalaivalidation.service.AsynchronousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @author: Xin Lin on 11/9/2020
 * @package: com.sydney.au.ethicalaivalidation.service
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
@Component
public class AsynchronousServiceImp implements AsynchronousService {
    @Autowired
    private JavaMailSender mailSender;

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.office365.com");
        mailSender.setPort(587);

        mailSender.setUsername("ethicalaivalidation@hotmail.com");
        mailSender.setPassword("ABCDEthical123123");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.debug","true");

        return  mailSender;
    }


    @Override
    @Async
    public void sendEmailVerifyEmail(String username, String useremail, String token){
        String emaillink = "http://localhost:3000/verifyemail/"+username+"/"+useremail+"/"+token;
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("ethicalaivalidation@hotmail.com");
            helper.setTo(useremail);
            helper.setSubject("Ethical AI Validation: Verify Your Email");
            helper.setText(
                    "<html><body><h2>Welcome to Ethical AI Validation Platform!</h2><p><h3>Please use the following link to verify your email.</h3></p>" +
                            "<p><a href='" + emaillink + "'>" +emaillink+
                            "</a></p></body></html>",
                    true);

            mailSender.send(mimeMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void sendEmailResetPassword(String username, String useremail, String token) {
        String emaillink = "http://localhost:3000/resetpassword/newpassword/"+username+"/"+token;
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("ethicalaivalidation@hotmail.com");
            helper.setTo(useremail);
            helper.setSubject("Ethical AI Validation: Reset Password Link");
            helper.setText(
                    "<html><body><h2>Reset Password</h2><p><h3>Please use the following link to reset your password.</h3></p>" +
                            "<p><a href='" + emaillink + "'>" +emaillink+
                            "</a></p></body></html>",
                    true);

            mailSender.send(mimeMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
