package com.example.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.domain.Instructor;
import com.example.domain.LoginInstructor;
import com.example.repository.InstructorRepository;
@Service
//@Component("com.example.service.InstructorDetailServiceImpl")
public class InstructorDetailServiceImpl implements UserDetailsService {
	
	/** DBから情報を得るためのリポジトリ */
	@Autowired
	private InstructorRepository instructorRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String) DBから検索をし、ログイン情報を構成して返す。
	 */
	@Override
	@Bean("instructor")
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {
		Instructor instructor = instructorRepository.findByEmail(email);
		if (instructor == null) {
			throw new UsernameNotFoundException("そのEmailは登録されていません。");
		}
		// 権限付与の例
		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_USER")); // ユーザ権限付与
//		if(member.isAdmin()) {
//			authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN")); // 管理者権限付与
//		}
		return new LoginInstructor(instructor,authorityList);
	}

}
