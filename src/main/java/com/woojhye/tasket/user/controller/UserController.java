package com.woojhye.tasket.user.controller;

import com.woojhye.tasket.user.dto.SignUpDTO;
import com.woojhye.tasket.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController{
    private final UserService userService;

    @GetMapping("/login")
    public String toLogin(Model model, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", csrfToken);
        return "contents/login";
    }

    @GetMapping("/sign-up")
    public String toSignUp() {
        return "contents/sign-up";
    }

    @PostMapping("/sign-up-proc")
    public String signUpProcess(SignUpDTO signUpDTO) {

        System.out.println(signUpDTO.getEmail());

        userService.signUpProcess(signUpDTO);

        return "redirect:/login";
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
