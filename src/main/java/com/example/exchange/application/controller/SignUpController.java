package com.example.exchange.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exchange.domain.service.UserService;

/**
 * ログインユーザのレート履歴を表示するコントローラ
 * 
 * 認証済みユーザの username を取得し、
 * Service 層を通して該当ユーザの履歴のみ取得する。
 * 
 * Controller は「画面制御のみ」を担当し、
 * データ取得ロジックは Service に委譲している。
 */
@Controller
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignUpForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password) {

        boolean success = userService.registerUser(username, password);

        if (!success) {
            return "redirect:/signup?error";
        }

        return "redirect:/login?registered";
    }
}
