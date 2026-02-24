package com.example.exchange.application.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exchange.application.dto.CurrencyRateDto;
import com.example.exchange.domain.service.CurrencyRateService;

/**
 * ログインユーザのレート履歴を表示するコントローラ
 * 
 * 認証済みユーザの username を取得し、
 * Service 層を通して該当ユーザの履歴のみ取得する。
 * 
 * Controller は画面制御に専念し、
 * データ取得ロジックは Service に委譲している。
 */
@Controller
public class HistoryController {

    private final CurrencyRateService rateService;

    public HistoryController(CurrencyRateService rateService) {
        this.rateService = rateService;
    }

    /**
	 * ログイン中のユーザのレート一覧を取得し、
	 * history.html に渡す
	 * 
	 * @param user ログイン中のユーザ情報
	 * @param model 画面へ値を渡すための Model
	 * @return 表示するテンプレート名
	 */
    @GetMapping("/exchange/history")
    public String showHistory(
    		@AuthenticationPrincipal UserDetails user,
    		@RequestParam(defaultValue = "0") int page,
    		Model model) {
    	
    	String username = user.getUsername();
    	
    	Page<CurrencyRateDto> rates = rateService.getRatesDto(username, page, 10);
    	
    	// 画面へ渡す
    	model.addAttribute("rates", rates);
    	
    	return "history";	// templates/history.html
    }
}
