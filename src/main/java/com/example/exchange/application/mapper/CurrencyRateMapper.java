package com.example.exchange.application.mapper;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.exchange.application.dto.ConvertResponseDto;
import com.example.exchange.application.dto.CurrencyRateDto;
import com.example.exchange.application.dto.HistoryDto;
import com.example.exchange.domain.model.CurrencyRate;

/**
 * CurrencyRate Entity を画面表示用 DTO に変換する Mapper
 * 
 * ・Controller からフォーマット処理を排除し、責務を明確化する。
 * ・Entity → DTO の変換ロジックを一っか所に集約することで、
 * 	表示仕様の変更に強い構造を実現する。
 */
@Component
public class CurrencyRateMapper {

	/** 画面表示用の日時フォーマット */
	private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	/**
	 * Entity → CurrencyRateDTO（管理者画面・一覧表示用）
	 * 
	 * ・username を含む
	 * ・論理削除情報（deleted / deletedAt / deletedBy）を含む
	 * ・fetchedAt はフォーマット済み文字列として渡す
	 * 
	 * @param e CurrencyRate Entity
	 * @return CurrencyRateDto
	 */
	public CurrencyRateDto toDto(CurrencyRate e) {
		return new CurrencyRateDto(
				e.getId(),
				e.getUsername(),
				e.getBaseCurrency(),
				e.getTargetCurrency(),
				e.getAmount(),
				e.getConvertedAmount(),
				e.getRate().toPlainString(),
				e.getFetchedAt().format(F),
				e.isDeleted(),
				e.getDeletedAt(),
				e.getDeletedBy());
	}

	/**
	 * Entity → ConvertResponseDto（変換結果画面用）
	 * 
	 * ・金額・レート・日時のみを返す軽量 DTO
	 * ・削除情報は不要
	 * 
	 * @param e CurrencyRate Entity
	 * @return ConvertResponseDto
	 */
	public ConvertResponseDto toConvertDto(CurrencyRate e) {
		return new ConvertResponseDto(
				e.getAmount(),
				e.getBaseCurrency(),
				e.getTargetCurrency(),
				e.getConvertedAmount(),
				e.getRate().toPlainString(),
				e.getFetchedAt().format(F));
	}

	/**
	 * Entity → CurrencyRateDto（最新レート取得用）
	 * 
	 * ・amount / convertedAmount は不要のため null
	 * ・削除情報は保持する（管理者画面で使用される可能性があるため）
	 * 
	 * @param e CurrencyRate Entity
	 * @return CurrencyRateDto
	 */
	public CurrencyRateDto toLatestDto(CurrencyRate e) {
		if (e == null)
			return null;

		return new CurrencyRateDto(
				e.getId(),
				e.getUsername(),
				e.getBaseCurrency(),
				e.getTargetCurrency(),
				null,	// amount は不要
				null,	// convertedAmount も不要
				e.getRate().toPlainString(),
				e.getFetchedAt().format(F),
				e.isDeleted(),
				e.getDeletedAt(),
				e.getDeletedBy());
	}

	/**
	 * Entity → HistoryDto（ユーザの履歴画面用）
	 * 
	 * ・削除情報は含めない（Service 層で除外済み）
	 * ・fetchedAt はフォーマット済み文字列として渡す
	 * 
	 * @param e CurrencyRate Entity
	 * @return HistoryDto
	 */
	public HistoryDto toHistoryDto(CurrencyRate e) {
		return new HistoryDto(
				e.getId(),
				e.getBaseCurrency(),
				e.getTargetCurrency(),
				e.getAmount(),
				e.getConvertedAmount(),
				e.getRate().toPlainString(),
				e.getFetchedAt().format(F));
	}
}
