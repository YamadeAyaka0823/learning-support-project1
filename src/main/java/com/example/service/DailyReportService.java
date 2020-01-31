package com.example.service;

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

}
