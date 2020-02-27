package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.TrainingStudent;

@Repository
public class TrainingStudentRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<TrainingStudent> TRAINING_STUDENT_ROW_MAPPER = (rs,i) -> {
		TrainingStudent trainingStudent = new TrainingStudent();
		trainingStudent.setStudentId(rs.getInt("student_id"));
		trainingStudent.setTrainingId(rs.getInt("training_id"));
		return trainingStudent;
	};
	
	
	/**
	 * 管理者画面で生徒を登録する時、training_studentテーブルにもstudentIdとtrainingIdをinsertするためのリポジトリ.
	 * @param trainingStudent
	 */
	public void insert(TrainingStudent trainingStudent) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(trainingStudent);
		String sql = "INSERT INTO training_student (training_id, student_id) VALUES (:trainingId, :studentId)";
		template.update(sql, param);
	}
	
	public List<TrainingStudent> load(Integer trainingId){
		String sql = "SELECT training_id, student_id FROM training_student WHERE training_id = :trainingId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("trainingId", trainingId);
		List<TrainingStudent> trainingStudentList = template.query(sql, param, TRAINING_STUDENT_ROW_MAPPER);
		return trainingStudentList;
	}

}
