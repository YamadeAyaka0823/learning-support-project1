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
import com.example.domain.CompanyMember;
import com.example.domain.Instructor;
import com.example.domain.Student;
import com.example.domain.Training;
import com.example.domain.TrainingStudent;

@Repository
public class CompanyRepository {
	
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
				
				CompanyMember companyMember = new CompanyMember();
				companyMember.setId(rs.getInt("F_id"));
				companyMember.setCompanyId(rs.getInt("F_company_id"));
				companyMember.setEmail(rs.getString("F_email"));
				companyMember.setKana(rs.getString("F_kana"));
				companyMember.setName(rs.getString("F_name"));
				companyMember.setPassword(rs.getString("F_password"));
				
				List<CompanyMember> companyMemberList = new ArrayList<>();
				companyMemberList.add(companyMember);
				
				Company company = new Company();
				company.setId(rs.getInt("E_id"));
				company.setKana(rs.getString("E_kana"));
				company.setName(rs.getString("E_name"));
				company.setRemarks(rs.getString("E_remarks"));
				company.setCompanyMemberList(companyMemberList);
				
				Student student = new Student();
				student.setCompanyId(rs.getInt("C_company_id"));
				student.setEmail(rs.getString("C_email"));
				student.setId(rs.getInt("C_id"));
				student.setKana(rs.getString("C_kana"));
				student.setName(rs.getString("C_name"));
				student.setPassword(rs.getString("C_password"));
				
				student.setCompany(company);
				student.setTrainingList(trainingList);
				studentList.add(student);
			}
			
			preId = id;
		}
		
		return trainingList;
	};
	
	
	private String joinTable() {
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
	    sb.append(" D.remarks D_remarks, ");
	    sb.append(" E.id E_id, ");
	    sb.append(" E.kana E_kana, ");
	    sb.append(" E.name E_name, ");
	    sb.append(" E.remarks E_remarks, ");
	    sb.append(" F.id F_id, ");
	    sb.append(" F.name F_name, ");
	    sb.append(" F.kana F_kana, ");
	    sb.append(" F.password F_password, ");
	    sb.append(" F.email F_email, ");
	    sb.append(" F.company_id F_company_id ");
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
	    sb.append(" INNER JOIN ");
	    sb.append(" companies E ");
	    sb.append(" ON E.id = C.company_id ");
	    sb.append(" INNER JOIN ");
	    sb.append(" company_members F ");
	    sb.append(" ON E.id = F.company_id ");
	    
	    String JoinSql = sb.toString();
	    return JoinSql;
	}
	
	//companyとcompanyMemberのマッパー.
	private static final ResultSetExtractor<List<Company>> COMPANY_RESULT_SET_EXTRACTOR = (rs) -> {
		List<Company> companyList = new ArrayList<>();
		List<CompanyMember> companyMemberList = null;
		
		int preId = -1;
		
		while(rs.next()) {
			int id = rs.getInt("A_id");
			
			if(id != preId) {
				Company company = new Company();
				company.setId(rs.getInt("A_id"));
				company.setKana(rs.getString("A_kana"));
				company.setName(rs.getString("A_name"));
				company.setRemarks(rs.getString("A_remarks"));
				
				companyMemberList = new ArrayList<>();
				company.setCompanyMemberList(companyMemberList);
				companyList.add(company);
			}
			
			int middleCheckId = rs.getInt("B_id");
			
			if(middleCheckId != 0) {
				CompanyMember companyMember = new CompanyMember();
				companyMember.setCompanyId(rs.getInt("B_company_id"));
				companyMember.setEmail(rs.getString("B_email"));
				companyMember.setId(rs.getInt("B_id"));
				companyMember.setKana(rs.getString("B_kana"));
				companyMember.setName(rs.getString("B_name"));
				companyMember.setPassword(rs.getString("B_password"));
				companyMemberList.add(companyMember);
			}
			preId = id;
		}
		return companyList;
	};
	
	//companyテーブルとcompany_memberテーブルを結合したSQL
	private String join2Table() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		sb.append(" A.id A_id, ");
		sb.append(" A.name A_name, ");
		sb.append(" A.kana A_kana, ");
		sb.append(" A.remarks A_remarks, ");
		sb.append(" B.id B_id, ");
		sb.append(" B.name B_name, ");
		sb.append(" B.kana B_kana, ");
		sb.append(" B.email B_email, ");
		sb.append(" B.password B_password, ");
		sb.append(" B.company_id B_company_id ");
		sb.append(" FROM ");
		sb.append(" companies A ");
		sb.append(" LEFT OUTER JOIN ");
		sb.append(" company_members B ");
		sb.append(" ON A.id = B.company_id ");
		
		String JoinSql = sb.toString();
		return JoinSql;
	}
	
	private static final RowMapper<Company> COMPANY_ROW_MAPPER = (rs,i) -> {
		Company company = new Company();
		company.setId(rs.getInt("id"));
		company.setKana(rs.getString("kana"));
		company.setName(rs.getString("name"));
		company.setRemarks(rs.getString("remarks"));
		return company;
	};
	
	
	/**
	 * 会社が受け持つ研修の一覧を表示するためのリポジトリ.
	 * @param id
	 * @return
	 */
	public List<Training> load(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable());
		sql.append(" WHERE E.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Training> trainingList = template.query(sql.toString(), param, TRAINING_RESULT_SET_EXTRACTOR);
		return trainingList;
	}
	
	/**
	 * 管理者画面で企業一覧を表示するためのリポジトリ.
	 * @return
	 */
	public List<Company> companyFindAll(){
		StringBuilder sql = new StringBuilder();
		sql.append(join2Table());
		sql.append(" ORDER BY A.id ");
		List<Company> companyList = template.query(sql.toString(), COMPANY_RESULT_SET_EXTRACTOR);
		return companyList;
	}
	
	/**
	 * 管理者画面で企業を新規登録するためのリポジトリ.
	 * @param company
	 * @return
	 */
	public void insert(Company company) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(company);
		String sql = "INSERT INTO companies (name, kana, remarks) VALUES (:name, :kana, :remarks)";
		template.update(sql, param);
	}
	
	/**
	 * 管理者画面の管理者新規登録で担当企業一覧を表示するためのリポジトリ.
	 * @return
	 */
	public List<Company> findAllName() {
		String sql = "SELECT id, name, kana, remarks FROM companies";
		List<Company> companyList = template.query(sql, COMPANY_ROW_MAPPER);
		return companyList;
	}
	
	/**
	 * 管理者画面で企業を編集するために1件検索するためのリポジトリ.
	 * @param id
	 * @return
	 */
	public Company oneLoad(Integer id) {
		String sql = "SELECT id, name, kana, remarks FROM companies WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Company> companyList = template.query(sql, param, COMPANY_ROW_MAPPER);
		if(companyList.size() == 0) {
			return null;
		}
		return companyList.get(0);
	}
	
	/**
	 * 管理者画面で企業を編集するためのリポジトリ.
	 * @param company
	 */
	public void update(Company company) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(company);
		String sql = "UPDATE companies SET id = :id, name = :name, kana = :kana, remarks = :remarks WHERE id = :id";
		template.update(sql, param);
	}
	
	/**
	 * 管理者画面で企業を曖昧検索するためのリポジトリ.
	 * @param name
	 * @return
	 */
	public List<Company> findByName(String name){
		StringBuilder sql = new StringBuilder();
		sql.append(join2Table());
		sql.append(" WHERE A.name LIKE :name ");
//		String sql = "SELECT ID, name, kana, remarks FROM companies WHERE name LIKE :name ";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Company> companyList = template.query(sql.toString(), param, COMPANY_RESULT_SET_EXTRACTOR);
		return companyList;
	}

}
