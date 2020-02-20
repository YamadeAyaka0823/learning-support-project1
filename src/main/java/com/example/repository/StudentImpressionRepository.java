package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.StudentImpression;

@Repository
public class StudentImpressionRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	
	/**
	 * 受講生所感を登録するリポジトリ.
	 * @param studentImpression
	 */
	public void insert(StudentImpression studentImpression) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(studentImpression);
		String sql = "INSERT INTO student_impressions (weekly_report_id, student_name, content) VALUES (:weeklyReportId, :studentName, :content)";
		template.update(sql, param);
	}
	
	/**
	 * 週報を編集するためのリポジトリ.
	 * @param studentImpression
	 */
	public void update(StudentImpression studentImpression) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(studentImpression);
		String sql = "UPDATE student_impressions SET content = :content WHERE weekly_report_id = :weeklyReportId AND id = :id";
		template.update(sql, param);
	}

}
