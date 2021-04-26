package com.sydney.au.ethicalaivalidation.security;

import com.sydney.au.ethicalaivalidation.security.JwtConfigurer;
import com.sydney.au.ethicalaivalidation.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author: Xin Lin on 11/9/2020
 * @package: com.sydney.au.ethicalaivalidation.service
 * @version: 1.0
 * <b>Description:</b>
 * <p>This class is used for user login setting and handle the Http settings.
 *  * It uses the WebSecurityConfigurerAdapter interface.</p>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    /**
     * <p>Configure the Http requests and user login process</p>
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/home").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/signup").permitAll()
                .antMatchers(HttpMethod.POST,"/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/verifyemail/**").permitAll()
                .antMatchers(HttpMethod.GET,"/userinfo").authenticated()
                .antMatchers(HttpMethod.PUT, "/userinfo").authenticated()
                .antMatchers(HttpMethod.GET, "/resetpassword/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/resetpassword/newpassword/**").permitAll()
                .antMatchers(HttpMethod.POST, "/resetpassword").permitAll()
                .antMatchers(HttpMethod.PUT, "/resetimage").authenticated()
                .anyRequest().permitAll()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
