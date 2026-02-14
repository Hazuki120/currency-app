package com.example.exchange.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.exchange.domain.model.User;
/**
 * User エンティティを操作するリポジトリ。
 * 
 * Spring Data JPA の命名規則に基づき、
 * ユーザ名による検索や存在チェックを提供する。
 */
public interface UserRepository extends JpaRepository<User, Long> {
	
	/**
	 * ユーザ名でユーザを取得
	 * 
	 * @param username ユーザ名
	 * @return User エンティティ（存在しない場合は null）
	 */
	User findByUsername(String username);
	
	/**
	 * 指定したユーザ名が既に存在するか判定
	 * 
	 * @param username ユーザ名
	 * @return 存在する場合 true 
	 */
	boolean existsByUsername(String username);
}
