package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Instructor;
import com.example.domain.Student;
import com.example.domain.Training;
import com.example.domain.TrainingStudent;

@Repository
public class TrainingRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final ResultSetExtractor<List<Training>> TRAINING_RESULT_SET_EXTRACTOR = (rs) -> {
		List<Training> trainingList = new ArrayList<>();
		List<Student> studentList = null;
		
		int preId = -1;
		
		while(rs.next()) {
			int id = rs.getInt("A_id");
			
			if(id != preId) {
				Training training = new Training();
				training.setId(rs.getInt("A_id"));
				training.setEndDate(rs.getDate("A_end_date"));
				training.setInstructorId(rs.getInt("A_instructor_id"));
				training.setName(rs.getString("A_name"));
				training.setStartDate(rs.getDate("A_start_date"));
				training.setSubInstructorId1(rs.getInt("A_sub_instructor_id1"));
				training.setSubInstructorId1(rs.getInt("A_sub_instructor_id2"));
				training.setSubInstructorId1(rs.getInt("A_sub_instructor_id3"));
				
				Instructor instructor = new Instructor();
				instructor.setAffiliation(rs.getString("D_affiliation"));
				instructor.setEmail(rs.getString("D_email"));
				instructor.setId(rs.getInt("D_id"));
				instructor.setKana(rs.getString("D_kana"));
				instructor.setName(rs.getString("D_name"));
				instructor.setPassword(rs.getString("D_password"));
				instructor.setRemarks(rs.getString("D_remarks"));
				
				training.setInstructor(instructor);
				studentList = new ArrayList<>();
				training.setStudentList(studentList);
				trainingList.add(training);
			}
			
			Integer middleCheckId = rs.getInt("B_training_id");
			
			if(middleCheckId != 0) {
				TrainingStudent trainingStudent = new TrainingStudent();
				trainingStudent.setStudentId(rs.getInt("B_student_id"));
				trainingStudent.setTrainingId(rs.getInt("B_training_id"));
				
				Student student = new Student();
				student.setCompanyId(rs.getInt("C_company_id"));
				student.setEmail(rs.getString("C_email"));
				student.setId(rs.getInt("C_id"));
				student.setKana(rs.getString("C_kana"));
				student.setName(rs.getString("C_name"));
				student.setPassword(rs.getString("C_password"));
				
				student.setTrainingList(trainingList);
				studentList.add(student);
			}
			
			preId = id;
		}
		
		return trainingList;
	};
	
	/**
	 * TrainingとTrainingStudentとStudentテーブルを結合.
	 * @return
	 */
	private String join3Table() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		sb.append(" A.id A_id, ");
	    sb.append(" A.end_date A_end_date, ");
	    sb.append(" A.instructor_id A_instructor_id, ");
	    sb.append(" A.name A_name, ");
	    sb.append(" A.start_date A_start_date, ");
	    sb.append(" A.sub_instructor_id1 A_sub_instructor_id1, ");
	    sb.append(" A.sub_instructor_id2 A_sub_instructor_id2, ");
	    sb.append(" A.sub_instructor_id3 A_sub_instructor_id3, ");
	    sb.append(" B.student_id B_student_id, ");
	    sb.append(" B.training_id B_training_id, ");
	    sb.append(" C.id C_id, ");
	    sb.append(" C.company_id C_company_id, ");
	    sb.append(" C.email C_email, ");
	    sb.append(" C.kana C_kana, ");
	    sb.append(" C.name C_name, ");
	    sb.append(" C.password C_password, ");
	    sb.append(" D.affiliation D_affiliation, ");
	    sb.append(" D.email D_email, ");
	    sb.append(" D.name D_name, ");
	    sb.append(" D.kana D_kana, ");
	    sb.append(" D.id D_id, ");
	    sb.append(" D.password D_password, ");
	    sb.append(" D.remarks D_remarks ");
	    sb.append(" FROM ");
	    sb.append(" trainings A ");
	    sb.append(" INNER JOIN ");
	    sb.append(" training_student B ");
	    sb.append(" ON A.id = B.training_id ");
	    sb.append(" INNER JOIN ");
	    sb.append(" students C ");
	    sb.append(" ON B.student_id = C.id ");
	    sb.append(" INNER JOIN ");
	    sb.append(" instructors D ");
	    sb.append(" ON D.id = A.instructor_id ");
	    
	    String JoinSql = sb.toString();
	    return JoinSql;
	}
	
	/**
	 * トレーニングを1件検索するリポジトリ.
	 * @param id
	 * @return
	 */
	public Training load(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append(join3Table());
		sql.append(" WHERE A.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Training> trainingList = template.query(sql.toString(), param, TRAINING_RESULT_SET_EXTRACTOR);
		if(trainingList.size() == 0) {
			return null;
		}
		return trainingList.get(0);
	}
	
	/**
	 * 研修を講師IDで1件検索するリポジトリ.
	 * @param id
	 * @return
	 */
	public Training instructorIdLoad(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append(join3Table());
		sql.append(" WHERE A.instructor_id = :instructorId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("instructorId", id);
		List<Training> trainingList = template.query(sql.toString(), param, TRAINING_RESULT_SET_EXTRACTOR);
		if(trainingList.size() == 0) {
			return null;
		}
		return trainingList.get(0);
	}

}
