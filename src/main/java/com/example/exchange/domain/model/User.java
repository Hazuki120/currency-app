package com.example.exchange.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * アプリケーションのユーザ情報を表すエンティティ。
 * 
 * Spring Security の認証に使用されるユーザ名・パスワード・権限を保持する。
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
	
	/** 主キー（自動採番）*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** ユーザ名 */
	private String username;
	
	/** ハッシュ化済みパスワード */
	private String password;
	
	/** 権限（例："USER"）*/
	private String role;

}
