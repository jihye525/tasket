package com.woojhye.tasket.user.controller;

import com.woojhye.tasket.user.dto.UserDto;
import com.woojhye.tasket.user.dto.UserRegisterRequestDto;
import com.woojhye.tasket.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/sign-up")
    public String signUpForm() {
        return "/contents/sign-up";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute UserRegisterRequestDto registerDto,
                        RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(registerDto);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/loginForm";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/sign-up";
        }
    }

    @PostMapping("/login-proc")
    public String login(@ModelAttribute UserDto userLoginDto,
                         RedirectAttributes redirectAttributes) {
        try {
            userService.loginUser(userLoginDto);
            redirectAttributes.addFlashAttribute("message", "로그인이 완료되었습니다.");
            return "redirect:/main";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/loginForm";
        }
    }
}
