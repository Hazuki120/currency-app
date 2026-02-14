package com.example.exchange.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * アプリケーションのユーザ情報を表すエンティティ。
 * 
 * Spring Security の認証に使用されるユーザ名・パスワード・権限を保持する。
 */
@Entity
@Table(name = "users")
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
	
    // --- getter / setter ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
