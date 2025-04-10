package com.woojhye.tasket.user.controller;


import com.woojhye.tasket.file.domain.Profile;
import com.woojhye.tasket.file.service.FileService;
import com.woojhye.tasket.user.domain.UserEntity;
import com.woojhye.tasket.user.dto.SignUpDTO;
import com.woojhye.tasket.user.dto.UserUpdateDTO;
import com.woojhye.tasket.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController{
    private final UserService userService;
    private final FileService fileService;

    @GetMapping("/login")
    public String toLogin(Model model, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", csrfToken);
        return "contents/login";
    }

    @GetMapping("/mypage")
    public String toMyPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        model.addAttribute("user", user);
        return "contents/mypage";
    }

    @PostMapping("/mypage")
    public String updateUserInfo(@ModelAttribute UserUpdateDTO dto,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        userService.updateUser(userDetails.getUsername(), dto);

        if(!dto.getProfile().isEmpty()){
            fileService.deleteProfile(userDetails.getUsername());
            fileService.update(userDetails.getUsername(), dto.getProfile());
        }

        return "redirect:/";
    }

    @GetMapping("/sign-up")
    public String toSignUp() {
        return "contents/sign-up";
    }

    @PostMapping("/sign-up")
    public String signup(@ModelAttribute @Valid SignUpDTO userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "contents/sign-up";
        }

        if (!userCreateForm.getPassword().equals(userCreateForm.getConfirmPassword())) {
            System.out.println("2개의 패스워드가 일치하지 않습니다.");
            bindingResult.rejectValue("confirmPassword", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "contents/sign-up";
        }

        try {
            Profile storeprofile = fileService.store(userCreateForm.getProfile());
            userService.signUpProcess(userCreateForm, storeprofile);
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            System.out.println(e.getMessage()+ e.getCause());
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "contents/sign-up";
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage()+ e.getCause());
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

    @GetMapping("/withdrawal")
    public String toWithdrawal(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        model.addAttribute("user", user);
        return "contents/withdrawal";
    }

    @PostMapping("/withdrawal")
    public String deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails.getUsername());
        fileService.deleteProfile(userDetails.getUsername());
        SecurityContextHolder.clearContext(); // 로그아웃 처리
        return "redirect:/login?logout";
    }
}
