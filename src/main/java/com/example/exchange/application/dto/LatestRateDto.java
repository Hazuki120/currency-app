package com.example.exchange.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LatestRateDto {
	
	private String baseCurrency;
	private String targetCurrency;
	private String rate;
	private String fetchedAtText;

}
