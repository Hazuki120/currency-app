package com.example.exchange.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.exchange.domain.model.CurrencyRate;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
	List<CurrencyRate> findByUsername(String username);
	Optional<CurrencyRate>findTopByBaseCurrencyAndTargetCurrencyOrderByFetchedAtDesc(
			String baseCurrency,
			String targetCurency);

}
