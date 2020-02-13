package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.TrainingStudent;

@Repository
public class TrainingStudentRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	
	/**
	 * 管理者画面で生徒を登録する時、training_studentテーブルにもstudentIdとtrainingIdをinsertするためのリポジトリ.
	 * @param trainingStudent
	 */
	public void insert(TrainingStudent trainingStudent) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(trainingStudent);
		String sql = "INSERT INTO training_student (training_id, student_id) VALUES (:trainingId, :studentId)";
		template.update(sql, param);
	}

}
