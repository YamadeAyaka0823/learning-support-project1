package com.example.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.PasswordResetToken;
import com.example.domain.Student;
import com.example.repository.PasswordTokenRepository;

@Service
@Transactional
public class SecurityService {
	
	@Autowired
	private PasswordTokenRepository passwordTokenRepository;
	
	public String validatePasswordResetToken(long id, String token) {
		PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
		
		if((passToken == null) || (passToken.getStudent().getId() != id)) {
			return "invalidToken";
		}
		
//		Calendar cal = Calendar.getInstance();
//		if((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
//			return "expired";
//		}
		
		Student student = passToken.getStudent();
		Authentication auth = new UsernamePasswordAuthenticationToken(student, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return null;
	}

}
