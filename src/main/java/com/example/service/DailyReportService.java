package com.example.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.DailyReport;
import com.example.form.DailyReportForm;
import com.example.repository.DailyReportRepository;

@Service
@Transactional
public class DailyReportService {
	
	@Autowired
	private DailyReportRepository dailyReportRepository;
	
	
	/**
	 * 講師が生徒の日報を閲覧するためのサービス.
	 * @param id
	 * @return
	 */
	public DailyReport instructorViewDailyReport(Integer id) {
		return dailyReportRepository.instructorViewDailyReport(id);
	}
	
	/**
	 * 講師が受講生の日報を日付と名前で検索して閲覧するためのサービス.
	 * @param trainingId
	 * @param date
	 * @param name
	 * @return
	 * @throws ParseException
	 */
	public DailyReport dateAndNameLoad(Integer trainingId, Date date, String name) throws ParseException {
        return dailyReportRepository.dateAndNameLoad(trainingId, date, name);
	}
	
	/**
	 * 日報を変更するためのサービス.
	 * @param form
	 * @throws ParseException
	 */
	public void update(DailyReportForm form) throws ParseException {
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
		dailyReportRepository.update(dailyReport);
	}
	
	/**
	 * 生徒を登録する際に、日報の一部もインサートするためのサービス.
	 * @param dailyReport
	 */
	public void insert(DailyReport dailyReport) {
		dailyReportRepository.insert(dailyReport);
	}
	
	/**
	 * 日報の印刷の初期画面のための1件検索.
	 * @param dailyReportId
	 * @return
	 */
	public DailyReport printDailyReport(Integer dailyReportId) {
		return dailyReportRepository.printDailyReport(dailyReportId);
	}

}
