package com.woojhye.tasket.user.dto;

import com.woojhye.tasket.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {

        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {     // 사용자의 역할을 GrantedAuthority 객체로 변환하여 리스트에 추가


            @Override
            public String getAuthority() {      // 유저의 역할을 반환

                return user.getRole();
            }
        });

        return collection;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singletonList((GrantedAuthority) () -> user.getRole());
//    }     // 개선된 코드. 람다식 사용


    // 로그인 시 필요한 정보(이메일, 비밀번호) 를 반환
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // 계정 상태(활성화 여부, 잠금 여부 등)를 관리
    // true로 두면 계정이 만료되지 않아서 편하게 사용가능
    @Override
    public boolean isAccountNonExpired() {      // 계정의 만료 여부를 확인하는 메서드
        return true;        // true를 반환하면 계정이 만료되지 않음. 계정이 일정 기간 후 만료되도록 설정하고 싶다면, false.
    }

    @Override
    public boolean isAccountNonLocked() {       // 계정이 잠겨있는지 확인하는 메서드
        return true;        // true를 반환하면 계정이 잠기지 않음. 일정 횟수 이상 로그인 실패 시 계정을 잠그는 기능을 추가할 경우 false.
    }

    @Override
    public boolean isCredentialsNonExpired() {      // 사용자의 비밀번호가 만료되었는지 확인
        return true;        // true를 반환하면 비밀번호가 만료되지 않음.
    }

    @Override
    public boolean isEnabled() {        // 계정이 활성화되어 있는지 확인하는 메서드
        return true;        // true를 반환하면 계정이 활성화됨
                        // 이메일 인증을 완료한 사용자만 로그인 가능하게 하고 싶다면, DB에서 isEnabled 필드를 관리하도록 수정 가능
    }
}