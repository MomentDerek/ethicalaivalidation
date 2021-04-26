package com.sydney.au.ethicalaivalidation.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author: Xin Lin on 11/9/2020
 * @package: com.sydney.au.ethicalaivalidation.service
 * @version: 1.0
 * <b>Description:</b>
 * <p>Add an exception handling</p>
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static Logger log = LogManager.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException{
        log.debug("Jwt authentication failed: "+authException);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Jwt authentication failed");
    }
}
