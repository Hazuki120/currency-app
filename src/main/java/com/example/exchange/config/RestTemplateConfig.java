package com.example.exchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate の Bean 定義クラス
 * HTTP 通信を行う RestTemplate を Spring の管理対象として登録する
 * 
 * ・RestTemplate を直接 new せず、Spring 管理下で生成する
 * ・コンストラクタインジェクションを可能にし、依存関係を明確化する
 * ・テスト時にモックへ差し替え可能にし、テスト容易性を向上される
 * ・インフラ層の設定を Config パッケージへ集約し、責務分離をする
 */
@Configuration
public class RestTemplateConfig {
	
	/**
	 * RestTemplate を Spring コンテナへ登録する。
	 * HTTP リクエスト送信用の RestTemplate インスタンスを生成
	 * 
	 * ・@Bean により Spring がライフサイクル管理を行う
	 * ・他クラスから DI により利用可能となる
	 * 
	 * @return RestTemplate インスタンス
	 */
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
