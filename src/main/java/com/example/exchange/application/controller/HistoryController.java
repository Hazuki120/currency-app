package com.example.exchange.application.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.exchange.domain.model.CurrencyRate;
import com.example.exchange.domain.service.CurrencyRateService;

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
public class HistoryController {

    private final CurrencyRateService rateService;

    public HistoryController(CurrencyRateService rateService) {
        this.rateService = rateService;
    }

    /**
	 * ログイン中のユーザの履歴一覧を取得し、
	 * rate-list.html に渡す
	 * 
	 * @param user ログイン中のユーザ情報
	 * @param model 画面へ値を渡すための Model
	 * @return 表示するテンプレート名
	 */
    @GetMapping("/exchange/history") // URLを統一
    public String showHistory(
            @AuthenticationPrincipal UserDetails user,
            Model model) {

        String username = user.getUsername();

        List<CurrencyRate> rates = rateService.getRatesByUsername(username);

        model.addAttribute("rates", rates);

        return "history"; // templates/history.html
    }
}
