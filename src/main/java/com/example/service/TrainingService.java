package com.example.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.DailyReport;
import com.example.domain.StudentImpression;
import com.example.domain.Training;
import com.example.domain.WeeklyReport;
import com.example.form.TrainingRegisterForm;
import com.example.form.TrainingUpdateForm;
import com.example.repository.DailyReportRepository;
import com.example.repository.StudentImpressionRepository;
import com.example.repository.TrainingRepository;
import com.example.repository.TrainingStudentRepository;
import com.example.repository.WeeklyReportRepository;

@Service
@Transactional
public class TrainingService {
	
	@Autowired
	private TrainingRepository trainingRepository;
	
	@Autowired
	private TrainingStudentRepository trainingStudentRepository;
	
	@Autowired
	private StudentImpressionRepository studentImpressionRepository;
	
	@Autowired
	private DailyReportRepository dailyReportRepository;
	
	@Autowired
	private WeeklyReportRepository weeklyReportRepository;
	
	
	/**
	 * 研修を1件検索するサービス.
	 * @param id
	 * @return
	 */
	public Training load(Integer id) {
		return trainingRepository.load(id);
	}
	
	/**
	 * 研修を講師IDで1件検索するサービス.
	 * @param id
	 * @return
	 */
	public Training instructorIdLoad(Integer id) {
		return trainingRepository.instructorIdLoad(id);
	}
	
	/**
	 * 管理者画面で研修一覧を表示するためのサービス.
	 * @return
	 */
	public List<Training> findAll(){
		return trainingRepository.findAll();
	}
	
	/**
	 * 管理者画面で研修を新規登録するためのサービス.
	 * @param form
	 * @throws ParseException 
	 */
	public void insert(TrainingRegisterForm form) throws ParseException {
		Training training = new Training();
		//日付をStringからDateに変換
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = sdFormat.parse(form.getEndDate());
		training.setEndDate(endDate);
        Date startDate = sdFormat.parse(form.getStartDate());
		training.setStartDate(startDate);
		
		training.setInstructorId(form.getInstructorId());
		training.setName(form.getName());
		training.setSubInstructorId1(form.getSubInstructorId1());
		training.setSubInstructorId2(form.getSubInstructorId2());
		training.setSubInstructorId3(form.getSubInstructorId3());
		trainingRepository.insert(training); //trainingテーブルにinsert.
	}
	
	/**
	 * 管理者画面で研修を編集するために1件検索するサービス.
	 * @param id
	 * @return
	 */
	public Training oneLoad(Integer id) {
		return trainingRepository.oneLoad(id);
	}
	
	/**
	 * 管理者画面で研修を編集するためのサービス.
	 * @param form
	 * @throws ParseException
	 */
	public void update(TrainingUpdateForm form) throws ParseException {
		//trainingsテーブルをupdate
		Training training = new Training();
		//日付をStringからDateに変換
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date endDate = sdFormat.parse(form.getEndDate());
		training.setEndDate(endDate);
		Date startDate = sdFormat.parse(form.getStartDate());
		training.setStartDate(startDate);
		
		training.setId(form.getId());
		training.setInstructorId(form.getInstructorId());
		training.setName(form.getName());
		training.setSubInstructorId1(form.getSubInstructorId1());
		training.setSubInstructorId2(form.getSubInstructorId2());
		training.setSubInstructorId3(form.getSubInstructorId3());
		trainingRepository.update(training);
		//trainingIdで一旦dailyReportを消す
		dailyReportRepository.deleteDailyReport(form.getId());
		//weeklyReportIdで一旦student_impressionを消す
		List<WeeklyReport> weeklyReportList = weeklyReportRepository.listWeeklyReport(form.getId());
		for(int i = 0; i < weeklyReportList.size(); i++) {
			for(int j = 0; j < weeklyReportList.get(i).getStudentImpressionList().size(); j++) {
				studentImpressionRepository.deleteStudentImpression(weeklyReportList.get(i).getStudentImpressionList().get(j).getId());
			}
		}
		//trainingIdで一旦weeklyReportを消す
		weeklyReportRepository.deleteWeeklyReport(form.getId());
		
//		List<TrainingStudent> trainingStudentList = trainingStudentRepository.load(form.getId());
		Training trainingForDailyReport = trainingRepository.load(form.getId());
    	//研修の開始日と終了日を取得する
		//DateをLocalDateに変換
		Date date2 = trainingForDailyReport.getStartDate();
		LocalDate start = ((java.sql.Date)date2).toLocalDate();
		Date date3 = trainingForDailyReport.getEndDate();
		LocalDate end = ((java.sql.Date)date3).toLocalDate();
		//１日ごとに表示させる.
		List<LocalDate> dates = new ArrayList<>();
		for(LocalDate startDateDailyReport = start; startDateDailyReport.isBefore(end); startDateDailyReport = startDateDailyReport.plusDays(1)) {
			dates.add(startDateDailyReport);
		}
		//講義最終日のみ最後に追加.
		dates.add(end);
		
		//Listにつめた日付をループしてdaily_reportテーブルにinsert.(date,training_id,student_idのみ)
		for(int j = 0; j < dates.size(); j++) {
			for(int z = 0; z < trainingForDailyReport.getStudentList().size(); z++) {
				DailyReport dailyReport = new DailyReport();
				dailyReport.setStudentId(trainingForDailyReport.getStudentList().get(z).getId());
				dailyReport.setTrainingId(form.getId());
				Date date = Date.from(dates.get(j).atStartOfDay(ZoneId.systemDefault()).toInstant());
				dailyReport.setDate(date);
				dailyReportRepository.insert(dailyReport);
			}
		}
	
	    //１週間ごとに表示させる.
	    List<LocalDate> weeklyDates = new ArrayList<>();
	    for(LocalDate weeklyReportStartDate = start; weeklyReportStartDate.isBefore(end); weeklyReportStartDate = weeklyReportStartDate.plusDays(7)) {
	    	//			dates.add(weeklyReportStartDate);
	    	LocalDate monday = weeklyReportStartDate.with(DayOfWeek.MONDAY);
	    	weeklyDates.add(monday);
	    }
	    for(int i = 0; i < weeklyDates.size(); i++) {
	    	WeeklyReport weeklyReport = new WeeklyReport();
	    	weeklyReport.setTrainingId(form.getId());
	    	weeklyReport.setInstructorName(trainingForDailyReport.getInstructor().getName());
	    	weeklyReport.setStartDate(Date.from(weeklyDates.get(i).atStartOfDay(ZoneId.systemDefault()).toInstant())); //LocalDateからDateへ変換.
	    	WeeklyReport weeklyReport2 = weeklyReportRepository.weeklyReportInsert(weeklyReport); //weekly_reportの一部をinsert.
	    	
	    	//student_impressionの一部をinsert.
	    	for(int j = 0; j < trainingForDailyReport.getStudentList().size(); j++) {
	    		StudentImpression studentImpression = new StudentImpression();
	    		studentImpression.setStudentName(trainingForDailyReport.getStudentList().get(j).getName());
	    		studentImpression.setWeeklyReportId(weeklyReport2.getId());
	    		studentImpressionRepository.insert(studentImpression);
	    	}
	    	
	    }
	}
	
	/**
	 * 管理者画面の生徒登録画面で研修を1件検索するためのサービス.
	 * @param id
	 * @return
	 */
	public Training loadJoin2Table(Integer id) {
		return trainingRepository.loadJoin2Table(id);
	}
	
	/**
	 * 管理者画面の日報閲覧画面で研修1つにつき受講している受講生一覧を取得するためのサービス.
	 * @param id
	 * @return
	 */
	public Training loadForAdmin(Integer id) {
		return trainingRepository.loadForAdmin(id);
	}
	
	/**
	 * 管理者画面で研修を研修名と日付で曖昧検索するためのサービス.
	 * @param name
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException 
	 */
	public List<Training> findByNameOrDate(String name, String startDate) throws ParseException{
		return trainingRepository.findByNameOrDate(name, startDate);
	}
	
	/**
	 * 管理者画面で研修を名前で曖昧検索するためのサービス.
	 * @param name
	 * @return
	 */
	public List<Training> findByName(String name){
		return trainingRepository.findByName(name);
	}
	
	/**
	 * 管理者画面で研修を日付で曖昧検索するためのサービス.
	 * @param startDate
	 * @return
	 */
	public List<Training> findByDate(String startDate){
		return trainingRepository.findByDate(startDate);
	}
	
	/**
	 * 管理者画面で研修を全件検索するためのサービス.
	 * @return
	 */
	public List<Training> findAllTraining(){
		return trainingRepository.findAllTraining();
	}
	
	/**
	 * 管理者画面で研修idを使って生徒一覧を取得するためのサービス.
	 * @param trainingId
	 * @return
	 */
	public List<Training> selectStudent(Integer trainingId){
		return trainingRepository.selectStudent(trainingId);
	}

}
