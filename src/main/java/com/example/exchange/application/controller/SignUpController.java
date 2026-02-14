package com.example.exchange.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exchange.domain.service.UserService;

/**
 * ユーザ登録（サインアップ）を担当するコントローラ
 * 
 * Controller は画面制御に専念し、
 * 登録処理のロジックは Service 層に委譲している。
 */
@Controller
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    /**
     * サインアップ画面を表示
     * 
     * @return 表示するテンプレート名
     */
    @GetMapping("/signup")
    public String showSignUpForm() {
        return "signup";
    }

    /**
     * ユーザ登録処理を実行
     * 
     * @param username 入力されたユーザ名
     * @param password 入力されたパスワード
     * @return リダイレクト先 URL
     */
    @PostMapping("/signup")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password) {

        boolean success = userService.registerUser(username, password);

        // 登録失敗時はエラー付きでサインアップ画面へ戻す
        if (!success) {
            return "redirect:/signup?error";
        }

        // 登録成功時はログイン画面へ
        return "redirect:/login?registered";
    }
}
