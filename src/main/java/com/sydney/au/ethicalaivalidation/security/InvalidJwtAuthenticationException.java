package com.sydney.au.ethicalaivalidation.security;

import javax.naming.AuthenticationException;

/**
 * @author: Xin Lin on 11/9/2020
 * @package: com.sydney.au.ethicalaivalidation.service
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
public class InvalidJwtAuthenticationException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public InvalidJwtAuthenticationException(String e){
        super(e);
    }
}
