package com.example.repository;

import java.util.ArrayList;
import java.util.Date;
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
import com.example.domain.StudentImpression;
import com.example.domain.Training;
import com.example.domain.WeeklyReport;

@Repository
public class WeeklyReportRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private SimpleJdbcInsert insert;
	
	/**
	 * INSERT時に採番されたIDを取得する方法.
	 */
	@PostConstruct
	public void init() {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert((JdbcTemplate)template.getJdbcOperations());
		SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName("weekly_reports");
		insert = withTableName.usingGeneratedKeyColumns("id");
	}
	
	private static final RowMapper<WeeklyReport> WEEKLT_REPORT_ROW_MAPPER = (rs,i) -> {
		WeeklyReport weeklyReport = new WeeklyReport();
		weeklyReport.setId(rs.getInt("id"));
		weeklyReport.setInstructorName(rs.getString("instructor_name"));
		weeklyReport.setContent(rs.getString("content"));
		weeklyReport.setStartDate(rs.getDate("start_date"));
		weeklyReport.setTrainingId(rs.getInt("training_id"));
		return weeklyReport;
	};
	
	/**
	 * 4つのテーブルのマッパー.
	 */
	private static final ResultSetExtractor<List<Training>> TRAINING_RESULT_SET_EXTRACTOR = (rs) -> {
		List<Training> trainingList = new ArrayList<>();
		List<WeeklyReport> weeklyReportList = null;
		List<StudentImpression> studentImpressionList = null;
		
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
				instructor.setId(rs.getInt("D_id"));
				instructor.setName(rs.getString("D_name"));
				instructor.setKana(rs.getString("D_kana"));
				instructor.setEmail(rs.getString("D_email"));
				instructor.setAffiliation(rs.getString("D_affiliation"));
				instructor.setPassword(rs.getString("D_password"));
				instructor.setRemarks(rs.getString("D_remarks"));
				training.setInstructor(instructor);
				
				weeklyReportList = new ArrayList<>();
				training.setWeeklyReportList(weeklyReportList);
				trainingList.add(training);
			}
			
			int middleCheckId = rs.getInt("B_id");
			
			if(middleCheckId != 0) {
				WeeklyReport weeklyReport = new WeeklyReport();
				weeklyReport.setId(rs.getInt("B_id"));
				weeklyReport.setInstructorName(rs.getString("B_instructor_name"));
				weeklyReport.setContent(rs.getString("B_content"));
				weeklyReport.setStartDate(rs.getDate("B_start_date"));
				weeklyReport.setTrainingId(rs.getInt("B_training_id"));
				
				StudentImpression studentImpression = new StudentImpression();
				studentImpression.setId(rs.getInt("C_id"));
				studentImpression.setWeeklyReportId(rs.getInt("C_weekly_report_id"));
				studentImpression.setStudentName(rs.getString("C_student_name"));
				studentImpression.setContent(rs.getString("C_content"));
				
				studentImpressionList = new ArrayList<>();
				weeklyReport.setStudentImpressionList(studentImpressionList);
				studentImpressionList.add(studentImpression);
				weeklyReportList.add(weeklyReport);
			}
			
			preId = id;
		}
		return trainingList;
	};
	
	/**
	 * weeklyReportとstudentImpressionテーブルのマッパー.
	 */
	private static final ResultSetExtractor<List<WeeklyReport>> WEEKLY_REPORT_RESULT_SET_EXTRACTOR = (rs) -> {
		List<WeeklyReport> weeklyReportList = new ArrayList<>();
		List<StudentImpression> studentImpressionList = null;
		
		int preId = -1;
		
		while(rs.next()) {
			int id = rs.getInt("B_id");
			
			if(id != preId) {
				WeeklyReport weeklyReport = new WeeklyReport();
				weeklyReport.setId(rs.getInt("B_id"));
				weeklyReport.setInstructorName(rs.getString("B_instructor_name"));
				weeklyReport.setContent(rs.getString("B_content"));
				weeklyReport.setStartDate(rs.getDate("B_start_date"));
				weeklyReport.setTrainingId(rs.getInt("B_training_id"));
				
				studentImpressionList = new ArrayList<>();
				weeklyReport.setStudentImpressionList(studentImpressionList);
				weeklyReportList.add(weeklyReport);
			}
			
			int middleCheckId = rs.getInt("C_id");
			
			if(middleCheckId != 0) {
				StudentImpression studentImpression = new StudentImpression();
				studentImpression.setId(rs.getInt("C_id"));
				studentImpression.setContent(rs.getString("C_content"));
				studentImpression.setStudentName(rs.getString("C_student_name"));
				studentImpression.setWeeklyReportId(rs.getInt("C_weekly_report_id"));
				
				studentImpressionList.add(studentImpression);
			}
			
			preId = id;
		}
		
		return weeklyReportList;
	};
	
	/**
	 * trainingとweeklyReportとstudentImpressionを結合.
	 * @return
	 */
	public String joinTable() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		sb.append(" A.id A_id, ");
		sb.append(" A.end_date A_end_date, ");
		sb.append(" A.start_date A_start_date, ");
		sb.append(" A.instructor_id A_instructor_id, ");
		sb.append(" A.name A_name, ");
		sb.append(" A.sub_instructor_id1 A_sub_instructor_id1, ");
		sb.append(" A.sub_instructor_id2 A_sub_instructor_id2, ");
		sb.append(" A.sub_instructor_id3 A_sub_instructor_id3, ");
		sb.append(" B.id B_id, ");
		sb.append(" B.instructor_name B_instructor_name, ");
		sb.append(" B.content B_content, ");
		sb.append(" B.start_date B_start_date, ");
		sb.append(" B.training_id B_training_id, ");
		sb.append(" C.id C_id, ");
		sb.append(" C.weekly_report_id C_weekly_report_id, ");
		sb.append(" C.student_name C_student_name, ");
		sb.append(" C.content C_content ");
		sb.append(" FROM ");
		sb.append(" trainings A ");
		sb.append(" LEFT OUTER JOIN ");
		sb.append(" weekly_reports B ");
		sb.append(" ON A.id = B.training_id ");
		sb.append(" LEFT OUTER JOIN ");
		sb.append(" student_impressions C ");
		sb.append(" ON B.id = C.weekly_report_id ");
		
		String JoinSql = sb.toString();
		return JoinSql;
	}
	
	/**
	 * trainingとweeklyReportとstudentImpressionとinstructorを結合.
	 * @return
	 */
	public String join4Table() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		sb.append(" A.id A_id, ");
		sb.append(" A.end_date A_end_date, ");
		sb.append(" A.start_date A_start_date, ");
		sb.append(" A.instructor_id A_instructor_id, ");
		sb.append(" A.name A_name, ");
		sb.append(" A.sub_instructor_id1 A_sub_instructor_id1, ");
		sb.append(" A.sub_instructor_id2 A_sub_instructor_id2, ");
		sb.append(" A.sub_instructor_id3 A_sub_instructor_id3, ");
		sb.append(" B.id B_id, ");
		sb.append(" B.instructor_name B_instructor_name, ");
		sb.append(" B.content B_content, ");
		sb.append(" B.start_date B_start_date, ");
		sb.append(" B.training_id B_training_id, ");
		sb.append(" C.id C_id, ");
		sb.append(" C.weekly_report_id C_weekly_report_id, ");
		sb.append(" C.student_name C_student_name, ");
		sb.append(" C.content C_content, ");
		sb.append(" D.id D_id, ");
		sb.append(" D.name D_name, ");
		sb.append(" D.kana D_kana, ");
		sb.append(" D.email D_email, ");
		sb.append(" D.password D_password, ");
		sb.append(" D.affiliation D_affiliation, ");
		sb.append(" D.remarks D_remarks ");
		sb.append(" FROM ");
		sb.append(" trainings A ");
		sb.append(" INNER JOIN ");
		sb.append(" weekly_reports B ");
		sb.append(" ON A.id = B.training_id ");
		sb.append(" INNER JOIN ");
		sb.append(" student_impressions C ");
		sb.append(" ON B.id = C.weekly_report_id ");
		sb.append(" INNER JOIN ");
		sb.append(" instructors D ");
		sb.append(" ON D.id = A.instructor_id ");
		
		String JoinSql = sb.toString();
		return JoinSql;
	}
	
	
	/**
	 * 週報を登録するためのリポジトリ.
	 * @param weeklyReport
	 */
	public WeeklyReport insert(WeeklyReport weeklyReport) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(weeklyReport);
		Number key = insert.executeAndReturnKey(param);
		weeklyReport.setId(key.intValue());
		return weeklyReport;
	}
	
	/**
	 * 週報を研修登録と同時にinsert.
	 * @param weeklyReport
	 */
	public WeeklyReport weeklyReportInsert(WeeklyReport weeklyReport) {
//		SqlParameterSource param = new BeanPropertySqlParameterSource(weeklyReport);
//		String sql = "INSERT INTO weekly_reports (start_date, instructor_name, content, training_id ) VALUES (:startDate, :instructorName, :content, :trainingId )";
//		template.update(sql, param);
		SqlParameterSource param = new BeanPropertySqlParameterSource(weeklyReport);
		Number key = insert.executeAndReturnKey(param);
		weeklyReport.setId(key.intValue());
		return weeklyReport;
	}
	
	/**
	 * 週報を1件検索するためのリポジトリ(trainingのidで検索).
	 * @param id
	 * @return
	 */
	public WeeklyReport load(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable());
		sql.append(" WHERE A.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<WeeklyReport> weeklyReportList = template.query(sql.toString(), param, WEEKLY_REPORT_RESULT_SET_EXTRACTOR);
		if(weeklyReportList.size() == 0) {
			return null;
		}
		return weeklyReportList.get(0);
	}
	
	/**
	 * 週報を日付で1件検索するためのリポジトリ.
	 * @param id
	 * @return
	 */
	public WeeklyReport loadByDate(Date date, Integer trainingId) {
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable());
		sql.append(" WHERE B.start_date = :startDate AND A.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("startDate", date).addValue("id", trainingId);
		List<WeeklyReport> weeklyReportList = template.query(sql.toString(), param, WEEKLY_REPORT_RESULT_SET_EXTRACTOR);
		if(weeklyReportList.size() == 0) {
			return null;
		}
		return weeklyReportList.get(0);
	}
	
	/**
	 * 週報の登録日の最大を取得するリポジトリ.
	 * @param startDate
	 * @return
	 */
	public WeeklyReport maxStartDate(Date startDate) {
		String sql = "SELECT MAX(start_date) FROM weekly_reports";
		List<WeeklyReport> weeklyReportList = template.query(sql, WEEKLT_REPORT_ROW_MAPPER);
		if(weeklyReportList.size() == 0) {
			return null;
		}
		return weeklyReportList.get(0);
	}
	
	/**
	 * 週報を編集するためのリポジトリ.
	 * @param weeklyReport
	 */
	public void update(WeeklyReport weeklyReport) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(weeklyReport);
		String sql = "UPDATE weekly_reports SET content = :content WHERE id = :id AND training_id = :trainingId";
		template.update(sql, param);
	}
	
	/**
	 * 週報を編集するためのリポジトリ.
	 * @param weeklyReport
	 */
	public void weeklyReportUpdate(WeeklyReport weeklyReport) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(weeklyReport);
		String sql = "UPDATE weekly_reports SET content = :content WHERE training_id = :trainingId AND start_date = :startDate";
		template.update(sql, param);
	}
	
	/**
	 * 管理者画面の研修編集でstudentImpressionテーブルを削除するために使う.
	 * @param trainingId
	 * @return
	 */
	public List<WeeklyReport> listWeeklyReport(Integer trainingId){
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable());
		sql.append(" WHERE A.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", trainingId);
		List<WeeklyReport> weeklyReportList = template.query(sql.toString(), param, WEEKLY_REPORT_RESULT_SET_EXTRACTOR);
		return weeklyReportList;
	}
	
	/**
	 * 管理者画面の研修編集でweekly_reportを一旦削除するためのリポジトリ.
	 * @param trainingId
	 */
	public void deleteWeeklyReport(Integer trainingId) {
		String sql = "DELETE FROM weekly_reports WHERE training_id = :trainingId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("trainingId", trainingId);
		template.update(sql, param);
	}
	
	/**
	 * 週報を印刷するために1件検索するためのリポジトリ(weekly_reportのidで検索).
	 * @param id
	 * @return
	 */
	public WeeklyReport printWeeklyReport(Integer weeklyReportId) {
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable());
		sql.append(" WHERE B.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", weeklyReportId);
		List<WeeklyReport> weeklyReportList = template.query(sql.toString(), param, WEEKLY_REPORT_RESULT_SET_EXTRACTOR);
		if(weeklyReportList.size() == 0) {
			return null;
		}
		return weeklyReportList.get(0);
	}

}
