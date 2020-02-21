package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Instructor;
import com.example.domain.InstructorPasswordResetToken;
import com.example.form.InstructorLoginForm;
import com.example.form.InstructorRegisterForm;
import com.example.form.InstructorUpdateForm;
import com.example.repository.InstructorPasswordTokenRepository;
import com.example.repository.InstructorRepository;

@Service
@Transactional
public class InstructorService {
	
	@Autowired
	private InstructorRepository instructorRepository;
	
	@Autowired
	private InstructorPasswordTokenRepository instructorPasswordTokenRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * 講師がログインするためのサービス.
	 * @param form
	 * @return
	 */
	public Instructor findByEmailAndPassword(InstructorLoginForm form) {
		return instructorRepository.findByEmailAndPassword(form.getEmail(), form.getPassword());
	}
	
	/**
	 * 講師がログインするためのサービス.
	 * @param form
	 * @return
	 */
	public Instructor findByEmail(String email) {
		return instructorRepository.findByEmail(email);
	}
	
	/**
	 * 講師の研修一覧のためのサービス.
	 * @param id
	 * @return
	 */
	public Instructor load(Integer id) {
		return instructorRepository.load(id);
	}
	
	/**
	 * 管理者画面で講師一覧のためのサービス.
	 * @return
	 */
	public List<Instructor> findAll(){
		return instructorRepository.findAll();
	}
	
	/**
	 * 管理者画面で講師を新規登録するためのサービス.
	 * @param form
	 */
	public void insert(InstructorRegisterForm form) {
		Instructor instructor = new Instructor();
		instructor.setName(form.getName());
		instructor.setKana(form.getKana());
		instructor.setEmail(form.getEmail());
		// パスワードをハッシュ化する
		String encodePassword = passwordEncoder.encode(form.getPassword());
		instructor.setPassword(encodePassword);
		instructor.setAffiliation(form.getAffiliation());
		instructor.setRemarks(form.getRemarks());
		instructorRepository.insert(instructor);
	}
	
	/**
	 * 管理者画面で講師情報を編集するために講師を1件検索するためのサービス.
	 * @param id
	 * @return
	 */
	public Instructor oneLoad(Integer id) {
		return instructorRepository.oneLoad(id);
	}
	
	/**
	 * 管理者画面で講師情報を更新するためのサービス.
	 * @param form
	 */
	public void update(InstructorUpdateForm form) {
		Instructor instructor = new Instructor();
		instructor.setId(form.getIntId());
		instructor.setName(form.getName());
		instructor.setKana(form.getKana());
		instructor.setEmail(form.getEmail());
		instructor.setAffiliation(form.getAffiliation());
		instructor.setRemarks(form.getRemarks());
		instructorRepository.update(instructor);
	}
	
	/**
	 * 管理者画面で講師を曖昧検索するためのサービス.
	 * @param name
	 * @return
	 */
	public List<Instructor> findByName(String name){
		return instructorRepository.findByName(name);
	}
	
	////////////////////////////////////////////////////////////////////////
	/**
	 * student_tokenテーブルにinsertするためのサービス.
	 * @param student
	 * @param token
	 */
	public void createPasswordResetTokenForInstructor(Instructor instructor, String token) {
		InstructorPasswordResetToken myToken = new InstructorPasswordResetToken();
		myToken.setInstructor(instructor);
		myToken.setToken(token);
		instructorPasswordTokenRepository.save(myToken);
	}

	/**
	 * 新しいパスワードをインサートするためのサービス.
	 * @param student
	 * @param password
	 */
	public void changeStudentPassword(Instructor instructor, String password) {
		instructor.setPassword(passwordEncoder.encode(password));
		instructorRepository.saveNewPassword(instructor);
	}
	
	

}
