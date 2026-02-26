package com.example.exchange.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.exchange.application.dto.UserDto;
import com.example.exchange.domain.model.User;

/**
 * User Entity を画面表示用 DTO に変換する Mapper。
 * 
 * ・Controller から Entity を直接渡さないための変換レイヤ
 * ・パスワードなど画面に不要な情報を排除する
 * ・一覧表示などで複数件を扱う場合の List 変換も提供する
 */
@Component
public class UserMapper {
	
	/**
	 * User Entity → UserDto（画面表示用）
	 * 
	 * ・パスワードは含めない（セキュリティ対策）
	 * ・管理者画面で必要な id / username / role のみを返す
	 * 
	 * @param u User Entity
	 * @return UserDto
	 */
	public UserDto toDto(User u) {
		return new UserDto(
				u.getId(),
				u.getUsername(),
				u.getRole());
	}
	
	/**
	 * User Entity のリスト → UserDto のリスト
	 * 
	 * ・Controller でループを書かせないための補助メソッド
	 * ・一覧画面で頻繁に使うため Mapper 側で提供する
	 * 
	 * @param users User Entity のリスト
	 * @return UserDto のリスト
	 */
	public List<UserDto>toDtoList(List<User>users){
		return users.stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}
}
