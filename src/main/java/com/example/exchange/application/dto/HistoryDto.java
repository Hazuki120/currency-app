package com.example.exchange.application.dto;

import java.math.BigDecimal;

public class HistoryDto {
	
	private Long id;
	private String baseCurrency;
	private String targetCurrency;
	private BigDecimal amount;
	private BigDecimal convertedAmount;
	private String rate;
	private String fetchedAtText;
	
	public HistoryDto(
			Long id,
			String baseCurrency,
			String targetCurrency,
			BigDecimal amount,
			BigDecimal convertedAmount,
			String rate,
			String fetchedAtText) {
		this.id = id;
		this.baseCurrency = baseCurrency;
		this.targetCurrency = targetCurrency;
		this.amount = amount;
		this.convertedAmount = convertedAmount;
		this.rate = rate;
		this.fetchedAtText = fetchedAtText;
	}

}
