package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Admin;
import com.example.domain.AdminPasswordResetToken;

@Repository
public class AdminPasswordTokenRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public static final RowMapper<AdminPasswordResetToken> ADMIN_PASSWORD_RESET_TOKEN_ROW_MAPPER = (rs,i) -> {
		Admin admin = new Admin();
		admin.setId(rs.getInt("id"));
		admin.setName(rs.getString("name"));
		admin.setKana(rs.getString("kana"));
		admin.setEmail(rs.getString("email"));
		admin.setPassword(rs.getString("password"));
		admin.setCanShowAllCompany(rs.getBoolean("can_show_all_company"));
		AdminPasswordResetToken adminPasswordResetToken = new AdminPasswordResetToken();
		adminPasswordResetToken.setAdmin(admin);
		adminPasswordResetToken.setToken(rs.getString("token"));
		return adminPasswordResetToken;
	};
	
	/**
	 * 管理者パスワード変更画面で発行されたトークンをDBへinsertする.
	 * @param student
	 * @param token
	 */
	public void save(AdminPasswordResetToken myToken) {
		String sql = "INSERT INTO admin_token (id, name, kana, email, password, can_show_all_company, token) VALUES (:id, :name, :kana, :email, :password, :canShowAllCompany, :token)";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", myToken.getAdmin().getId()).addValue("name", myToken.getAdmin().getName()).addValue("kana", myToken.getAdmin().getKana()).addValue("email", myToken.getAdmin().getEmail()).addValue("password", myToken.getAdmin().getPassword()).addValue("canShowAllCompany", myToken.getAdmin().getCanShowAllCompany()).addValue("token", myToken.getToken());
		template.update(sql, param);
	}
	
	/**
	 * IDとトークンを元に1件検索する.
	 * @param id
	 * @param token
	 * @return
	 */
	public AdminPasswordResetToken findByToken(String token) {
		String sql = "SELECT id, name, kana, email, password, can_show_all_company, token FROM admin_token WHERE token = :token";
		SqlParameterSource param = new MapSqlParameterSource().addValue("token", token);
		AdminPasswordResetToken adminPasswordResetToken = template.queryForObject(sql, param, ADMIN_PASSWORD_RESET_TOKEN_ROW_MAPPER);
		return adminPasswordResetToken;
	}

}
