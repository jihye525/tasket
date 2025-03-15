package com.woojhye.tasket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TasketController {

    @GetMapping("/main")
    public String toMain(){
        return "/main";
    }

    @GetMapping("/login")
    public String toLogin(){
        return "/contents/login";
    }

    @GetMapping("/mypage")
    public String toMypage(){
        return "/contents/mypage";
    }

    @GetMapping("/sign-up")
    public String toSignup(){
        return "/contents/sign-up";
    }
}
