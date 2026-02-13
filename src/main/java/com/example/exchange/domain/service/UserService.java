package com.example.exchange.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.exchange.domain.User;
import com.example.exchange.domain.UserRepository;

/**
 * ユーザに関するビジネスロジックを扱う Service
 *
 * ・ユーザ登録処理
 * ・重複チェック
 * ・パスワードのハッシュ化
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ユーザ登録処理
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

        // セキュリティ対策：必ずハッシュ化
        user.setPassword(passwordEncoder.encode(password));

        user.setRole("USER");

        userRepository.save(user);

        return true;
    }
}
