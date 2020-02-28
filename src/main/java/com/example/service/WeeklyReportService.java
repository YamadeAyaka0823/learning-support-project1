package com.example.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.StudentImpression;
import com.example.domain.Training;
import com.example.domain.WeeklyReport;
import com.example.form.StudentImpressionForm;
import com.example.form.WeeklyReportForm;
import com.example.form.WeeklyReportUpdateForm;
import com.example.repository.StudentImpressionRepository;
import com.example.repository.WeeklyReportRepository;

@Service
@Transactional
public class WeeklyReportService {
	
	@Autowired
	private WeeklyReportRepository weeklyReportRepository;
	
	@Autowired
	private StudentImpressionRepository studentImpressionRepository;
	
	
	/**
	 * 週報を登録するサービス.
	 * @param form
	 * @throws ParseException
	 */
	public void insert(WeeklyReportForm form, StudentImpressionForm form2) throws ParseException {
		//週報をインサートする
		WeeklyReport weeklyReport = new WeeklyReport();
		weeklyReport.setContent(form.getInstructorContent());
		weeklyReport.setTrainingId(form.getTrainingId());
		
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = sdFormat.parse(form.getStartDate());
		weeklyReport.setStartDate(date);
		weeklyReportRepository.weeklyReportUpdate(weeklyReport);
		
		//受講生所感をインサートするためにweeklyReportIdを検索.
		WeeklyReport weeklyReport2 = weeklyReportRepository.loadByDate(date, form.getTrainingId());
		
		//受講生所感をインサートする
		for(int i =0; form.getStudentImpressionList().size() > i; i++) {
			StudentImpression studentImpression = new StudentImpression();
			studentImpression.setStudentName(form.getStudentImpressionList().get(i).getStudentName());
			studentImpression.setContent(form.getStudentImpressionList().get(i).getContent());
			studentImpression.setWeeklyReportId(weeklyReport2.getId());
			studentImpressionRepository.update(studentImpression);
		}
		
		
	}
	
	/**
	 * 週報を1件検索するためのサービス.
	 * @param id
	 * @return
	 */
	public WeeklyReport load(Integer id) {
		return weeklyReportRepository.load(id);
	}
	
	/**
	 * 週報の登録日の最大を取得するサービス.
	 * @param startDate
	 * @return
	 */
	public WeeklyReport maxStartDate(Date startDate) {
		return weeklyReportRepository.maxStartDate(startDate);
	}
	
	/**
	 * 週報を日付で1件検索するためのサービス.
	 * @param weeklyReportId
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public WeeklyReport loadByDate(Date date, Integer trainingId) throws ParseException {
        return weeklyReportRepository.loadByDate(date, trainingId);
	}
	
	/**
	 * 週報を編集するためのサービス.
	 * @param weeklyReportForm
	 * @param studentImpressionForm
	 */
	public void update(WeeklyReportUpdateForm weeklyReportForm) {
		//週報をupdateする
		WeeklyReport weeklyReport = new WeeklyReport();
		weeklyReport.setContent(weeklyReportForm.getInstructorContent());
		weeklyReport.setId(weeklyReportForm.getWeeklyReportId());
		weeklyReport.setTrainingId(weeklyReportForm.getTrainingId());
		weeklyReportRepository.update(weeklyReport);
		
		//受講生所感をupdateする
		for(int i =0; weeklyReportForm.getStudentImpressionList().size() > i; i++) {
			StudentImpression studentImpression = new StudentImpression();
			studentImpression.setId(weeklyReportForm.getStudentImpressionList().get(i).getId());
			studentImpression.setContent(weeklyReportForm.getStudentImpressionList().get(i).getContent());
			studentImpression.setWeeklyReportId(weeklyReport.getId());
			studentImpressionRepository.update(studentImpression);
		}
	}
	
	/**
	 * 週報の１部をインサート.
	 * @param weeklyReport
	 */
	public WeeklyReport weeklyReportInsert(WeeklyReport weeklyReport) {
		return weeklyReportRepository.weeklyReportInsert(weeklyReport);
	}
	
	/**
	 * 週報を印刷するために1件検索するためのサービス.
	 * @param weeklyReportId
	 * @return
	 */
	public WeeklyReport printWeeklyReport(Integer weeklyReportId) {
		return weeklyReportRepository.printWeeklyReport(weeklyReportId);
	}
	
	/**
	 * 週報を検索するためのサービス.
	 * @param trainingId
	 * @return
	 */
	public Training loadForWeeklyReport(Integer trainingId) {
		return weeklyReportRepository.loadForWeeklyReport(trainingId);
	}
	
}
