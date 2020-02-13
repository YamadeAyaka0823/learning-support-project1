package com.example.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.DailyReport;
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
	public DailyReport dateAndNameLoad(Integer trainingId, String date, String name) throws ParseException {
		//String型のdateをDate型に変換.
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfDailyReport = sdFormat.parse(date);
        return dailyReportRepository.dateAndNameLoad(trainingId, dateOfDailyReport, name);
	}

}
