package com.example.exchange.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.exchange.domain.model.User;
import com.example.exchange.domain.repository.UserRepository;

/**
 * アプリケーション起動時に初期データを投入する設定クラス。
 * 
 * 本クラスでは、管理者ユーザ（ADMIN）が存在しない場合にのみ
 * 自動生成する処理を定義している。
 * 
 * CommandLineRunner を利用することで、
 * Spring Boot 起動完了後に初期化処理を実行している。
 */
@Configuration
public class DataInitializer {
	
	/** ログ出力用 */
	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

	/**
	 * 管理者ユーザを初期登録する Bean。
	 * 
	 * ・既に admin ユーザが存在する場合は作成しない
	 * ・パスワードは PasswordEncoder によりハッシュ化。
	 * 
	 * @param userRepository ユーザデータアクセス用リポジトリ
	 * @param passwordEncoder パスワードハッシュ化用エンコーダ
	 * @return CommandLineRunner
	 */
	@Bean
	CommandLineRunner initAdminUser(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {

			// admin ユーザが存在しない場合のみ作成
			if (userRepository.findByUsername("admin") == null) {

				User admin = new User();
				admin.setUsername("admin");

				// パスワードは必ずハッシュ化して保存
				admin.setPassword(passwordEncoder.encode("adminpass"));

				// ロールベースアクセス制御用の権限
				admin.setRole("ADMIN");

				userRepository.save(admin);
				
				logger.info("管理者ユーザを作成しました");
			}else {
				logger.info("管理者ユーザは既に存在します。");
			}
		};
	}

}
