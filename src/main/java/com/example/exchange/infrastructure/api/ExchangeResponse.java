package com.example.exchange.infrastructure.api;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeResponse {
	private Boolean success;
	private BigDecimal result;

}
