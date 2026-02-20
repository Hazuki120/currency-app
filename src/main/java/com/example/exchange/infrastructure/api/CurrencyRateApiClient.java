package com.example.exchange.infrastructure.api;

import java.math.BigDecimal;
import java.util.Map;

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
		
		Map response = restTemplate.getForObject(url, Map.class);
		
		Object result = response.get("result");
		if(result == null) {
			throw new IllegalStateException("API returned no result:" + response);
		}
		return new BigDecimal(result.toString());
	}
}
