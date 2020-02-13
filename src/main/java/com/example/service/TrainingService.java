package com.example.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Training;
import com.example.form.TrainingRegisterForm;
import com.example.form.TrainingUpdateForm;
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
		trainingRepository.insert(training);
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

}
