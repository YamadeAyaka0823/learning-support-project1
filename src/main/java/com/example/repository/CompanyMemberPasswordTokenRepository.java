package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.CompanyMember;
import com.example.domain.CompanyMemberPasswordResetToken;

@Repository
public class CompanyMemberPasswordTokenRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public static final RowMapper<CompanyMemberPasswordResetToken> COMPANY_MEMBER_PASSWORD_RESET_TOKEN_ROW_MAPPER = (rs,i) -> {
		CompanyMember companyMember = new CompanyMember();
		companyMember.setId(rs.getInt("id"));
		companyMember.setName(rs.getString("name"));
		companyMember.setKana(rs.getString("kana"));
		companyMember.setEmail(rs.getString("email"));
		companyMember.setPassword(rs.getString("password"));
		companyMember.setCompanyId(rs.getInt("company_id"));
		CompanyMemberPasswordResetToken companyMemberPasswordResetToken = new CompanyMemberPasswordResetToken();
		companyMemberPasswordResetToken.setCompanyMember(companyMember);
		companyMemberPasswordResetToken.setToken(rs.getString("token"));
		return companyMemberPasswordResetToken;
	};
	
	/**
	 * 企業担当者パスワード変更画面で発行されたトークンをDBへinsertする.
	 * @param student
	 * @param token
	 */
	public void save(CompanyMemberPasswordResetToken myToken) {
		String sql = "INSERT INTO company_member_token (id, name, kana, email, password, company_id, token) VALUES (:id, :name, :kana, :email, :password, :companyId, :token)";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", myToken.getCompanyMember().getId()).addValue("name", myToken.getCompanyMember().getName()).addValue("kana", myToken.getCompanyMember().getKana()).addValue("email", myToken.getCompanyMember().getEmail()).addValue("password", myToken.getCompanyMember().getPassword()).addValue("companyId", myToken.getCompanyMember().getCompanyId()).addValue("token", myToken.getToken());
		template.update(sql, param);
	}
	
	/**
	 * IDとトークンを元に1件検索する.
	 * @param id
	 * @param token
	 * @return
	 */
	public CompanyMemberPasswordResetToken findByToken(String token) {
		String sql = "SELECT id, name, kana, email, password, company_id, token FROM company_member_token WHERE token = :token";
		SqlParameterSource param = new MapSqlParameterSource().addValue("token", token);
		CompanyMemberPasswordResetToken companyMemberPasswordResetToken = template.queryForObject(sql, param, COMPANY_MEMBER_PASSWORD_RESET_TOKEN_ROW_MAPPER);
		return companyMemberPasswordResetToken;
	}

}
