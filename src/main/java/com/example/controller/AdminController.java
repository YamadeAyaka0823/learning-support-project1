package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Admin;
import com.example.domain.AdminCompany;
import com.example.domain.Company;
import com.example.form.AdminRegisterForm;
import com.example.form.AdminUpdateForm;
import com.example.service.AdminService;
import com.example.service.CompanyService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private CompanyService companyService;
	
	@ModelAttribute
	public AdminRegisterForm setUpForm() {
		return new AdminRegisterForm();
	}
	
	/**
	 * 管理者画面で管理者一覧を表示するためのコントローラー.
	 * @return
	 */
	@RequestMapping("/admin_list")
	public String adminList(Model model) {
		List<Admin> adminList = adminService.findAll();
		model.addAttribute("adminList", adminList);
		return "admin/facility_manager_list";
	}
	
	/**
	 * 管理者画面で管理者を新規登録する初期画面.
	 * @return
	 */
	@RequestMapping("/register")
	public String register(Model model) {
		List<Company> companyList = companyService.findAllName();
		model.addAttribute("companyList", companyList);
		return "admin/facility_manager_detail";
	}
	
	/**
	 * 管理者画面で管理者を新規登録する.
	 * @param form
	 * @param adminCompany
	 * @return
	 */
	@RequestMapping("/admin_register")
	public String adminRegister(@Validated AdminRegisterForm form, BindingResult result, AdminCompany adminCompany, Model model) {
		if(result.hasErrors()) {
			return register(model);
		}
		adminService.insert(form, adminCompany);
		return "redirect:/admin/admin_list";
	}
	
	/**
	 * 管理者画面で管理者情報変更の初期画面.
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(Integer id, Model model) {
		Admin admin = adminService.load(id);
		model.addAttribute("admin", admin);
		List<Company> companyList = companyService.findAllName();
		model.addAttribute("companyList", companyList);
		return "admin/facility_manager_edit";
	}
	
	/**
	 * 管理者画面で管理者の情報を更新.
	 * @param form
	 * @param adminCompany
	 * @return
	 */
	@RequestMapping("/admin_edit")
	public String adminEdit(AdminUpdateForm form, AdminCompany adminCompany) {
		adminService.update(form, adminCompany);
		return "redirect:/admin/admin_list";
	}

}
