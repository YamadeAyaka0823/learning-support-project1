package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Instructor;
import com.example.domain.Training;

@Repository
public class InstructorRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Instructor> INSTRUCTOR_ROW_MAPPER = (rs,i) -> {
		Instructor instructor = new Instructor();
		instructor.setAffiliation(rs.getString("affiliation"));
		instructor.setEmail(rs.getString("email"));
		instructor.setId(rs.getInt("id"));
		instructor.setKana(rs.getString("kana"));
		instructor.setName(rs.getString("name"));
		instructor.setPassword(rs.getString("password"));
		instructor.setRemarks(rs.getString("remarks"));
		return instructor;
	};
	
	private static final ResultSetExtractor<List<Instructor>> INSTRUCTOR_RESULT_SET_EXTRACTOR = (rs) -> {
		List<Instructor> instructorList = new ArrayList<>();
		List<Training> trainingList = null;
		
		int preId = -1;
		
		while(rs.next()) {
			int id = rs.getInt("B_id");
			
			if(id != preId) {
				Instructor instructor = new Instructor();
				instructor.setAffiliation(rs.getString("B_affiliation"));
				instructor.setEmail(rs.getString("B_email"));
				instructor.setId(rs.getInt("B_id"));
				instructor.setKana(rs.getString("B_kana"));
				instructor.setName(rs.getString("B_name"));
				instructor.setPassword(rs.getString("B_password"));
				instructor.setRemarks(rs.getString("B_remarks"));
				
				trainingList = new ArrayList<>();
				instructor.setTrainingList(trainingList);
				instructorList.add(instructor);
			}
			
			Integer middleCheckId = rs.getInt("A_id");
			
			if(middleCheckId != 0) {
				Training training = new Training();
				training.setEndDate(rs.getDate("A_end_date"));
				training.setId(rs.getInt("A_id"));
				training.setInstructorId(rs.getInt("A_instructor_id"));
				training.setName(rs.getString("A_name"));
				training.setStartDate(rs.getDate("A_start_date"));
				training.setSubInstructorId1(rs.getInt("A_sub_instructor_id1"));
				training.setSubInstructorId2(rs.getInt("A_sub_instructor_id2"));
				training.setSubInstructorId3(rs.getInt("A_sub_instructor_id3"));
				
				trainingList.add(training);
			}
			preId = id;
		}
		return instructorList;
	};
	
	/**
	 * テーブル結合.
	 * @return
	 */
	private String joinTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append(" A.id A_id, ");
		sb.append(" A.start_date A_start_date, ");
		sb.append(" A.end_date A_end_date, ");
		sb.append(" A.name A_name, ");
		sb.append(" A.instructor_id A_instructor_id, ");
		sb.append(" A.sub_instructor_id1 A_sub_instructor_id1, ");
		sb.append(" A.sub_instructor_id2 A_sub_instructor_id2, ");
		sb.append(" A.sub_instructor_id3 A_sub_instructor_id3, ");
		sb.append(" B.id B_id, ");
		sb.append(" B.email B_email, ");
		sb.append(" B.name B_name, ");
		sb.append(" B.kana B_kana, ");
		sb.append(" B.password B_password, ");
		sb.append(" B.remarks B_remarks, ");
		sb.append(" B.affiliation B_affiliation ");
		sb.append(" FROM trainings A ");
		sb.append(" INNER JOIN ");
		sb.append(" instructors B ");
		sb.append(" ON A.instructor_id = B.id ");
		
		String joinSql = sb.toString();
		return joinSql;
	}
	
	/**
	 * 講師がログインするためのリポジトリ.
	 * @param email
	 * @param password
	 * @return
	 */
	public Instructor findByEmailAndPassword(String email, String password) {
		String sql = "SELECT id, name, kana, email, password, affiliation, remarks FROM instructors WHERE email = :email AND password = :password";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email).addValue("password", password);
		List<Instructor> instructorList = template.query(sql, param, INSTRUCTOR_ROW_MAPPER);
		if(instructorList.size() == 0) {
			return null;
		}
		return instructorList.get(0);
	}
	
	/**
	 * 講師1人に対する研修一覧のためのリポジトリ.
	 * @param id
	 * @return
	 */
	public Instructor load(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable());
		sql.append(" WHERE B.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Instructor> instructorList = template.query(sql.toString(), param, INSTRUCTOR_RESULT_SET_EXTRACTOR);
		if(instructorList.size() == 0) {
			return null;
		}
		return instructorList.get(0);
	}
	

}
