package com.example.exchange.application.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exchange.domain.service.CurrencyRateService;
import com.example.exchange.domain.service.UserService;

/**
 * 管理者専用機能を提供するコントローラ。
 * 
 * ・レートの全件表示/削除
 * ・ユーザ一覧表示/削除
 * 
 * 本クラスでは画面性のみを担当し、
 * 実際のビジネスロジック（削除処理など）は Service 層へ委譲している。
 * 
 * また、@PreAuthorize により ADMIN 権限を持つユーザのみ
 * アクセス可能とすることで、メソッド単位の認可制御を実現している。
 * 
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	/** レート関連の行処理を担当する */
	private CurrencyRateService rateService;
	
	/** ユーザ管理関連の業務処理を担当する */
	private UserService userService;
	
	/**
	 * コンストラクタインジェクション
	 * フィールドインジェクションではなく、コンストラクタインジェクションを採用
	 * ・テスト容易性の向上
	 * ・依存関係の明確化
	 * を意識している。
	 * @param userService
	 */
	public AdminController(CurrencyRateService rateService,
							UserService userService) {
		this.rateService = rateService;
		this.userService = userService;
	}
	
	/**
	 * 全ユーザの通貨レート履歴を表示する。
	 * 管理者は全データを閲覧可能とする。
	 * 
	 * @param model 画面へ値を渡すための Model
	 * @return 表示テンプレート名
	 */
	@GetMapping("/rates")
	public String allRates(Model model) {
		model.addAttribute("rates", rateService.findAll());
		return "admin/rates";
	}
	
	/**
	 * レート削除処理。
	 * 削除ロジックはサービス層に委譲している。
	 * 
	 * @param id 削除対象レートの ID
	 * @return 一覧画面へのリダイレクト
	 */
	@PostMapping("/rates/delete")
	public String deleteRate(@RequestParam Long id) {
		rateService.deleteById(id);
		return "redirect:/admin/rates";
	}
	
	/**
	 * 全ユーザ一覧を表示する。
	 * 
	 * @param model 画面へ値を渡すための Model
	 * @return 表示テンプレート名
	 */
	@GetMapping("/users")
	public String showUsers(Model model){
		model.addAttribute("users", userService.findAll());
		return "admin/users";
	}
	
	/**
	 * ユーザ削除処理。
	 * 管理者削除不可などの業務ルールはサービス側で制御している。
	 * 
	 * @param id 削除対象ユーザ ID
	 * @return 一覧画面へリダイレクト
	 */
	@PostMapping("/users/delete")
	public String deleteUser(@RequestParam Long id) {
		userService.deleteById(id);
		return "redirect:/admin/users";
	}
}
