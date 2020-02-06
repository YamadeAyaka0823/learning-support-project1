package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.CompanyMember;
import com.example.domain.Training;
import com.example.form.CompanyMemberLoginForm;
import com.example.service.CompanyMemberService;
import com.example.service.CompanyService;

@Controller
@RequestMapping("/company")
public class CompanyController {
	
	@Autowired
	private CompanyMemberService companyMemberService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private HttpSession session;
	
	@ModelAttribute
	public CompanyMemberLoginForm setUpForm() {
		return new CompanyMemberLoginForm();
	}
	
	/**
	 * 企業のログイン初期画面.
	 * @return
	 */
	@RequestMapping("")
	public String index() {
		return "company/company_login";
	}
	
	/**
	 * ログインするためのコントローラー.
	 * @param form
	 * @return
	 */
	@RequestMapping("/login")
	public String login(@Validated CompanyMemberLoginForm form, BindingResult result, Model model) {
		CompanyMember companyMember = companyMemberService.findByEmailAndPassword(form);
		if(companyMember == null) {
			model.addAttribute("error", "メールアドレスかパスワードが間違っています");
			return index();
		}
		if(result.hasErrors()) {
			return index();
		}
		session.setAttribute("company_id", companyMember.getCompanyId());
		return "forward:/company/list";
	}
	
	/**
	 * 会社が受け持つ研修の一覧を表示するコントローラー.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public String list(Model model) {
		Integer id = (Integer) session.getAttribute("company_id"); //sessionに入れたcompany_idで検索.
		List<Training> trainingList = companyService.load(id);
		model.addAttribute("trainingList", trainingList);
		return "company/company_training_list";
	}

}
