package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Training;
import com.example.repository.TrainingRepository;

@Service
@Transactional
public class TrainingService {
	
	@Autowired
	private TrainingRepository trainingRepository;
	
	
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

}
