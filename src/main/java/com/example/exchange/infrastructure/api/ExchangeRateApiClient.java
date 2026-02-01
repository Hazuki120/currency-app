package com.example.exchange.infrastructure.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ExchangeRateApiClient {
	@Value("${exchange.api.key}")
	private String apiKey;
	private final RestTemplate restTemplate = new RestTemplate();
	public double getRate(String base, String target) {
		String url = UriComponentsBuilder
				.fromUriString("https://api.exchangerate.host/convert")
				.queryParam("access_key", apiKey)
				.queryParam("base", base)
				.queryParam("symbols", target)
				.build()
				.toUriString();
		
		Map response = restTemplate.getForObject(url, Map.class);
		
		Map<String, Double> rates = (Map<String, Double>)response.get("rates");
		
		return rates.get(target);
	}
}
