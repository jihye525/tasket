package com.woojhye.tasket;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
public class TasketController {

    @GetMapping("/admin")
    public String toAdmin() {
        return "/admin";
    }

    @GetMapping("/")
    public String toMain(Model model) {

        // 현재 로그인한 사용자의 ID (이메일 또는 username)
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        // 현재 사용자의 인증 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자의 권한(ROLE) 가져오기
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();  // 예: "ROLE_USER", "ROLE_ADMIN"

        // 모델에 ID와 ROLE 추가
        model.addAttribute("id", id);
        model.addAttribute("role", role);

        return "/main";
    }


}

