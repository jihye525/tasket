package com.woojhye.tasket.user.controller;

import com.woojhye.tasket.user.dto.SignUpDTO;
import com.woojhye.tasket.user.service.SignUpService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private SignUpService signUpService;

    @GetMapping("/login")
    public String toLogin() {

        return "/contents/login";
    }


    @GetMapping("/sign-up")
    public String toSignUp() {

        return "/contents/sign-up";
    }


    @PostMapping("/sign-up-proc")
    public String signUpProcess(SignUpDTO signUpDTO) {

        System.out.println(signUpDTO.getEmail());

        signUpService.signUpProcess(signUpDTO);

        return "redirect:/contents/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/login";
    }
}
