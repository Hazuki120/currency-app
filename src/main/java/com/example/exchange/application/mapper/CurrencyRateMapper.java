package com.example.exchange.application.mapper;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.exchange.application.dto.ConvertResponseDto;
import com.example.exchange.application.dto.CurrencyRateDto;
import com.example.exchange.domain.model.CurrencyRate;

@Component
public class CurrencyRateMapper {
	
	/** 画面表示用の日時フォーマット */
	private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	/**
	 * Entity → DTO 変換メソッド
	 * 
	 * 表示用フォーマット（文字列化・日時整形）をここで行うことで、
	 * Cotroller からフォーマット処理を排除する
	 * 
	 * @param e CurrencyRate Entity
	 * @return CurrencyRateDto 画面用 DTO
	 */
	public CurrencyRateDto toDto(CurrencyRate e) {
		return new CurrencyRateDto(
				e.getId(),
				e.getBaseCurrency(),
				e.getTargetCurrency(),
				e.getAmount(),
				e.getConvertedAmount(),
				e.getRate().toPlainString(),
				e.getFetchedAt().format(F)
				);				
	}
	
	public ConvertResponseDto toConvertDto(CurrencyRate e) {
		return new ConvertResponseDto(
				e.getAmount(),
				e.getBaseCurrency(),
				e.getTargetCurrency(),
				e.getConvertedAmount(),
				e.getRate().toPlainString(),
				e.getFetchedAt().format(F));
	}
	
	public CurrencyRateDto toLatestDto(CurrencyRate e) {
		if(e == null)return null;
		
		return new CurrencyRateDto(
				e.getId(),
				e.getBaseCurrency(),
				e.getTargetCurrency(),
				null,
				null,
				e.getRate().toPlainString(),
				e.getFetchedAt().format(F));
	}
}
