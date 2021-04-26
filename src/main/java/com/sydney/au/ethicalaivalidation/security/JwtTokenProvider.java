package com.sydney.au.ethicalaivalidation.security;

import com.sydney.au.ethicalaivalidation.security.InvalidJwtAuthenticationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * @author: Xin Lin on 11/9/2020
 * @package: com.sydney.au.ethicalaivalidation.service
 * @version: 1.0
 * <b>Description:</b>
 * <p>Aim: Give a provider to the filter to handle the JwtToken-related methods.</p>
 * <p>Including create a new token, resolve the token and validate the token.</p>
 */
@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security,jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1 hour

//    @Qualifier("")
    @Autowired
    private UserDetailsService userDetailsService;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, List<String> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = new Date();
        Date validaty = new Date(now.getTime()+validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validaty)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUsername(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
    }

    // get Jwttoken body
    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    // check whether the token is expired
    public boolean validateToken(String token) throws InvalidJwtAuthenticationException {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if(claims.getBody().getExpiration().before(new Date())){
                return false;
            }
            return true;
        }catch (JwtException | IllegalArgumentException e){
            throw new InvalidJwtAuthenticationException("Expired or invalid Jwt token");
        }
    }

    // generate password token
    public String passwordToken(String username){
        Claims claims = Jwts.claims().setSubject(username);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // generate saved password
    public String savedPasswordToken(String password){
        Claims claims = Jwts.claims().setSubject(password);
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public String resolvePassword(String password){
        String resolvedPassword = password.substring(7, password.length());
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(resolvedPassword).getBody().getSubject();
    }

}
