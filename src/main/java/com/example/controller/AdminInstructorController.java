package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.Admin;
import com.example.domain.Instructor;
import com.example.domain.LoginAdmin;
import com.example.domain.Training;
import com.example.form.AdminLoginForm;
import com.example.form.InstructorRegisterForm;
import com.example.form.InstructorUpdateForm;
import com.example.service.AdminService;
import com.example.service.InstructorService;
import com.example.service.TrainingService;

@Controller
@RequestMapping("/adminInstructor")
public class AdminInstructorController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private InstructorService instructorService;
	
	@Autowired
    private TrainingService trainingService;
	
	@ModelAttribute
	public AdminLoginForm setUpForm() {
		return new AdminLoginForm();
	}
	
	@ModelAttribute
	public InstructorRegisterForm setUpForm2() {
		return new InstructorRegisterForm();
	}
	
	/**
	 * 管理者ログイン初期画面.
	 * @return
	 */
//	@RequestMapping("")
//	public String index() {
//		return "admin/admin_login";
//	}
	
	/**
	 * 管理者がログインするためのコントローラー.
	 * @param form
	 * @return
	 */
//	@RequestMapping("/admin_login")
//	public String login(@Validated AdminLoginForm form, BindingResult result, Model model) {
//		Admin admin = adminService.findByEmailAndPassword(form);
//		
//		if(admin == null) {
//			model.addAttribute("error", "メールアドレスかパスワードが間違っています");
//			return index();
//		}
//		
//		if(result.hasErrors()) {
//			return index();
//		}
//		List<Training> trainingList = trainingService.findAll();
//		model.addAttribute("trainingList", trainingList);
//		return "admin/admin_training_list";
//	}
	
	@RequestMapping("/admin_login")
	public String login(Model model,@RequestParam(required = false) String error) {
		
		if (error != null) {
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");

			
		}
		return "admin/admin_login";
	}
	
	/**
	 * 講師一覧画面
	 * @return
	 */
	@RequestMapping("/instructor_list") 
	public String instructorList(String name, Model model, @AuthenticationPrincipal LoginAdmin loginAdmin) {
		List<Instructor> instructorList = null;
		if(name == null) {
			instructorList = instructorService.findAll(); //全件検索
		}else {
			instructorList = instructorService.findByName(name); //曖昧検索
		}
				
		model.addAttribute("instructorList", instructorList);
		return "admin/instructor_list";
	}
	
	/**
	 * 講師の新規登録初期画面.
	 * @return
	 */
	@RequestMapping("/register")
	public String register() {
		return "admin/instructor_detail";
	}
	
	/**
	 * 講師を新規登録するためのコントローラー.
	 * @param form
	 * @return
	 */
	@RequestMapping("/instructor_register")
	public String instructorRegister(@Validated InstructorRegisterForm form, BindingResult result) {
		
		if(result.hasErrors()) {
			return register();
		}
		instructorService.insert(form);
		return "redirect:/adminInstructor/instructor_list";
	}
	
	/**
	 * 講師情報を編集するための初期画面.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(Integer id, Model model) {
		Instructor instructor = instructorService.oneLoad(id); //講師の情報
		model.addAttribute("instructor", instructor);
		Instructor instructorTrainingList = instructorService.load(id); //講師1人が受け持つ研修一覧
		model.addAttribute("instructorTrainingList", instructorTrainingList);
		return "admin/instructor_edit";
	}
	
	/**
	 * 講師情報を変更するためのコントローラー.
	 * @param form
	 * @return
	 */
	@RequestMapping("/instructor_edit")
	public String instructorEdit(InstructorUpdateForm form) {
		instructorService.update(form);
		return "redirect:/adminInstructor/instructor_list";
	}
	
	/**
	 * 企業一覧初期画面.
	 * @return
	 */
//	@RequestMapping("/company_list")
//	public String companyList(Model model) {
//		List<Company> companyList = companyService.companyFindAll();
//		model.addAttribute("companyList", companyList);
//		return "admin/company_list";
//	}


}
