package com.sydney.au.ethicalaivalidation.service.impl;

import com.sydney.au.ethicalaivalidation.domain.Users;
import com.sydney.au.ethicalaivalidation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Xin Lin on 11/10/2020
 * @package: com.sydney.au.ethicalaivalidation.service
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users u = userService.getUserByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(u==null){
            throw new UsernameNotFoundException("This user is not exist");
        }
        int userType = u.getUsertype();

        if(userType == 1){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+"AI SupplierAdmin"));
        }
        else if(userType == 2){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+"AI Supplier"));
        }
        else if(userType == 3){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+"Validator"));
        }
        else{
            authorities.add(new SimpleGrantedAuthority("ROLE_"+"Administrator"));
        }
        return new User(
                u.getUsername(),
                passwordEncoder.encode(u.getPassword()),
                true,true,true,true,
                authorities
        );
    }
}
