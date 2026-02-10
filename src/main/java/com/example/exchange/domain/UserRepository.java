package com.example.exchange.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	
	// username でユーザを検索するためのメソッド
	User findByUsername(String username);
}
