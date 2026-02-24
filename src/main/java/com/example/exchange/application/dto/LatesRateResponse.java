package com.example.exchange.application.dto;

public record LatesRateResponse(String base,
		String target,
		String rate,
		String fetchedAt) {
}
