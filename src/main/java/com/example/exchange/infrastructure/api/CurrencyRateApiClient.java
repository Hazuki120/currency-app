package com.example.exchange.infrastructure.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CurrencyRateApiClient {

	@Value("${exchange.api.key}")
	private String apiKey;

	private final RestTemplate restTemplate = new RestTemplate();

	public BigDecimal getRate(String base, String target) {

		String url = UriComponentsBuilder
				.fromUriString("https://api.exchangerate.host/convert")
				.queryParam("access_key", apiKey)
				.queryParam("from", base)
				.queryParam("to", target)
				.queryParam("amount", 1)
				.build()
				.toUriString();

		ExchangeResponse response = restTemplate.getForObject(url, ExchangeResponse.class);

		if (response == null || response.getResult() == null) {
			throw new IllegalStateException("API returned no result: " + response);
		}

		return response.getResult();
	}
}