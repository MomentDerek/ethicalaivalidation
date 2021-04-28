package com.sydney.au.ethicalaivalidation.controller;

import com.sydney.au.ethicalaivalidation.service.AdminUserService;
import com.sydney.au.ethicalaivalidation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final UserService userService;

    public AdminUserController(AdminUserService adminUserService, UserService userService) {
        this.adminUserService = adminUserService;
        this.userService = userService;
    }

    private boolean checkIsNotAdmin(UserDetails userDetails) {
        return !userService.getUserType(userDetails.getUsername()).equals(4);
    }

    @GetMapping("/userlist")
    public @ResponseBody
    ResponseEntity<Object> listAllUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Map<String, Object>> res = adminUserService.listAllUser();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{usertype}")
    public @ResponseBody
    ResponseEntity<Object> listAllUserByType(@PathVariable("usertype") String userType,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Map<String, Object>> res = adminUserService.listAllUserByType(userType);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/userlist/{username}")
    public @ResponseBody
    ResponseEntity<Object> getUserDetail(@AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Map<String, Object>> res = adminUserService.listAllUser();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
