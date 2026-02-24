package com.example.exchange.domain.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.exchange.application.dto.CurrencyRateDto;
import com.example.exchange.application.dto.HistoryDto;
import com.example.exchange.application.mapper.CurrencyRateMapper;
import com.example.exchange.domain.model.CurrencyRate;
import com.example.exchange.domain.repository.CurrencyRateRepository;

/**
 * 通貨レート履歴の取得・削除を担当するサービス
 * 
 * 主な責務：
 * ・ユーザごとの履歴取得（論理削除を除外）
 * ・管理者用の全履歴取得
 * ・Entity → DTO 変換
 * ・論理削除および完全削除
 * 
 * Controller は DTO のみ扱い、Entity を外部へ直接公開しない設計とする。
 */
@Service
public class CurrencyRateService {

	/** 通貨レートデータアクセスを担当する Repository */
	private final CurrencyRateRepository rateRepository;
	
	/** Entity → DTO 変換を担当する Mapper */
	private final CurrencyRateMapper mapper;
	
	/** コンストラクタインジェクション */
	public CurrencyRateService(CurrencyRateRepository repository, CurrencyRateMapper mapper) {
		this.rateRepository = repository;
		this.mapper = mapper;
	}
	
	/**
	 * 指定したユーザのレート履歴を取得（論理削除を除外・新しい順）
	 * 
	 * @param username ユーザ名
	 * @param page ページ番号
	 * @param size １ページあたりの件数
	 * @return Entity のページ
	 */
	public Page<CurrencyRate> getRatesByUsername(String username, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by("fetchedAt").descending());
		
		return rateRepository.findByUsernameAndDeletedFalseOrderByFetchedAtDesc(username, pageable);
		}
	
	/**
	 * 指定ユーザのレート履歴を DTO で取得
	 * 
	 * Controller は DTO のみ扱うため、Entity → DTO 変換をここで行う。
	 * 
	 * @param username ユーザ名
	 * @param page ページ番号
	 * @param size １ページあたりの件数
	 * @return DTO のページ
	 */
	public Page<HistoryDto> getRatesDto(String username, int page, int size){
		Page<CurrencyRate> entityPage = getRatesByUsername(username, page, size);
		
		return entityPage.map(mapper::toHistoryDto);
		}

	/**
	 * 全ユーザのレート履歴を DTO で取得（管理者用）
	 * 
	 * ・論理削除済みも含めて全件取得
	 * 
	 * @param pageable ページ情報
	 * @return DTO のページ
	 */
	public Page<CurrencyRateDto> findAllDto(Pageable pageable){
		return rateRepository.findAll(pageable).map(mapper::toDto);
	}
	
	/**
	 * レート情報を論理削除する。
	 * 
	 * ・deleted = true
	 * ・deletedAt に削除日時を記録 
	 * ・deletedBy に削除実行者（ユーザ or ADMIN）を記録
	 * 
	 *  @param id 削除対象 ID
	 *  @param changeBy 削除実行者　
	 */
	public void deleteById(Long id, String changedBy) {
		CurrencyRate rate = rateRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
		rate.setDeleted(true);
		rate.setDeletedAt(LocalDateTime.now());
		rate.setDeletedBy(changedBy);
		rateRepository.save(rate);
	}
	
	/**
	 * 完全削除（物理削除）
	 * 
	 * ・管理者画面専用
	 * ・DB から完全に削除され、復元不可
	 * 
	 * @param id 削除対象 ID
	 */
	public void hardDelete(Long id) {
		rateRepository.deleteById(id);
	}
	
}
