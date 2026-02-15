package com.example.exchange.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * ログイン成功時の遷移先を制御するクラス。
 * 
 * Spring Security のデフォルト動作では、
 * 常に同一 URL へリダイレクトされる。
 * 
 * 本クラスでは、ユーザの権限（ROLE）を判定し、
 * ・ADMIN → 管理者画面
 * ・USER  → 中将ユーザ画面
 * へ分岐させることで、ロールベースの画面制御を実現している。
 */
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	
	/**
	 * 認証成功時に呼び出されるメソッド。
	 * 
	 * @param request HTTP リクエスト
	 * @param response HTTP レスポンス
	 * @param authentication 認証情報（ログインユーザ情報を含む）
	 */
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication)
			throws IOException, ServletException{
		
		// 認証ユーザが ADMIN 権限を持っているか判定
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		
		/**
		 * contextPath を付与することで、
		 * 将来的にアプリケーションがサブパス配下
		 * （例：/app 配下）にデプロイされた場合でも
		 * 正しくリダイレクトできるよう考慮している。
		 */
		if(isAdmin) {
			response.sendRedirect(request.getContextPath() + "/admin/rates");
		}else {
			response.sendRedirect("/exchange");
		}
	}

}
