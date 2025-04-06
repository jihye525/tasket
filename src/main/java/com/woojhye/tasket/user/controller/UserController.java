package com.woojhye.tasket.user.controller;

import com.woojhye.tasket.user.dto.SignUpDTO;
import com.woojhye.tasket.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @PostMapping("/sign-up")
    public String signup(@Valid SignUpDTO userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "contents/sign-up";
        }

        if (!userCreateForm.getPassword().equals(userCreateForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "contents/sign-up";
        }

        try {
            userService.signUpProcess(userCreateForm);
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "contents/sign-up";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "contents/sign-up";
        }

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
