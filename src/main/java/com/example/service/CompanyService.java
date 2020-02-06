package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Company;
import com.example.domain.Training;
import com.example.form.CompanyRegisterForm;
import com.example.form.CompanyUpdateForm;
import com.example.repository.CompanyRepository;

@Service
@Transactional
public class CompanyService {
	
	@Autowired
	private CompanyRepository companyRepository;
	
	
	/**
	 * 会社が受け持つ研修の一覧を表示するサービス.
	 * @param id
	 * @return
	 */
	public List<Training> load(Integer id){
		return companyRepository.load(id);
	}
	
	/**
	 * 管理者画面で企業一覧を表示するためのサービス.
	 * @return
	 */
	public List<Company> companyFindAll(){
		return companyRepository.companyFindAll();
	}
	
	/**
	 * 管理者画面の管理者新規登録で担当企業一覧を表示するためのサービス.
	 * @return
	 */
	public List<Company> findAllName(){
		return companyRepository.findAllName();
	}
	
	/**
	 * 管理者画面で企業を新規登録するためのサービス.
	 * @param form
	 */
	public void insert(CompanyRegisterForm form) {
		Company company = new Company();
		company.setKana(form.getKana());
		company.setName(form.getName());
		company.setRemarks(form.getRemarks());
		companyRepository.insert(company);
	}
	
	/**
	 * 管理者画面で企業を編集するために1件検索するためのサービス.
	 * @param id
	 * @return
	 */
	public Company oneLoad(Integer id) {
		return companyRepository.oneLoad(id);
	}
	
	/**
	 * 管理者画面で企業を編集するためのサービス.
	 * @param form
	 */
	public void update(CompanyUpdateForm form) {
		Company company = new Company();
		company.setId(form.getIntId());
		company.setKana(form.getKana());
		company.setName(form.getName());
		company.setRemarks(form.getRemarks());
		companyRepository.update(company);
	}
	
	/**
	 * 管理者画面で企業を曖昧検索するためのサービス.
	 * @param name
	 * @return
	 */
	public List<Company> findByName(String name){
		return companyRepository.findByName(name);
	}

}
