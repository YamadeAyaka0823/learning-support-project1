package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Admin;
import com.example.domain.AdminCompany;
import com.example.domain.Company;
import com.example.form.AdminLoginForm;
import com.example.form.AdminRegisterForm;
import com.example.form.AdminUpdateForm;
import com.example.repository.AdminCompanyRepository;
import com.example.repository.AdminRepository;
import com.example.repository.CompanyRepository;

@Service
@Transactional
public class AdminService {
	
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private AdminCompanyRepository adminCompanyRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	
	/**
	 * 管理者がログインするためのサービス.
	 * @param form
	 * @return
	 */
	public Admin findByEmailAndPassword(AdminLoginForm form) {
		return adminRepository.findByEmailAndPassword(form.getEmail(), form.getPassword());
	}
	
	/**
	 * 管理者がログインするためのサービス.
	 * @param form
	 * @return
	 */
	public Admin findByEmail(String email) {
		return adminRepository.findByEmail(email);
	}
	
	/**
	 * 管理者画面で管理者の一覧を取得するためのサービス.
	 * @return
	 */
	public List<Admin> findAll(){
		return adminRepository.findAll();
	}
	
	/**
	 * 管理者画面で管理者を新規登録するためのサービス.
	 * @param admin
	 * @param company
	 * @param adminCompany
	 */
	public void insert(AdminRegisterForm form, AdminCompany adminCompany) {
		
		Admin admin = new Admin();
		admin.setEmail(form.getEmail());
		admin.setKana(form.getKana());
		admin.setName(form.getName());
		//もし入力されたパスワードと確認用パスワードが同じなら
		if(form.getPassword().equals(form.getConfirmPassword())) {
			// パスワードをハッシュ化する
			String encodePassword = passwordEncoder.encode(form.getPassword());
			admin.setPassword(encodePassword);
		}
		//canShowAllCompanyがtrueかfalseか
		if(form.getResponsibleCompany().equals("true")) {
			admin.setCanShowAllCompany(true);
		}else {
			admin.setCanShowAllCompany(false);
		}
		Admin administrator = adminRepository.insert(admin);
		
			adminCompany.setAdminId(administrator.getId()); //insertしたadminのIDをadminCompanyテーブルのadmin_idにいれる
			
			//getCanShowAllCompanyがtrueだった場合
			if(admin.getCanShowAllCompany() == true) {
//				List<Company> companyList = companyRepository.findAllName();
//				for(int i = 0; i < companyList.size(); i++) {
//					Integer companyId = companyList.get(i).getId();
//					adminCompany.setCompanyId(companyId);
//					adminCompanyRepository.insert(adminCompany);
//				}
				return;
			}
			
				//getCanShowAllCompanyがfalseだった場合
				for(Integer companyId : form.getCompanyList()) {
					adminCompany.setCompanyId(companyId); //insertしたcompanyのIDをadminCompanyテーブルのcompany_idにいれる
					adminCompanyRepository.insert(adminCompany);
				}
			
		
		
	}
	
	/**
	 * 管理者画面で管理者編集画面で情報を表示するために1件検索するサービス.
	 * @param id
	 * @return
	 */
	public Admin load(Integer id) {
		return adminRepository.load(id);
	}
	
	public void update(AdminUpdateForm form, AdminCompany adminCompany) {
		Admin admin = new Admin();
		admin.setId(form.getIntId());
		admin.setEmail(form.getEmail());
		admin.setKana(form.getKana());
		admin.setName(form.getName());
		//canShowAllCompanyがtrueかfalseか
		if(form.getResponsibleCompany().equals("true")) {
			admin.setCanShowAllCompany(true);
		}else {
			admin.setCanShowAllCompany(false);
		}
		adminRepository.update(admin); //adminテーブルupdate
		adminCompanyRepository.delete(form.getIntId()); //中間テーブルを消す
		
		adminCompany.setAdminId(form.getIntId()); //insertしたadminのIDをadminCompanyテーブルのadmin_idにいれる
		
		//getCanShowAllCompanyがtrueだった場合
		if(admin.getCanShowAllCompany() == true) {
//			List<Company> companyList = companyRepository.findAllName();
//			for(int i = 0; i < companyList.size(); i++) {
//				Integer companyId = companyList.get(i).getId();
//				adminCompany.setCompanyId(companyId);
//				adminCompanyRepository.insert(adminCompany);
		return;
			}
		
	
			//getCanShowAllCompanyがfalseだった場合
			for(Integer companyId : form.getCompanyList()) {
				adminCompany.setCompanyId(companyId); //insertしたcompanyのIDをadminCompanyテーブルのcompany_idにいれる
				adminCompanyRepository.insert(adminCompany);
			}
		
			
	}
	
	/**
	 * 管理者IDで担当している企業を検索するためのサービス.
	 * @param id
	 * @return
	 */
	public List<Admin> loadByAdminId(Integer id){
		return adminRepository.loadByAdminId(id);
	}
	
	/**
	 * 名前とadminIdで企業を曖昧検索するためのサービス.
	 * @param name
	 * @param id
	 * @return
	 */
	public List<Admin> findByNameAndAdminId(String name, Integer id){
		return adminRepository.findByNameAndAdminId(name, id);
	}

}
