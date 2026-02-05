package com.example.exchange.appllecation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.exchange.domain.service.CurrencyRateService;

/**
 * DBから受け取ったデータをコントロールする
 */
@Controller
@RequestMapping("/rates")
public class RateListController {
	@Autowired
	private CurrencyRateService service;
	
	@GetMapping
	public String showRateList(Model model) {
		model.addAttribute("rates", service.getAllRates());
		return "rate-list";
	}
}
