package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Instructor;
import com.example.domain.InstructorPasswordResetToken;

@Repository
public class InstructorPasswordTokenRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public static final RowMapper<InstructorPasswordResetToken> INSTRUCTOR_PASSWORD_RESET_TOKEN = (rs,i) -> {
		Instructor instructor = new Instructor();
		instructor.setId(rs.getInt("id"));
		instructor.setAffiliation(rs.getString("affiliation"));
		instructor.setEmail(rs.getString("email"));
		instructor.setKana(rs.getString("kana"));
		instructor.setName(rs.getString("name"));
		instructor.setPassword(rs.getString("password"));
		instructor.setRemarks(rs.getString("remarks"));
		InstructorPasswordResetToken instructorPasswordResetToken = new InstructorPasswordResetToken();
		instructorPasswordResetToken.setInstructor(instructor);
		instructorPasswordResetToken.setToken(rs.getString("token"));
		return instructorPasswordResetToken;
	};
	
	/**
	 * 講師パスワード変更画面で発行されたトークンをDBへinsertする.
	 * @param student
	 * @param token
	 */
	public void save(InstructorPasswordResetToken myToken) {
		System.out.println(myToken);
		String sql = "INSERT INTO instructor_token (id, name, kana, email, password, affiliation, remarks, token) VALUES (:id, :name, :kana, :email, :password, :remarks, :affiliation, :token)";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", myToken.getInstructor().getId()).addValue("name", myToken.getInstructor().getName()).addValue("kana", myToken.getInstructor().getKana()).addValue("email", myToken.getInstructor().getEmail()).addValue("password", myToken.getInstructor().getPassword()).addValue("remarks", myToken.getInstructor().getRemarks()).addValue("token", myToken.getToken()).addValue("affiliation", myToken.getInstructor().getAffiliation());
		template.update(sql, param);
	}
	
	/**
	 * IDとトークンを元に1件検索する.
	 * @param id
	 * @param token
	 * @return
	 */
	public InstructorPasswordResetToken findByToken(String token) {
		String sql = "SELECT id, name, kana, email, password, affiliation, remarks, token FROM instructor_token WHERE token = :token";
		SqlParameterSource param = new MapSqlParameterSource().addValue("token", token);
		InstructorPasswordResetToken instructorPasswordResetToken = template.queryForObject(sql, param, INSTRUCTOR_PASSWORD_RESET_TOKEN);
		return instructorPasswordResetToken;
	}

}
