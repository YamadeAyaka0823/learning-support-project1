package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Student;
import com.example.form.StudentLoginForm;
import com.example.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	@ModelAttribute
	public StudentLoginForm setUpForm() {
		return new StudentLoginForm();
	}
	
	/**
	 * 受講生ログイン初期画面.
	 * @return
	 */
	@RequestMapping("")
	public String index() {
		return "student/student_login";
	}
	
	/**
	 * 受講生のログイン.
	 * @param form
	 * @return
	 */
	@RequestMapping("/login")
	public String login(@Validated StudentLoginForm form, BindingResult result, Model model) {
		Student student = studentService.findByEmailAndPassword(form);
		if(student == null) {
			model.addAttribute("error", "メールアドレスかパスワードが間違っています");
			return index();
		}
		if(result.hasErrors()) {
			return index();
		}
		return "student/student_training_list";
	}
	

}
