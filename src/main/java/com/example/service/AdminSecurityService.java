package com.example.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Admin;
import com.example.domain.AdminPasswordResetToken;
import com.example.repository.AdminPasswordTokenRepository;

@Service
@Transactional
public class AdminSecurityService {
	
	@Autowired
	private AdminPasswordTokenRepository adminPasswordTokenRepository;
	
	public String validatePasswordResetToken(long id, String token) {
		AdminPasswordResetToken passToken = adminPasswordTokenRepository.findByToken(token);
		
		if((passToken == null) || (passToken.getAdmin().getId() != id)) {
			return "invalidToken";
		}
		
//		Calendar cal = Calendar.getInstance();
//		if((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
//			return "expired";
//		}
		
		Admin admin = passToken.getAdmin();
		Authentication auth = new UsernamePasswordAuthenticationToken(admin, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return null;
	}

}
