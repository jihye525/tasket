package com.woojhye.tasket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TasketController {

    @GetMapping("/main")
    public String toMain(){
        return "/main";
    }
}
