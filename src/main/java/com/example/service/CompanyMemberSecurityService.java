package com.example.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.CompanyMember;
import com.example.domain.CompanyMemberPasswordResetToken;
import com.example.repository.CompanyMemberPasswordTokenRepository;

@Service
@Transactional
public class CompanyMemberSecurityService {
	
	@Autowired
	private CompanyMemberPasswordTokenRepository companyMemberPasswordTokenRepository;
	
	public String validatePasswordResetToken(long id, String token) {
		CompanyMemberPasswordResetToken passToken = companyMemberPasswordTokenRepository.findByToken(token);
		
		if((passToken == null) || (passToken.getCompanyMember().getId() != id)) {
			return "invalidToken";
		}
		
//		Calendar cal = Calendar.getInstance();
//		if((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
//			return "expired";
//		}
		
		CompanyMember companyMember = passToken.getCompanyMember();
		Authentication auth = new UsernamePasswordAuthenticationToken(companyMember, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return null;
	}

}
