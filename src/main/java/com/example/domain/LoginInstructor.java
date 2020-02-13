package com.example.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoginInstructor extends User {
	
	private static final long serialVersionUID = 1L;
	/** 受講生情報 */
	private final Instructor instructor;
	
	/**
	 * 通常の管理者情報に加え、認可用ロールを設定する。
	 * 
	 * @param Administrator　管理者情報
	 * @param authorityList 権限情報が入ったリスト
	 */
	public LoginInstructor(Instructor instructor, Collection<GrantedAuthority> authorityList) {
		super(instructor.getEmail(), instructor.getPassword(), authorityList);
		this.instructor = instructor;
	}

	public Instructor getInstructor() {
		return instructor;
	}

}
