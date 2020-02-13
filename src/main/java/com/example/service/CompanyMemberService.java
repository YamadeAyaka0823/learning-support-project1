package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.CompanyMember;
import com.example.form.CompanyMemberLoginForm;
import com.example.repository.CompanyMemberRepository;

@Service
@Transactional
public class CompanyMemberService {
	
	@Autowired
	private  CompanyMemberRepository companyMemberRepository;
	
	
	/**
	 * 企業担当者がログインするためのサービス.
	 * @param form
	 * @return
	 */
	public CompanyMember findByEmailAndPassword(CompanyMemberLoginForm form) {
		return companyMemberRepository.findByEmailAndPassword(form.getEmail(), form.getPassword());
	}
	
	/**
	 * 企業担当者がログインするためのサービス.
	 * @param form
	 * @return
	 */
	public CompanyMember findByEmail(String email) {
		return companyMemberRepository.findByEmail(email);
	}
	
	/**
	 * 管理者画面で企業担当者を新規登録するためのサービス.
	 * @param form
	 */
	public void insert(CompanyMember companyMember) {
		companyMemberRepository.insert(companyMember);
	}
	
	/**
	 * 管理者画面の企業担当者登録画面の下に担当者一覧を表示させるためのサービス.
	 * @param companyId
	 * @return
	 */
	public List<CompanyMember> findByCompanyId(Integer companyId){
		return companyMemberRepository.findByCompanyId(companyId);
	}
	
	/**
	 * 管理者画面で企業担当者を削除するサービス.
	 * @param id
	 */
	public void delete(Integer id) {
		companyMemberRepository.delete(id);
	}

}
