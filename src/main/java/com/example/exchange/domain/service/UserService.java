package com.example.exchange.domain.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.exchange.domain.model.User;
import com.example.exchange.domain.repository.UserRepository;

/**
 * ユーザに関するビジネスロジックを担当するサービス。
 *
 * ・ユーザ登録処理
 * ・重複チェック
 * ・パスワードのハッシュ化
 * ・ユーザ削除時の業務ルール制御
 * 
 * コントローラからは業務処理を直接記述せず、
 * 本サービスへ委譲する設計とすることで責務分離を意識している。
 */
@Service
public class UserService {

	/** ユーザデータアクセスを担当する */
	private final UserRepository userRepository;
	
	/** パスワードハッシュ化用 */
    private final PasswordEncoder passwordEncoder;

    /** コンストラクタインジェクション */
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ユーザ登録処理。
     * 
     * 業務ルール
     * ・既存ユーザ名の場合は登録不可
     * ・パスワードは必ずハッシュ化
     * ・登録時のロールは USER 固定
     *
     * @param username ユーザ名
     * @param password 平文パスワード
     * @return 登録成功なら true、既存ユーザなら false
     */
    public boolean registerUser(String username, String password) {

        // 既存ユーザチェック
        if (userRepository.existsByUsername(username)) {
            return false;
        }

        User user = new User();
        user.setUsername(username);

        // セキュリティ対策：パスワードは必ずハッシュ化して保存
        user.setPassword(passwordEncoder.encode(password));

        // 権限は固定で USER を付与
        user.setRole("USER");

        userRepository.save(user);

        return true;
    }
    
    /**
     * 本ユーザ一覧取得（管理者画面用）
     *  
     * @return ユーザ一覧
     */
    public List<User> findAll(){
    	return userRepository.findAll();
    }
    
    /**
     * ユーザ削除処理。
     * 
     * 業務ルール：
     * ・存在しないユーザは削除不可
     * ・ADMIN ロールは削除不可
     * 
     * 削除可否の判断は本サービス層で制御している。
     * 
     * @param id 削除対象ユーザ ID
     */
    public void deleteById(Long id) {
    	User user = userRepository.findById(id)
    			.orElseThrow(() -> new RuntimeException("ユーザが存在しません"));
    	
    	// 管理者削除禁止（業務ルール）
    	if(user.getRole().equals("ADMIN")) {
    		throw new RuntimeException("管理者は削除できません");
    	}
    	
    	userRepository.delete(user);
    }
}
