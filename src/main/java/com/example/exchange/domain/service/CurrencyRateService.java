package com.example.exchange.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exchange.domain.model.CurrencyRate;
import com.example.exchange.domain.repository.CurrencyRateRepository;

@Service
public class CurrencyRateService {
	@Autowired
	private CurrencyRateRepository repository;
	
	public List<CurrencyRate> getAllRates(){
		return repository.findAll();
		}
}
