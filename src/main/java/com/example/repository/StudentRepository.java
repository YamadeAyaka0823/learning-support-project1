package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Student;

@Repository
public class StudentRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Student> STUDENT_ROW_MAPPER = (rs,i) -> {
		Student student = new Student();
		student.setCompanyId(rs.getInt("company_id"));
		student.setEmail(rs.getString("email"));
		student.setId(rs.getInt("id"));
		student.setKana(rs.getString("kana"));
		student.setName(rs.getString("name"));
		student.setPassword(rs.getString("password"));
		return student;
	};
	
	
	/**
	 * 受講生がログインするためのリポジトリ.
	 * @param email
	 * @param password
	 * @return
	 */
	public Student findByEmailAndPassword(String email, String password) {
		String sql = "SELECT id, name, kana, email, password, company_id FROM students WHERE email = :email AND password = :password";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email).addValue("password", password);
		List<Student> studentList = template.query(sql, param, STUDENT_ROW_MAPPER);
		if(studentList.size() == 0) {
			return null;
		}
		return studentList.get(0);
	}

}
