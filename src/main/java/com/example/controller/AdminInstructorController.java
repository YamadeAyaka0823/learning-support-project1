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

import com.example.domain.Instructor;
import com.example.domain.LoginAdmin;
import com.example.form.AdminLoginForm;
import com.example.form.InstructorRegisterForm;
import com.example.form.InstructorUpdateForm;
import com.example.service.InstructorService;

@Controller
@RequestMapping("/admin")
public class AdminInstructorController {
	
	@Autowired
	private InstructorService instructorService;
	
	@ModelAttribute
	public AdminLoginForm setUpForm() {
		return new AdminLoginForm();
	}
	
	@ModelAttribute
	public InstructorRegisterForm setUpForm2() {
		return new InstructorRegisterForm();
	}
	
	/**
	 * 講師一覧画面
	 * @return
	 */
	@RequestMapping("/instructor_list") 
	public String instructorList(String name, Model model, @AuthenticationPrincipal LoginAdmin loginAdmin) {
		//ログインしている管理者のIDを取得
		Integer adminId = loginAdmin.getAdmin().getId();
		model.addAttribute("adminId", adminId);
		
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
	@RequestMapping("/i_register")
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
		return "redirect:/admin/instructor_list";
	}
	
	/**
	 * 講師情報を編集するための初期画面.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/i_edit")
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
		return "redirect:/admin/instructor_list";
	}


}
