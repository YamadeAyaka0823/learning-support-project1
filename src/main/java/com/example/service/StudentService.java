package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Student;
import com.example.form.StudentLoginForm;
import com.example.repository.StudentRepository;

@Service
@Transactional
public class StudentService {
	
	@Autowired
	private StudentRepository studentRepository;
	
	/**
	 * 受講者がログインするためのサービス.
	 * @param form
	 * @return
	 */
	public Student findByEmailAndPassword(StudentLoginForm form) {
		return studentRepository.findByEmailAndPassword(form.getEmail(), form.getPassword());
	}

}
