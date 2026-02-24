package com.example.exchange.application.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exchange.application.dto.HistoryDto;
import com.example.exchange.domain.service.CurrencyRateService;

/**
 * ログインユーザのレート履歴を表示するコントローラ
 * 
 * 提供機能：
 * ・ログイン中ユーザのレート一覧表示
 * ・履歴の論理削除
 * 
 * Controller は画面遷移と Model への値設定のみを担当し、
 * データ取得・削除などのビジネスロジックは Service に委譲。
 */
@Controller
public class HistoryController {

	/** レート履歴関連の業務処理を担当する */
    private final CurrencyRateService rateService;

    /**
     * コンストラクタインジェクション
     * 依存関係を明確にし、テスト容易性を高めるために採用。
     */
    public HistoryController(CurrencyRateService rateService) {
        this.rateService = rateService;
    }

    /**
	 * ログイン中のユーザのレート一覧を表示する。
	 * 
	 * ページ番号を受け取り、該当ユーザの履歴のみを取得して画面へ渡す。
	 * 論理削除された履歴は Service 層で除外される。
	 * 
	 * @param user ログイン中のユーザ情報
	 * @param page ページ番号
	 * @param model 画面へ値を渡すための Model
	 * @return history.html
	 */
    @GetMapping("/exchange/history")
    public String showHistory(
    		@AuthenticationPrincipal UserDetails user,
    		@RequestParam(defaultValue = "0") int page,
    		Model model) {
    	
    	String username = user.getUsername();
    	
    	// ログインユーザの履歴をページング取得
    	Page<HistoryDto> rates = rateService.getRatesDto(username, page, 10);
    	
    	// 画面へ渡す
    	model.addAttribute("rates", rates);
    	
    	return "history";	// templates/history.html
    }
    
    /**
     * レート履歴の論理削除を行う。
     * 
     * 削除者としてログイン中のユーザ名を記録する。
     * 実際の削除処理は CurrencyRateService に委譲。
     * 
     * @param id 削除対象レートID
     * @param user ログイン中のユーザ情報
     * @return 履歴一覧画面へリダイレクト
     */
    @PostMapping("/exchange/history/delete")
    public String delete(
    		@RequestParam Long id,
    		@AuthenticationPrincipal UserDetails user) {
    	
    	rateService.deleteById(id, user.getUsername());
    	return "redirect:/exchange/history";
    }
}
