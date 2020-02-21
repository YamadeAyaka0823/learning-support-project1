package com.example.controller;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.Admin;
import com.example.domain.AdminCompany;
import com.example.domain.Company;
import com.example.domain.LoginAdmin;
import com.example.form.AdminRegisterForm;
import com.example.form.AdminUpdateForm;
import com.example.form.ChangePasswordForm;
import com.example.service.AdminSecurityService;
import com.example.service.AdminService;
import com.example.service.CompanyService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private AdminSecurityService adminSecurityService;
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
    private MessageSource messages;
	
	@ModelAttribute
	public AdminRegisterForm setUpForm() {
		return new AdminRegisterForm();
	}
	
	/**
	 * 管理者画面で管理者一覧を表示するためのコントローラー.
	 * @return
	 */
	@RequestMapping("/admin_list")
	public String adminList(Model model, @AuthenticationPrincipal LoginAdmin loginAdmin) {
		//ログインしている管理者のIDを取得
		Integer adminId = loginAdmin.getAdmin().getId();
		model.addAttribute("adminId", adminId);
		
		List<Admin> adminList = adminService.findAll();
		model.addAttribute("adminList", adminList);
		return "admin/facility_manager_list";
	}
	
	/**
	 * 管理者画面で管理者を新規登録する初期画面.
	 * @return
	 */
	@RequestMapping("/a_register")
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
	@RequestMapping("/a_edit")
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
	
	////////////////////////////////////////////////////////////////////////
	/**
	 * パスワード変更初期画面.
	 * @return
	 */
	@RequestMapping("/admin_forgot_password")
	public String forgotPassword() {
		return "admin/admin_forgotPassword";
	}

	/**
	 * パスワードを変更するためのリンク付きメールを送るためのコントローラー.
	 * @param request
	 * @param email
	 * @return
	 */
	@RequestMapping(value="/admin_resetPassword", method= RequestMethod.POST)
	//@ResponseBody
	public String resetPassword(HttpServletRequest request,@RequestParam("email") String email) {
		Admin admin = adminService.findByEmail(email); //入力されたメールアドレスからstudentを検索する.
		String token = UUID.randomUUID().toString(); //トークンを発行.
		adminService.createPasswordResetTokenForStudent(admin, token); //studentとtokenをstudent_tokenテーブルにinsertする.
		mailSender.send(constructResetTokenEmail(request.getLocale(), token, admin)); //メールを送る.
		return "success";
	}

	/**
	 * メールを送るためのメソッド.
	 * @param contextPath
	 * @param locale
	 * @param token
	 * @param student
	 * @return
	 */
	private SimpleMailMessage constructResetTokenEmail(Locale locale, String token, Admin admin) {
		String url = "http://localhost:8080/admin/admin_changePassword?id=" + admin.getId() + "&token=" + token; //トークンとstudentID付きのURL.
		String message = messages.getMessage("message.resetPassword", null, Locale.JAPANESE); //リンク付きURLと一緒に送るメッセージ(メッセージは別途、messages.propertiesに記載).
		return constructEmail("Reset Password", message + " \r\n" + url, admin);
	}

	/**
	 * メールを送るためのメソッド.
	 * @param subject
	 * @param body
	 * @param student
	 * @return
	 */
	private SimpleMailMessage constructEmail(String subject, String body, Admin admin) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject); //タイトルの設定
		email.setText(body); //メールの本文
		email.setTo(admin.getEmail());
		return email;
	}

	/**
	 * メールに送られてきたリンクをクリックすると飛んでくるコントローラー.
	 * @param locale
	 * @param model
	 * @param id
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/admin_changePassword", method= RequestMethod.GET)
	public String showChangePasswordPage(Locale locale, Model model, @RequestParam("id") long id, @RequestParam("token") String token) {
		String result = adminSecurityService.validatePasswordResetToken(id, token);
		if(result != null) {
			model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
			return "redirect:/login?lang=" + locale.getLanguage();
		}
		return "admin/admin_updatePassword";
	}

	/**
	 * 新しいパスワードを保存するためのコントローラー.
	 * @param locale
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/admin_savePassword", method= RequestMethod.POST)
	//@ResponseBody
	public String savePassword(Locale locale, ChangePasswordForm form) {
		Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //生徒の情報を取得.
		adminService.changeStudentPassword(admin,form.getNewPassword()); //新しいパスワードをインサートする.
		return "changePasswordComplete";
	}

}
