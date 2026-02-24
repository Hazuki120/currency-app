package com.example.exchange.application.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exchange.application.dto.CurrencyRateDto;
import com.example.exchange.domain.service.CurrencyRateService;
import com.example.exchange.domain.service.UserService;

/**
 * 管理者専用機能を提供するコントローラ。
 * 
 * 提供機能：
 * ・全ユーザのレート履歴一覧表示
 * ・レートの論理削除
 * ・レートの完全削除（物理削除）
 * ・ユーザ一覧表示
 * ・ユーザ削除
 * 
 * Controller は画面遷移・Model への値設定のみを担当し、
 * 実際の削除処理などのビジネスロジックは Service層へ委譲する。
 * 
 * @PreAuthorize により ADMIN 権限を持つユーザのみアクセス可能。
 * 
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	/** レート関連の業務処理を担当する */
	private final CurrencyRateService rateService;
	
	/** ユーザ管理関連の業務処理を担当する */
	private final UserService userService;
	
	/**
	 * コンストラクタインジェクション
	 * 依存関係を明確にし、テスト容易性を高めるために採用。
	 */
	public AdminController(CurrencyRateService rateService,
							UserService userService) {
		this.rateService = rateService;
		this.userService = userService;
	}
	
	/**
	 * 全ユーザの通貨レート履歴を表示する。
	 * 管理者は論理削除済みも含めて全件閲覧可能。
	 * ページング・並び順（ID 降順）を指定して取得する。
	 * 
	 * @param page ページ番号
	 * @param size 1ページあたりの件数
	 * @param model 画面へ値を渡すための Model
	 * @return admin/rates.html
	 */
	@GetMapping("/rates")
	public String allRates(
			@RequestParam(defaultValue = "0")int page,
			@RequestParam(defaultValue = "10")int size,
			Model model) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<CurrencyRateDto> ratePage = rateService.findAllDto(pageable);
		
		model.addAttribute("ratePage", ratePage);
		model.addAttribute("rates", ratePage.getContent());
		return "admin/rates";
	}
	
	/**
	 * レート履歴の論理削除（管理者による削除）
	 * 
	 * 削除者は”ADMIN”として記録される。
	 * 実際の削除処理は CurrencyRateService に委譲。
	 * 
	 * @param id 削除対象レート ID
	 * @return レート一覧画面へリダイレクト
	 */
	@PostMapping("/rates/delete")
	public String deleteRate(@RequestParam Long id) {
		rateService.deleteById(id, "ADMIN");
		return "redirect:/admin/rates";
	}
	
	/**
	 * レート履歴の完全削除（物理削除）
	 * 
	 * 論理削除済みのデータを DB から完全に削除する。
	 * 管理者のみ実行可能。
	 * 
	 * @param id 完全削除対象レート ID
	 * @return レート一覧画面へリダイレクト
	 */
	@PostMapping("/rates/hard-delete")
	public String hardDelete(@RequestParam Long id) {
		rateService.hardDelete(id);
		return "redirect:/admin/rates";
	}
	
	/**
	 * 全ユーザ一覧を表示する。
	 * 
	 * @param model 画面へ値を渡すための Model
	 * @return admin/users.html
	 */
	@GetMapping("/users")
	public String showUsers(Model model){
		model.addAttribute("users", userService.findAll());
		return "admin/users";
	}
	
	/**
	 * ユーザ削除処理。
	 * 管理者削除不可などの業務ルール UserService で制御している。
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
