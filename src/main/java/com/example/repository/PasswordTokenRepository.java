package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.PasswordResetToken;
import com.example.domain.Student;

@Repository
public class PasswordTokenRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public static final RowMapper<PasswordResetToken> PASSWORD_RESET_TOKEN_ROW_MAPPER = (rs,i) -> {
		Student student = new Student();
		student.setId(rs.getInt("id"));
		student.setName(rs.getString("name"));
		student.setKana(rs.getString("kana"));
		student.setEmail(rs.getString("email"));
		student.setPassword(rs.getString("password"));
		student.setCompanyId(rs.getInt("company_id"));
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setStudent(student);
		passwordResetToken.setToken(rs.getString("token"));
		return passwordResetToken;
	};
	
//	public static final RowMapper<StudentToken> STUDENT_ROW_MAPPER = (rs,i) -> {
//		StudentToken studentToken = new StudentToken();
//		studentToken.setId(rs.getInt("id"));
//		studentToken.setName(rs.getString("name"));
//		studentToken.setKana(rs.getString("kana"));
//		studentToken.setEmail(rs.getString("email"));
//		studentToken.setPassword(rs.getString("password"));
//		studentToken.setCompanyId(rs.getInt("company_id"));
//		studentToken.setToken(rs.getString("token"));
//		return studentToken;
//	};
	
	/**
	 * 生徒パスワード変更画面で発行されたトークンをDBへinsertする.
	 * @param student
	 * @param token
	 */
	public void save(PasswordResetToken myToken) {
		String sql = "INSERT INTO student_token (id, name, kana, email, password, company_id, token) VALUES (:id, :name, :kana, :email, :password, :companyId, :token)";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", myToken.getStudent().getId()).addValue("name", myToken.getStudent().getName()).addValue("kana", myToken.getStudent().getKana()).addValue("email", myToken.getStudent().getEmail()).addValue("password", myToken.getStudent().getPassword()).addValue("companyId", myToken.getStudent().getCompanyId()).addValue("token", myToken.getToken());
		template.update(sql, param);
	}
	
	/**
	 * IDとトークンを元に1件検索する.
	 * @param id
	 * @param token
	 * @return
	 */
	public PasswordResetToken findByToken(String token) {
		String sql = "SELECT id, name, kana, email, password, company_id, token FROM student_token WHERE token = :token";
		SqlParameterSource param = new MapSqlParameterSource().addValue("token", token);
		PasswordResetToken passwordResetToken = template.queryForObject(sql, param, PASSWORD_RESET_TOKEN_ROW_MAPPER);
		return passwordResetToken;
	}

}
