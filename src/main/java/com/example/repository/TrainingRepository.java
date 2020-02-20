package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Company;
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
	
//	private static final ResultSetExtractor<List<Training>> TRAINING_RESULT_SET_EXTRACTOR4 = (rs) -> {
//		List<Training> trainingList = new ArrayList<>();
//		List<Student> studentList = null;
//		
//		int preId = -1;
//		
//		while(rs.next()) {
//			int id = rs.getInt("A_id");
//			
//			if(id != preId) {
//				Training training = new Training();
//				training.setId(rs.getInt("A_id"));
//				training.setEndDate(rs.getDate("A_end_date"));
//				training.setInstructorId(rs.getInt("A_instructor_id"));
//				training.setName(rs.getString("A_name"));
//				training.setStartDate(rs.getDate("A_start_date"));
//				training.setSubInstructorId1(rs.getInt("A_sub_instructor_id1"));
//				training.setSubInstructorId1(rs.getInt("A_sub_instructor_id2"));
//				training.setSubInstructorId1(rs.getInt("A_sub_instructor_id3"));
//				
//				Instructor instructor = new Instructor();
//				instructor.setAffiliation(rs.getString("D_affiliation"));
//				instructor.setEmail(rs.getString("D_email"));
//				instructor.setId(rs.getInt("D_id"));
//				instructor.setKana(rs.getString("D_kana"));
//				instructor.setName(rs.getString("D_name"));
//				instructor.setPassword(rs.getString("D_password"));
//				instructor.setRemarks(rs.getString("D_remarks"));
//				
//				training.setInstructor(instructor);
//				studentList = new ArrayList<>();
//				training.setStudentList(studentList);
//				trainingList.add(training);
//			}
//			
//			Integer middleCheckId = rs.getInt("B_training_id");
//			
//			if(middleCheckId != 0) {
//				TrainingStudent trainingStudent = new TrainingStudent();
//				trainingStudent.setStudentId(rs.getInt("B_student_id"));
//				trainingStudent.setTrainingId(rs.getInt("B_training_id"));
//				
//				Student student = new Student();
//				student.setCompanyId(rs.getInt("C_company_id"));
//				student.setEmail(rs.getString("C_email"));
//				student.setId(rs.getInt("C_id"));
//				student.setKana(rs.getString("C_kana"));
//				student.setName(rs.getString("C_name"));
//				student.setPassword(rs.getString("C_password"));
//				
//				student.setTrainingList(trainingList);
//				
//				Company company = new Company();
//				company.setId(rs.getInt("E_id"));
//				company.setKana(rs.getString("E_kana"));
//				company.setName(rs.getString("E_name"));
//				company.setRemarks(rs.getString("E_remarks"));
//				student.setCompany(company);
//				studentList.add(student);
//			}
//			
//			preId = id;
//		}
//		
//		return trainingList;
//	};
	
	/**
	 * TrainingとTrainingStudentとStudentとinstructorテーブルを結合.
	 * @return
	 */
	private String join4Table() {
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
	    sb.append(" LEFT OUTER JOIN ");
	    sb.append(" training_student B ");
	    sb.append(" ON A.id = B.training_id ");
	    sb.append(" LEFT OUTER JOIN ");
	    sb.append(" students C ");
	    sb.append(" ON B.student_id = C.id ");
	    sb.append(" LEFT OUTER JOIN ");
	    sb.append(" instructors D ");
	    sb.append(" ON D.id = A.instructor_id ");
	    
	    String JoinSql = sb.toString();
	    return JoinSql;
	}
	
//	/**
//	 * TrainingとTrainingStudentとStudentとinstructorとcompanyテーブルを結合.
//	 * @return
//	 */
//	private String join5Table() {
//		StringBuilder sb = new StringBuilder();
//		sb.append(" SELECT ");
//		sb.append(" A.id A_id, ");
//	    sb.append(" A.end_date A_end_date, ");
//	    sb.append(" A.instructor_id A_instructor_id, ");
//	    sb.append(" A.name A_name, ");
//	    sb.append(" A.start_date A_start_date, ");
//	    sb.append(" A.sub_instructor_id1 A_sub_instructor_id1, ");
//	    sb.append(" A.sub_instructor_id2 A_sub_instructor_id2, ");
//	    sb.append(" A.sub_instructor_id3 A_sub_instructor_id3, ");
//	    sb.append(" B.student_id B_student_id, ");
//	    sb.append(" B.training_id B_training_id, ");
//	    sb.append(" C.id C_id, ");
//	    sb.append(" C.company_id C_company_id, ");
//	    sb.append(" C.email C_email, ");
//	    sb.append(" C.kana C_kana, ");
//	    sb.append(" C.name C_name, ");
//	    sb.append(" C.password C_password, ");
//	    sb.append(" D.affiliation D_affiliation, ");
//	    sb.append(" D.email D_email, ");
//	    sb.append(" D.name D_name, ");
//	    sb.append(" D.kana D_kana, ");
//	    sb.append(" D.id D_id, ");
//	    sb.append(" D.password D_password, ");
//	    sb.append(" D.remarks D_remarks, ");
//	    sb.append(" E.id E_id, ");
//	    sb.append(" E.name E_name, ");
//	    sb.append(" E.kana E_kana, ");
//	    sb.append(" E.remarks E_remarks ");
//	    sb.append(" FROM ");
//	    sb.append(" trainings A ");
//	    sb.append(" LEFT OUTER JOIN ");
//	    sb.append(" training_student B ");
//	    sb.append(" ON A.id = B.training_id ");
//	    sb.append(" LEFT OUTER JOIN ");
//	    sb.append(" students C ");
//	    sb.append(" ON B.student_id = C.id ");
//	    sb.append(" LEFT OUTER JOIN ");
//	    sb.append(" instructors D ");
//	    sb.append(" ON D.id = A.instructor_id ");
//	    sb.append(" LEFT OUTER JOIN ");
//	    sb.append(" companies E ");
//	    sb.append(" ON C.company_id = E.id ");
//	    
//	    String JoinSql = sb.toString();
//	    return JoinSql;
//	}
	
	//trainingとinstructorのマッパー.
	private static final ResultSetExtractor<List<Training>> TRAINING_RESULT_SET_EXTRACTOR2 = (rs) -> {
		List<Training> trainingList = new ArrayList<>();
		
		int preId = -1;
		
		while(rs.next()) {
			int id = rs.getInt("A_id");
			
			if(id != preId) {
				Training training = new Training();
				training.setEndDate(rs.getDate("A_end_date"));
				training.setId(rs.getInt("A_id"));
				training.setInstructorId(rs.getInt("A_instructor_id"));
				training.setName(rs.getString("A_name"));
				training.setStartDate(rs.getDate("A_start_date"));
				training.setSubInstructorId1(rs.getInt("A_sub_instructor_id1"));
				training.setSubInstructorId2(rs.getInt("A_sub_instructor_id2"));
				training.setSubInstructorId3(rs.getInt("A_sub_instructor_id3"));
				
				Instructor instructor = new Instructor();
				instructor.setAffiliation(rs.getString("B_affiliation"));
				instructor.setEmail(rs.getString("B_email"));
				instructor.setId(rs.getInt("B_id"));
				instructor.setKana(rs.getString("B_kana"));
				instructor.setName(rs.getString("B_name"));
				instructor.setPassword(rs.getString("B_password"));
				instructor.setRemarks(rs.getString("B_remarks"));
				
				training.setInstructor(instructor);
				trainingList.add(training);
			}
			preId = id;
		}
		return trainingList;
	};
	
	//trainingテーブルとinstructorテーブルをjoin.
	private String join2Table() {
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
	
	private static final RowMapper<Training> TRAINING_ROW_MAPPER = (rs,i) -> {
		Training training = new Training();
		training.setEndDate(rs.getDate("end_date"));
		training.setId(rs.getInt("id"));
		training.setInstructorId(rs.getInt("instructor_id"));
		training.setName(rs.getString("name"));
		training.setStartDate(rs.getDate("start_date"));
		training.setSubInstructorId1(rs.getInt("sub_instructor_id1"));
		training.setSubInstructorId2(rs.getInt("sub_instructor_id2"));
		training.setSubInstructorId3(rs.getInt("sub_instructor_id3"));
		return training;
	};
	
	//studentとtrainingとtraining_studentテーブルのmapper.
	private static final ResultSetExtractor<List<Training>> TRAINING_RESULT_SET_EXTRACTOR3 = (rs) -> {
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
	    sb.append(" C.password C_password ");
	    sb.append(" FROM ");
	    sb.append(" trainings A ");
	    sb.append(" INNER JOIN ");
	    sb.append(" training_student B ");
	    sb.append(" ON A.id = B.training_id ");
	    sb.append(" INNER JOIN ");
	    sb.append(" students C ");
	    sb.append(" ON B.student_id = C.id ");
	    
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
		sql.append(join4Table());
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
		sql.append(join4Table());
		sql.append(" WHERE A.instructor_id = :instructorId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("instructorId", id);
		List<Training> trainingList = template.query(sql.toString(), param, TRAINING_RESULT_SET_EXTRACTOR);
		if(trainingList.size() == 0) {
			return null;
		}
		return trainingList.get(0);
	}
	
	/**
	 * 管理者画面で研修一覧を表示するためのリポジトリ.
	 * @return
	 */
	public List<Training> findAll(){
		StringBuilder sql = new StringBuilder();
		sql.append(join2Table());
		sql.append(" ORDER BY A.start_date ");
		List<Training> trainingList = template.query(sql.toString(), TRAINING_RESULT_SET_EXTRACTOR2);
		return trainingList;
	}
	
	/**
	 * 管理者画面で研修を新規登録するためのリポジトリ.
	 * @param training
	 */
	public void insert(Training training) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(training);
		String sql = "INSERT INTO trainings (name, start_date, end_date, instructor_id, sub_instructor_id1, sub_instructor_id2, sub_instructor_id3) VALUES (:name, :startDate, :endDate, :instructorId, :subInstructorId1, :subInstructorId2, :subInstructorId3)";
		template.update(sql, param);
	}
	
	/**
	 * 管理者画面で研修を編集するために1件検索するためのリポジトリ.
	 * @param id
	 * @return
	 */
	public Training oneLoad(Integer id) {
		String sql = "SELECT id, name, start_date, end_date, instructor_id, sub_instructor_id1, sub_instructor_id2, sub_instructor_id3 FROM trainings WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Training training = template.queryForObject(sql, param, TRAINING_ROW_MAPPER);
		return training;
	}
	
	/**
	 * 管理者画面で研修を変更するためのリポジトリ.
	 * @param training
	 */
	public void update(Training training) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(training);
		String sql = "UPDATE trainings SET name = :name, start_date = :startDate, end_date = :endDate, instructor_id = :instructorId, sub_instructor_id1 = :subInstructorId1, sub_instructor_id2 = :subInstructorId2, sub_instructor_id3 = :subInstructorId3 WHERE id = :id";
		template.update(sql, param);
	}
	
	/**
	 * 管理者画面の生徒登録画面でトレーニングを1件検索するリポジトリ.
	 * @param id
	 * @return
	 */
	public Training loadJoin2Table(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append(join2Table());
		sql.append(" WHERE A.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Training> trainingList = template.query(sql.toString(), param, TRAINING_RESULT_SET_EXTRACTOR2);
		if(trainingList.size() == 0) {
			return null;
		}
		return trainingList.get(0);
	}
	
	/**
	 * 管理者画面の日報閲覧画面で研修1つにつき受講している受講生一覧を取得するためのリポジトリ.
	 * @param id
	 * @return
	 */
	public Training loadForAdmin(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append(join3Table());
		sql.append(" WHERE A.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Training> trainingList = template.query(sql.toString(), param, TRAINING_RESULT_SET_EXTRACTOR3);
		if(trainingList.size() == 0) {
			return null;
		}
		return trainingList.get(0);
	}
	
	/**
	 * 管理者画面で研修を研修名と日付で曖昧検索するためのリポジトリ.
	 * @return
	 */
	public List<Training> findByNameOrDate(String name, String startDate){
		StringBuilder sql = new StringBuilder();
		sql.append(join2Table());
		sql.append(" WHERE A.name LIKE :name AND to_char(A.start_date, 'yyyy-mm') LIKE (:startDate) ORDER BY A.id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("startDate", "%" + startDate + "%");
		List<Training> trainingList = template.query(sql.toString(), param, TRAINING_RESULT_SET_EXTRACTOR2);
		return trainingList;
	}
	
	/**
	 * 管理者画面で研修を研修名で曖昧検索するためのリポジトリ.
	 * @param name
	 * @return
	 */
	public List<Training> findByName(String name){
		StringBuilder sql = new StringBuilder();
		sql.append(join2Table());
		sql.append(" WHERE A.name LIKE :name ORDER BY A.id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Training> trainingList = template.query(sql.toString(), param, TRAINING_RESULT_SET_EXTRACTOR2);
		return trainingList;
	}
	
	/**
	 * 管理者画面で研修を日付で曖昧検索するためのリポジトリ.
	 * @param startDate
	 * @return
	 */
	public List<Training> findByDate(String startDate){
		StringBuilder sql = new StringBuilder();
		sql.append(join2Table());
		sql.append(" WHERE to_char(A.start_date, 'yyyy-mm') LIKE (:startDate) ORDER BY A.id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("startDate", "%" + startDate + "%");
		List<Training> trainingList = template.query(sql.toString(), param, TRAINING_RESULT_SET_EXTRACTOR2);
		return trainingList;
	}
	
	/**
	 * 管理者画面で研修一覧を表示するためのリポジトリ.
	 * @return
	 */
	public List<Training> findAllTraining(){
		StringBuilder sql = new StringBuilder();
		sql.append(join4Table());
		sql.append(" ORDER BY A.start_date ");
		List<Training> trainingList = template.query(sql.toString(), TRAINING_RESULT_SET_EXTRACTOR);
		return trainingList;
	}
	
	/**
	 * 管理者画面で研修idで生徒一覧を取得するためのリポジトリ.
	 * @param trainingId
	 * @return
	 */
	public List<Training> selectStudent(Integer trainingId){
		StringBuilder sql = new StringBuilder();
		sql.append(join3Table());
		sql.append(" WHERE A.id = :id ORDER BY C.id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", trainingId);
		List<Training> trainingList = template.query(sql.toString(), param, TRAINING_RESULT_SET_EXTRACTOR3);
		return trainingList;
	}

}
