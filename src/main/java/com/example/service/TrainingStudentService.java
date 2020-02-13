package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.TrainingStudent;
import com.example.repository.TrainingStudentRepository;

@Service
@Transactional
public class TrainingStudentService {
	
	@Autowired
	private TrainingStudentRepository trainingStudentRepository;
	
	/**
	 * 管理者画面で生徒を登録する時、training_studentテーブルにもstudentIdとtrainingIdをinsertするためのサービス.
	 * @param trainingStudent
	 */
	public void insert(TrainingStudent trainingStudent) {
		trainingStudentRepository.insert(trainingStudent);
	}

}
