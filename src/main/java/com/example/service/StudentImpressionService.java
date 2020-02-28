package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.StudentImpression;
import com.example.repository.StudentImpressionRepository;

@Service
@Transactional
public class StudentImpressionService {
	
	@Autowired
	private StudentImpressionRepository studentImpressionRepository;
	
	/**
	 * 生徒の所感の１部をインサートするためのサービス.
	 * @param studentImpression
	 */
	public void insert(StudentImpression studentImpression) {
		studentImpressionRepository.insert(studentImpression);
	}

}
