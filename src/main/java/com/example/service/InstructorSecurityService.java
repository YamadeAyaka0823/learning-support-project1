package com.example.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Instructor;
import com.example.domain.InstructorPasswordResetToken;
import com.example.repository.InstructorPasswordTokenRepository;

@Service
@Transactional
public class InstructorSecurityService {
	
	@Autowired
	private InstructorPasswordTokenRepository instructorPasswordTokenRepository;
	
	public String validatePasswordResetToken(long id, String token) {
		InstructorPasswordResetToken passToken = instructorPasswordTokenRepository.findByToken(token);
		
		if((passToken == null) || (passToken.getInstructor().getId() != id)) {
			return "invalidToken";
		}
		
//		Calendar cal = Calendar.getInstance();
//		if((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
//			return "expired";
//		}
		
		Instructor instructor = passToken.getInstructor();
		Authentication auth = new UsernamePasswordAuthenticationToken(instructor, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return null;
	}

}
