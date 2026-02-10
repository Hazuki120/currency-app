package com.example.exchange.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currency_rate")
public class CurrencyRate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String baseCurrency;
	
	@Column(nullable = false)
	private String targetCurrency;
	
	@Column(nullable = false)
	private Double rate;
	
	@Column(nullable = false)
	private LocalDateTime fetchedAt;
	
	public CurrencyRate() {}
	
	public CurrencyRate(String username, String baseCurrency, String targetCurrency, Double rate, LocalDateTime fetchedAt) {
		this.username = username;
		this.baseCurrency = baseCurrency;
		this.targetCurrency = targetCurrency;
		this.rate = rate;
		this.fetchedAt = fetchedAt;
	}
	
	public Long getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public String getTargetCurrency() {
		return targetCurrency;
	}
	public Double getRate() {
		return rate;
	}
	public LocalDateTime getFetchedAt() {
		return fetchedAt;
	}

}
