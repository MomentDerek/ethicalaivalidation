package com.sydney.au.ethicalaivalidation.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @author: Xin Lin on 11/9/2020
 * @package: com.sydney.au.ethicalaivalidation.service
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
@Component
public interface AsynchronousService {
    @Async
    public void sendEmailVerifyEmail(String username, String useremail, String token);

    @Async
    public void sendEmailResetPassword(String username, String useremail, String token);
}
