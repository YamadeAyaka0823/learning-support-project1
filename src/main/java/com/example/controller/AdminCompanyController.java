package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Admin;
import com.example.domain.Company;
import com.example.domain.CompanyMember;
import com.example.domain.LoginAdmin;
import com.example.form.CompanyMemberRegisterForm;
import com.example.form.CompanyRegisterForm;
import com.example.form.CompanyUpdateForm;
import com.example.service.AdminService;
import com.example.service.CompanyMemberService;
import com.example.service.CompanyService;

@Controller
@RequestMapping("/admin")
public class AdminCompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CompanyMemberService companyMemberService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@ModelAttribute
	public CompanyRegisterForm setUpForm() {
		return new CompanyRegisterForm();
	}
	
	@ModelAttribute
	public CompanyMemberRegisterForm setUpForm2() {
		return new CompanyMemberRegisterForm();
	}
	
	/**
	 * 企業一覧初期画面.
	 * @return
	 */
	@RequestMapping("/company_list")
	public String companyList(String name, Model model, @AuthenticationPrincipal LoginAdmin loginAdmin) {
		//ログインしている管理者のIDを取得
		Integer adminId = loginAdmin.getAdmin().getId();
		session.setAttribute("adminId", adminId);
		
		List<Company> companyList = null;
		List<Admin> adminList = null;
		if(name == null) {
			Admin admin = adminService.load(adminId);
			if(admin.getCanShowAllCompany() == true) {
			companyList = companyService.companyFindAll(); //全件検索
			model.addAttribute("companyList", companyList);
			}else {
			adminList = adminService.loadByAdminId(adminId);
     		model.addAttribute("adminList", adminList);
			}
		}else {
			Admin admin = adminService.load(adminId);
			if(admin.getCanShowAllCompany() == true) {
				companyList = companyService.findByName(name); //曖昧検索
				model.addAttribute("companyList", companyList);
			}else {
				adminList = adminService.findByNameAndAdminId(name, adminId);
				model.addAttribute("adminList", adminList);
			}
		}
		
		return "admin/company_list";
	}
	
	/**
	 * 企業新規登録初期画面.
	 * @return
	 */
	@RequestMapping("/c_register")
	public String register() {
		return "admin/company_detail";
	}
	
	/**
	 * 企業の新規登録.
	 * @param form
	 * @return
	 */
	@RequestMapping("/company_register")
	public String companyRegister(@Validated CompanyRegisterForm form, BindingResult result) {
		if(result.hasErrors()) {
			return register();
		}
		companyService.insert(form);
		return "redirect:/admin/company_list";
	}
	
	/**
	 * 企業を編集する初期画面.
	 * @return
	 */
	@RequestMapping("/c_edit")
	public String edit(Integer id, Model model) {
		Company company = companyService.oneLoad(id);
		model.addAttribute("company", company);
		return "admin/company_edit";
	}
	
	/**
	 * 企業を編集するためのコントローラー.
	 * @param form
	 * @return
	 */
	@RequestMapping("/company_edit")
	public String companyEdit(CompanyUpdateForm form) {
		companyService.update(form);
		return "redirect:/admin/company_list";
	}
	
	/**
	 * 企業担当者を新規登録する初期画面.
	 * @return
	 */
	@RequestMapping("/companyMember_register")
	public String companyMemberRegister(Integer id, Model model) {
		Company company = companyService.oneLoad(id);
		model.addAttribute("company", company);
		List<CompanyMember> companyMemberList = companyMemberService.findByCompanyId(id);
		model.addAttribute("companyMemberList", companyMemberList);
		return "admin/company_register_charge";
	}
	
	/**
	 * 企業担当者を新規登録する.
	 * @param form
	 * @param model
	 * @return
	 */
	@RequestMapping("/companyMember_insert")
	public String companyMemberInsert(Integer companyId, @Validated CompanyMemberRegisterForm form, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return companyMemberRegister(companyId, model);
		}
		CompanyMember companyMember = new CompanyMember();
		companyMember.setCompanyId(companyId);
		companyMember.setEmail(form.getEmail());
		companyMember.setKana(form.getKana());
		companyMember.setName(form.getName());
		if(form.getPassword().equals(form.getConfirmPassword())) {
			// パスワードをハッシュ化する
			String encodePassword = passwordEncoder.encode(form.getPassword());
			companyMember.setPassword(encodePassword);
		}else {
			model.addAttribute("error", "確認用パスワードが間違っています");
			return companyMemberRegister(companyId, model);
		}
		companyMemberService.insert(companyMember);
		return "redirect:/admin/company_list";
	}
	
	/**
	 * 企業担当者を削除する.
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	public String delete(Integer id, Model model) {
		companyMemberService.delete(id);
		return "redirect:/admin/company_list";
	}

}
