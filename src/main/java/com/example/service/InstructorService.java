package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Instructor;
import com.example.form.InstructorLoginForm;
import com.example.repository.InstructorRepository;

@Service
@Transactional
public class InstructorService {
	
	@Autowired
	private InstructorRepository instructorRepository;
	
	/**
	 * 講師がログインするためのサービス.
	 * @param form
	 * @return
	 */
	public Instructor findByEmailAndPassword(InstructorLoginForm form) {
		return instructorRepository.findByEmailAndPassword(form.getEmail(), form.getPassword());
	}
	
	/**
	 * 講師の研修一覧のためのサービス.
	 * @param id
	 * @return
	 */
	public Instructor load(Integer id) {
		return instructorRepository.load(id);
	}
	
	

}
