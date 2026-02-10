package com.example.exchange.appllecation.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.exchange.domain.repository.CurrencyRateRepository;

/**
 * DBから受け取ったデータをコントロールする
 */
@Controller
@RequestMapping("/rates")
public class RateListController {
	private final CurrencyRateRepository rateRepository;
	
	public RateListController(CurrencyRateRepository rateRepository) {
		this.rateRepository = rateRepository;
	}
	
	@GetMapping
	public String showRateList (
		@AuthenticationPrincipal UserDetails user,
		Model model) {
		String username = user.getUsername();
		model.addAttribute("rates",
				rateRepository.findByUsername(username));
		
		return "rate-list";
	}
}
