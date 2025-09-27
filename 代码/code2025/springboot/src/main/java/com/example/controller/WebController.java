package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class WebController {
    @Resource
    AdminService adminService;
    @Resource
    UserService userService;
    //表示这是一个get请求的接口

   @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbaccount=null;
        if("ADMIN".equals(account.getRole())){
            dbaccount=adminService.login(account);
        }else if("USER".equals(account.getRole())){
            dbaccount=userService.login(account);
       }else {
            throw new CustomerException("非法请求");
        }
        return Result.success(dbaccount);
   }
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.success();
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {

        if("ADMIN".equals(account.getRole())){
            adminService.updatePassword(account);
        }
        if("USER".equals(account.getRole())){
            userService.updatePassword(account);
        }
       return Result.success();
    }
}
