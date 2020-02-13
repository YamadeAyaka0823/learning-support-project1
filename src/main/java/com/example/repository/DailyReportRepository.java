package com.example.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.DailyReport;
import com.example.domain.Student;
import com.example.domain.Training;
import com.example.domain.TrainingStudent;

@Repository
public class DailyReportRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<DailyReport> DAILY_REPORT_ROW_MAPPER = (rs,i) -> {
		DailyReport dailyReport = new DailyReport();
		dailyReport.setAboutInstructor(rs.getInt("about_instructor"));
		dailyReport.setContent(rs.getString("content"));
		dailyReport.setDate(rs.getDate("date"));
		dailyReport.setDetailIntelligibillity(rs.getString("detail_intelligibillity"));
		dailyReport.setId(rs.getInt("id"));
		dailyReport.setIntelligibility(rs.getInt("intelligibility"));
		dailyReport.setQuestion(rs.getString("question"));
		dailyReport.setStudentId(rs.getInt("student_id"));
		dailyReport.setTrainingId(rs.getInt("training_id"));
		return dailyReport;
	};
	
	/**
	 * dailyReportとtrainingとstudentのmapper.
	 */
	private static final ResultSetExtractor<List<DailyReport>> DAILY_REPORT_RESULT_SET_EXTRACTOR = (rs) -> {
		List<DailyReport> dailyReportList = new ArrayList<>();
		
		int preId = -1;
		
		while(rs.next()) {
			int id = rs.getInt("A_id");
			
			if(id != preId) {
				DailyReport dailyReport = new DailyReport();
				dailyReport.setId(rs.getInt("A_id"));
				dailyReport.setAboutInstructor(rs.getInt("A_about_instructor"));
				dailyReport.setContent(rs.getString("A_content"));
				dailyReport.setDate(rs.getDate("A_date"));
				dailyReport.setDetailIntelligibillity(rs.getString("A_detail_intelligibillity"));
				dailyReport.setIntelligibility(rs.getInt("A_intelligibility"));
				dailyReport.setQuestion(rs.getString("A_question"));
				dailyReport.setStudentId(rs.getInt("A_student_id"));
				dailyReport.setTrainingId(rs.getInt("A_training_id"));
				
				Training training = new Training();
				training.setId(rs.getInt("B_id"));
				training.setEndDate(rs.getDate("B_end_date"));
				training.setInstructorId(rs.getInt("B_instructor_id"));
				training.setName(rs.getString("B_name"));
				training.setStartDate(rs.getDate("B_start_date"));
				training.setSubInstructorId1(rs.getInt("B_sub_instructor_id1"));
				training.setSubInstructorId2(rs.getInt("B_sub_instructor_id2"));
				training.setSubInstructorId3(rs.getInt("B_sub_instructor_id3"));
				
				dailyReport.setTraining(training);
				
				Student student = new Student();
				student.setId(rs.getInt("C_id"));
				student.setCompanyId(rs.getInt("C_company_id"));
				student.setEmail(rs.getString("C_email"));
				student.setKana(rs.getString("C_kana"));
				student.setName(rs.getString("C_name"));
				student.setPassword(rs.getString("C_password"));
				
				dailyReport.setStudent(student);
				dailyReportList.add(dailyReport);
			}
			
			preId = id;
		}
		
		return dailyReportList;
	};
	
	/**
	 * dailyReportとtrainingとstudentテーブルの結合.
	 * @return
	 */
	private String joinTable() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		sb.append(" A.id A_id, ");
		sb.append(" A.about_instructor A_about_instructor, ");
		sb.append(" A.content A_content, ");
		sb.append(" A.date A_date, ");
		sb.append(" A.detail_intelligibillity A_detail_intelligibillity, ");
		sb.append(" A.intelligibility A_intelligibility, ");
		sb.append(" A.question A_question, ");
		sb.append(" A.student_id A_student_id, ");
		sb.append(" A.training_id A_training_id, ");
		sb.append(" B.id B_id, ");
		sb.append(" B.end_date B_end_date, ");
		sb.append(" B.instructor_id B_instructor_id, ");
		sb.append(" B.name B_name, ");
		sb.append(" B.start_date B_start_date, ");
		sb.append(" B.sub_instructor_id1 B_sub_instructor_id1, ");
		sb.append(" B.sub_instructor_id2 B_sub_instructor_id2, ");
		sb.append(" B.sub_instructor_id3 B_sub_instructor_id3, ");
		sb.append(" C.id C_id, ");
		sb.append(" C.company_id C_company_id, ");
		sb.append(" C.email C_email, ");
		sb.append(" C.kana C_kana, ");
		sb.append(" C.name C_name, ");
		sb.append(" C.password C_password ");
		sb.append(" FROM ");
		sb.append(" daily_reports A ");
		sb.append(" INNER JOIN ");
		sb.append(" trainings B ");
		sb.append(" ON A.training_id = B.id ");
		sb.append(" INNER JOIN ");
		sb.append(" students C ");
		sb.append(" ON A.student_id = C.id ");
		
		String JoinSql = sb.toString();
		return JoinSql;
	}
	
	
	/**
	 * 受講生の日報をインサートするためのリポジトリ.
	 * @param dailyReport
	 */
	public void insert(DailyReport dailyReport) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(dailyReport);
		String sql = "INSERT INTO daily_reports (date, training_id, student_id, content, intelligibility, detail_intelligibillity, about_instructor, question) VALUES ( :date, :trainingId, :studentId, :content, :intelligibility, :detailIntelligibillity, :aboutInstructor, :question )";
		template.update(sql, param);
	}
	
	/**
	 * 受講生の日報の一覧のためのリポジトリ.
	 * @param id
	 * @return
	 */
	public DailyReport load(Integer id, Integer studentId) {
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable());
		sql.append(" WHERE B.id = :id AND A.student_id = :studentId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id).addValue("studentId", studentId);
		List<DailyReport> dailyReportList = template.query(sql.toString(), param, DAILY_REPORT_RESULT_SET_EXTRACTOR);
		if(dailyReportList.size() == 0) {
			return null;
		}
		return dailyReportList.get(0);
	}
	
	/**
	 * 日報を1件検索するためのリポジトリ.
	 * @param date
	 * @return
	 */
	public DailyReport dateLoad(Integer trainingId,Date date2) {
//		String sql = "SELECT id, date, training_id, student_id, content, intelligibility, detail_intelligibillity, about_instructor, question FROM daily_reports WHERE date = :date AND training_id = :trainingId";
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable());
		sql.append(" WHERE A.training_id = :trainingId AND A.date = :date ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("trainingId", trainingId).addValue("date", date2);
		List<DailyReport> dailyReportList = template.query(sql.toString(), param, DAILY_REPORT_RESULT_SET_EXTRACTOR);
		if(dailyReportList.size() == 0) {
			return null;
		}
		return dailyReportList.get(0);
	}
	
	/**
	 * 講師が生徒の日報を閲覧するためのリポジトリ.
	 * @param id
	 * @return
	 */
	public DailyReport instructorViewDailyReport(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable());
		sql.append(" WHERE B.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<DailyReport> dailyReportList = template.query(sql.toString(), param, DAILY_REPORT_RESULT_SET_EXTRACTOR);
		if(dailyReportList.size() == 0) {
			return null;
		}
		return dailyReportList.get(0);
	}
	
	/**
	 * 管理者画面で受講生の日報の一覧のためのリポジトリ.
	 * @param id
	 * @return
	 */
	public DailyReport loadForAdmin(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable());
		sql.append(" WHERE B.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<DailyReport> dailyReportList = template.query(sql.toString(), param, DAILY_REPORT_RESULT_SET_EXTRACTOR);
		if(dailyReportList.size() == 0) {
			return null;
		}
		return dailyReportList.get(0);
	}
	
	/**
	 * 講師画面で受講生の日報を日付と名前のプルダウンを選択して閲覧するためのリポジトリ.
	 * @param trainingId
	 * @param date
	 * @param name
	 * @return
	 */
	public DailyReport dateAndNameLoad(Integer trainingId, Date date, String name) {
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable());
		sql.append(" WHERE A.training_id = :trainingId AND A.date = :date AND C.name = :name ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("trainingId", trainingId).addValue("date", date).addValue("name", name);
		List<DailyReport> dailyReportList = template.query(sql.toString(), param, DAILY_REPORT_RESULT_SET_EXTRACTOR);
		if(dailyReportList.size() == 0) {
			return null;
		}
		return dailyReportList.get(0);
	}

}
