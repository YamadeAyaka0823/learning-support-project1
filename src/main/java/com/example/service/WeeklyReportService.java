package com.example.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.StudentImpression;
import com.example.domain.Training;
import com.example.domain.WeeklyReport;
import com.example.form.StudentImpressionForm;
import com.example.form.WeeklyReportForm;
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
		weeklyReport.setInstructorName(form.getInstructorName());
		weeklyReport.setTrainingId(form.getTrainingId());
		
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = sdFormat.parse(form.getStartDate());
		weeklyReport.setStartDate(date);
		weeklyReportRepository.insert(weeklyReport);
		
		//受講生所感をインサートする
		for(int i =0; form.getStudentImpressionList().size() > i; i++) {
			StudentImpression studentImpression = new StudentImpression();
			studentImpression.setStudentName(form.getStudentImpressionList().get(i).getStudentName());
			studentImpression.setContent(form.getStudentImpressionList().get(i).getContent());
			studentImpression.setWeeklyReportId(weeklyReport.getId());
			studentImpressionRepository.insert(studentImpression);
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

}
