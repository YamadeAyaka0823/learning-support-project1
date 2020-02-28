package com.example.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.DailyReport;
import com.example.domain.PasswordResetToken;
import com.example.domain.Student;
import com.example.form.DailyReportForm;
import com.example.form.StudentUpdateForm;
import com.example.repository.DailyReportRepository;
import com.example.repository.PasswordTokenRepository;
import com.example.repository.StudentRepository;

@Service
@Transactional
public class StudentService {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private DailyReportRepository dailyReportRepository;
	
	@Autowired
	private PasswordTokenRepository passwordTokenRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * 受講者がログインするためのサービス.
	 * @param form
	 * @return
	 */
	public Student findByEmail(String email) {
		return studentRepository.findByEmail(email);
	}
	
	/**
	 * 受講生の研修一覧のためのサービス.
	 * @param id
	 * @return
	 */
	public Student load(Integer id) {
		return studentRepository.load(id);
	}
	
	/**
	 * 受講生の日報をインサートするサービス.
	 * @param form
	 * @throws ParseException 
	 */
	public void insert(DailyReportForm form) throws ParseException {
		DailyReport dailyReport = new DailyReport();
		dailyReport.setAboutInstructor(form.getIntAboutInstructor());
		dailyReport.setContent(form.getContent());
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = sdFormat.parse(form.getFormattedDate());
		dailyReport.setDate(date);
		dailyReport.setDetailIntelligibillity(form.getDetailIntelligibillity());
		dailyReport.setIntelligibility(form.getIntIntelligibility());
		dailyReport.setQuestion(form.getQuestion());
		dailyReport.setStudentId(form.getStudentId());
		dailyReport.setTrainingId(form.getTrainingId());
		dailyReportRepository.insert(dailyReport);
	}
	
	/**
	 * 受講生の日報の閲覧のためのサービス.
	 * @param id
	 * @return
	 */
	public DailyReport dailyLoad(Integer id, Integer studentId) {
		return dailyReportRepository.load(id, studentId);
	}
	
	/**
	 * 日報を1件検索するためのサービス.
	 * @param date
	 * @return
	 */
	public DailyReport dateLoad(Integer trainingId, Integer studentId, Date date2) {
		return dailyReportRepository.dateLoad(trainingId,studentId,date2);
	}
	
	/**
	 * 管理者画面で生徒を登録するためのサービス.
	 * @param student
	 */
	public Student insert(Student student) {
		return studentRepository.insert(student);
	}
	
	/**
	 * 管理者画面で受講生の日報の一覧のためのサービス.
	 * @param id
	 * @return
	 */
	public DailyReport loadForAdmin(Integer id) {
		return dailyReportRepository.loadForAdmin(id);
	}
	
	/**
	 * 管理者画面で受講生を1件検索するためのサービス.
	 * @param studentId
	 * @return
	 */
	public Student findByStudentId(Integer studentId) {
		return studentRepository.loadByStudentId(studentId);
	}
	
	/**
	 * 管理者画面で受講生を編集するためのサービス.
	 * @param form
	 */
	public void update(StudentUpdateForm form) {
		Student student = new Student();
		student.setId(form.getStudentId());
		student.setEmail(form.getEmail());
		student.setKana(form.getKana());
		student.setName(form.getName());
		studentRepository.update(student);
	}
	
	////////////////////////////////////////////////////////////////////////
	/**
	 * student_tokenテーブルにinsertするためのサービス.
	 * @param student
	 * @param token
	 */
	public void createPasswordResetTokenForStudent(Student student, String token) {
		PasswordResetToken myToken = new PasswordResetToken(token, student);
		myToken.setStudent(student);
		myToken.setToken(token);
		passwordTokenRepository.save(myToken);
	}
	
	/**
	 * 新しいパスワードをインサートするためのサービス.
	 * @param student
	 * @param password
	 */
	public void changeStudentPassword(Student student, String password) {
		student.setPassword(passwordEncoder.encode(password));
		studentRepository.saveNewPassword(student);
	}

}
