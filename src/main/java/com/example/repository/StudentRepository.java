package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.domain.Instructor;
import com.example.domain.Student;
import com.example.domain.Training;
import com.example.domain.TrainingStudent;

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
	
	private static final ResultSetExtractor<List<Student>> STUDENT_RESULT_SET_EXTRACTOR = (rs) -> {
		List<Student> studentList = new ArrayList<>();
		List<Training> trainingList = null;
		
		int preId = -1;
		
		while(rs.next()) {
			int id = rs.getInt("A_id");
			
			if(id != preId) {
				Student student = new Student();
				student.setCompanyId(rs.getInt("A_company_id"));
				student.setEmail(rs.getString("A_email"));
				student.setId(rs.getInt("A_id"));
				student.setKana(rs.getString("A_kana"));
				student.setName(rs.getString("A_name"));
				student.setPassword(rs.getString("A_password"));
				
				trainingList = new ArrayList<>();
				student.setTrainingList(trainingList);
				studentList.add(student);
			}
			
			Integer middleCheckId = rs.getInt("C_student_id");
			
			if(middleCheckId != 0) {
				TrainingStudent trainingStudent = new TrainingStudent();
				trainingStudent.setStudentId(rs.getInt("C_student_id"));
				trainingStudent.setTrainingId(rs.getInt("C_training_id"));
				
				Training training = new Training();
				training.setEndDate(rs.getDate("B_end_date"));
				training.setId(rs.getInt("B_id"));
				training.setInstructorId(rs.getInt("B_instructor_id"));
				training.setName(rs.getString("B_name"));
				training.setStartDate(rs.getDate("B_start_date"));
				training.setSubInstructorId1(rs.getInt("B_sub_instructor_id1"));
				training.setSubInstructorId2(rs.getInt("B_sub_instructor_id2"));
				training.setSubInstructorId3(rs.getInt("B_sub_instructor_id3"));
				
				Instructor instructor = new Instructor();
				instructor.setAffiliation(rs.getString("D_affiliation"));
				instructor.setEmail(rs.getString("D_email"));
				instructor.setId(rs.getInt("D_id"));
				instructor.setKana(rs.getString("D_kana"));
				instructor.setName(rs.getString("D_name"));
				instructor.setPassword(rs.getString("D_password"));
				instructor.setRemarks(rs.getString("D_remarks"));
				
				training.setInstructor(instructor);
				
				trainingList.add(training);
			}
			
			preId = id;
		}
		
		return studentList;
	};

	
	/**
	 * 3つのテーブルを結合.
	 * @return
	 */
	private String join3Table() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
	    sb.append(" A.id A_id, ");
	    sb.append(" A.company_id A_company_id, ");
	    sb.append(" A.email A_email, ");
	    sb.append(" A.kana A_kana, ");
	    sb.append(" A.name A_name, ");
	    sb.append(" A.password A_password, ");
	    sb.append(" B.id B_id, ");
	    sb.append(" B.end_date B_end_date, ");
	    sb.append(" B.instructor_id B_instructor_id, ");
	    sb.append(" B.name B_name, ");
	    sb.append(" B.start_date B_start_date, ");
	    sb.append(" B.sub_instructor_id1 B_sub_instructor_id1, ");
	    sb.append(" B.sub_instructor_id2 B_sub_instructor_id2, ");
	    sb.append(" B.sub_instructor_id3 B_sub_instructor_id3, ");
	    sb.append(" C.student_id C_student_id, ");
	    sb.append(" C.training_id C_training_id, ");
	    sb.append(" D.affiliation D_affiliation, ");
	    sb.append(" D.email D_email, ");
	    sb.append(" D.name D_name, ");
	    sb.append(" D.kana D_kana, ");
	    sb.append(" D.id D_id, ");
	    sb.append(" D.password D_password, ");
	    sb.append(" D.remarks D_remarks ");
	    sb.append(" FROM ");
	    sb.append(" students A ");
	    sb.append(" INNER JOIN ");
	    sb.append(" training_student C ");
	    sb.append(" ON  A.id = C.student_id ");
	    sb.append(" INNER JOIN ");
	    sb.append(" trainings B ");
	    sb.append(" ON  B.id = C.training_id ");
	    sb.append(" INNER JOIN ");
	    sb.append(" instructors D ");
	    sb.append(" ON D.id = B.instructor_id ");
		
		String JoinSql = sb.toString();
		return JoinSql;
	}
	
	
	/**
	 * 受講生がログインするためのリポジトリ.
	 * @param email
	 * @param password
	 * @return
	 */
	public Student findByEmailAndPassword(String email, String password) {
		String sql = "SELECT id, name, kana, email, password, company_id FROM students WHERE email = :email AND password = :password";
//		StringBuilder sql = new StringBuilder();
//		sql.append(join3Table());
//		sql.append(" WHERE A.email = :email AND A.password = :password ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email).addValue("password", password);
		List<Student> studentList = template.query(sql.toString(), param, STUDENT_ROW_MAPPER);
		if(studentList.size() == 0) {
			return null;
		}
		return studentList.get(0);
	}
	
	/**
	 * 受講生がログインするためのリポジトリ.
	 * @param email
	 * @param password
	 * @return
	 */
	public Student findByEmail(String email) {
		String sql = "SELECT id, name, kana, email, password, company_id FROM students WHERE email = :email";
//		StringBuilder sql = new StringBuilder();
//		sql.append(join3Table());
//		sql.append(" WHERE A.email = :email AND A.password = :password ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
		Student student = template.queryForObject(sql.toString(), param, STUDENT_ROW_MAPPER);
		return student;
	}
	
	/**
	 * 受講生の研修一覧のためのリポジトリ.
	 * @param id
	 * @return
	 */
	public Student load(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append(join3Table());
		sql.append(" WHERE A.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Student> studentList = template.query(sql.toString(), param, STUDENT_RESULT_SET_EXTRACTOR);
		if(studentList.size() == 0) {
			return null; 
		}
		return studentList.get(0);
	}
	
	private SimpleJdbcInsert insert;
	/**
	 * INSERT時に採番されたIDを取得する方法.
	 */
	@PostConstruct
	public void init() {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert((JdbcTemplate)template.getJdbcOperations());
		SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName("students");
		insert = withTableName.usingGeneratedKeyColumns("id");
	}
	
	/**
	 * 管理者画面で生徒を登録するためのリポジトリ.
	 * @param student
	 */
	public Student insert(Student student) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(student);
//		String sql = "INSERT INTO students(name,kana,email,password,company_id) VALUES (:name, :kana, :email, :password, :companyId)";
//		template.update(sql, param);
		Number key = insert.executeAndReturnKey(param);
		student.setId(key.intValue());
		return student;
	}
	
	

}
