package com.example.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


public class LoginStudent extends User {
	
	private static final long serialVersionUID = 1L;
	/** 受講生情報 */
	private final Student student;
	
	/**
	 * 通常の管理者情報に加え、認可用ロールを設定する。
	 * 
	 * @param Administrator　管理者情報
	 * @param authorityList 権限情報が入ったリスト
	 */
	public LoginStudent(Student student, Collection<GrantedAuthority> authorityList) {
		super(student.getEmail(), student.getPassword(), authorityList);
		this.student = student;
	}

	public Student getStudent() {
		return student;
	}

}
