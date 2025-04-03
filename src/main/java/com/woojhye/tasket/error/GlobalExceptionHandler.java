package com.woojhye.tasket.error;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception, Model model) {
        model.addAttribute("errorMessage", exception.getMessage()); // 예외 메시지를 모델에 추가
        return "/error"; // 에러 페이지 (error.html) 반환
    }
}