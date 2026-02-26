package com.example.exchange.application.dto;

import lombok.Getter;

/**
 * 管理者画面でユーザ情報を表示するための DTO
 * 
 * ・Entity（User）をそのまま画面に渡さないための表示専用オブジェクト
 * ・パスワードなど画面に不要な情報は含めない
 * ・一覧表示や編集画面で必要な最小限の項目のみ保持する
 */
@Getter
public class UserDto {
	
	/** ユーザ ID（管理画面での識別用） */
	private Long id;
	/** ユーザ名（ログイン ID として使用） */
	private String username;
	/** 権限（例：USER / ADMIN） */
	private String role;
	
	/**
	 * 全フィールドを設定するコンストラクタ
	 * DTO は不変オブジェクトとして扱うため setter は持たない
	 * 
	 * @param id ユーザID
	 * @param username ユーザ名
	 * @param role 権限
	 */
	public UserDto(Long id, String username, String role) {
		this.id = id;
		this.username = username;
		this.role = role;
	}

}
