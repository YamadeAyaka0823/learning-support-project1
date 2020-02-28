package com.example.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.CompanyMember;
import com.example.domain.DailyReport;
import com.example.domain.LoginCompanyMember;
import com.example.domain.Training;
import com.example.domain.WeeklyReport;
import com.example.form.ChangePasswordForm;
import com.example.form.CompanyMemberLoginForm;
import com.example.service.CompanyMemberSecurityService;
import com.example.service.CompanyMemberService;
import com.example.service.CompanyService;
import com.example.service.DailyReportService;
import com.example.service.StudentService;
import com.example.service.TrainingService;
import com.example.service.WeeklyReportService;

@Controller
@RequestMapping("/company")
public class CompanyController {
	
	@Autowired
	private CompanyMemberService companyMemberService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TrainingService trainingService;
	
	@Autowired
	private WeeklyReportService weeklyReportService;
	
	@Autowired
	private DailyReportService dailyReportService;
	
	@Autowired
	private CompanyMemberSecurityService companyMemberSecurityService;
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
    private MessageSource messages;
	
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
	@RequestMapping("/company_login")
	public String login(Model model,@RequestParam(required = false) String error) {
		
		if (error != null) {
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");

			
		}
		return "company/company_login";
	}
	
	/**
	 * 会社が受け持つ研修の一覧を表示するコントローラー.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public String list(Model model, @AuthenticationPrincipal LoginCompanyMember loginCompanyMember) {
		Integer id = loginCompanyMember.getCompanyMember().getCompanyId(); //company_idで検索.
		List<Training> trainingList = companyService.load(id);
		model.addAttribute("trainingList", trainingList);
		return "company/company_training_list";
	}
	
	/**
	 * 企業が受け持つ研修の受講生の日報を閲覧する.
	 * @return
	 */
	@RequestMapping("/view_daily_report")
	public String viewDailyReport(Integer id, Model model) {
		DailyReport dailyReport = studentService.loadForAdmin(id);
		//日付を年月日形式に変換
		Date date = dailyReport.getDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate = dateFormat.format(date);
		model.addAttribute("formattedDate", formattedDate);
				
		if(dailyReport.getIntelligibility() == 1) {
		   model.addAttribute("intelligibility", "良く理解できた");
		}else if(dailyReport.getIntelligibility() == 2) {
		   model.addAttribute("intelligibility", "概ね理解できた");
		}else if(dailyReport.getIntelligibility() == 3) {
		   model.addAttribute("intelligibility", "ふつう");
		}else if(dailyReport.getIntelligibility() == 4) {
		   model.addAttribute("intelligibility", "少し難しかった");
		}else if(dailyReport.getIntelligibility() == 5) {
		   model.addAttribute("intelligibility", "とても難しかった");
		}
				
		if(dailyReport.getAboutInstructor() == 1) {
			model.addAttribute("aboutInstructor", "とても丁寧だった");
		}else if(dailyReport.getAboutInstructor() == 2) {
			model.addAttribute("aboutInstructor", "概ね丁寧だった");
		}else if(dailyReport.getAboutInstructor() == 3) {
			model.addAttribute("aboutInstructor", "どちらともいえない");
		}else if(dailyReport.getAboutInstructor() == 4) {
			model.addAttribute("aboutInstructor", "やや丁寧ではなかった");
		}else if(dailyReport.getAboutInstructor() == 5) {
			model.addAttribute("aboutInstructor", "全く丁寧ではなかった");
		}
		model.addAttribute("dailyReport", dailyReport);
		
		  //研修の開始日と終了日を取得する
		  //DateをLocalDateに変換
		  Date startDate = dailyReport.getTraining().getStartDate();
		  LocalDate start = ((java.sql.Date)startDate).toLocalDate();
		  Date endDate = dailyReport.getTraining().getEndDate();
		  LocalDate end = ((java.sql.Date)endDate).toLocalDate();
		  //１日ごとに表示させる.
		  List<String> dates = new ArrayList<>();
		  for(LocalDate startDay = start; startDay.isBefore(end); startDay = startDay.plusDays(1)) {
			  DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); //yyyy/MM/dd形式に変換.
			  String formatStartDate = datetimeformatter.format(startDay);
			  dates.add(formatStartDate);
		  }	
		  //講義最終日のみ最後に追加.
		  DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); //yyyy/MM/dd形式に変換.
		  String formatEndDate = datetimeformatter.format(end);
		  dates.add(formatEndDate);
		  model.addAttribute("dates", dates);
		  //日報閲覧画面で生徒一覧のプルダウン.
		  Training training = trainingService.loadForAdmin(id);
		  model.addAttribute("training", training);
		return "company/company_view_daily_report";
	}
	
	/**
	 * 企業が受け持つ研修の受講生の日報を日付と名前で1件検索するためのコントローラー.
	 * @param trainingId
	 * @param date
	 * @param name
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/daily_search")
	public String dailySearch(Integer trainingId, String date, String name, Model model) throws ParseException {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date changeDate = sdFormat.parse(date);
        DailyReport dailyReport = dailyReportService.dateAndNameLoad(trainingId, changeDate, name);
		
		//日付を年月日形式に変換
				Date date3 = dailyReport.getDate();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
				String formattedDate = dateFormat.format(date3);
				model.addAttribute("formattedDate", formattedDate);
				
				if(dailyReport.getIntelligibility() == 1) {
					   model.addAttribute("intelligibility", "良く理解できた");
					}else if(dailyReport.getIntelligibility() == 2) {
						model.addAttribute("intelligibility", "概ね理解できた");
					}else if(dailyReport.getIntelligibility() == 3) {
						model.addAttribute("intelligibility", "ふつう");
					}else if(dailyReport.getIntelligibility() == 4) {
						model.addAttribute("intelligibility", "少し難しかった");
					}else if(dailyReport.getIntelligibility() == 5) {
						model.addAttribute("intelligibility", "とても難しかった");
					}
					
					if(dailyReport.getAboutInstructor() == 1) {
						model.addAttribute("aboutInstructor", "とても丁寧だった");
					}else if(dailyReport.getAboutInstructor() == 2) {
						model.addAttribute("aboutInstructor", "概ね丁寧だった");
					}else if(dailyReport.getAboutInstructor() == 3) {
						model.addAttribute("aboutInstructor", "どちらともいえない");
					}else if(dailyReport.getAboutInstructor() == 4) {
						model.addAttribute("aboutInstructor", "やや丁寧ではなかった");
					}else if(dailyReport.getAboutInstructor() == 5) {
						model.addAttribute("aboutInstructor", "全く丁寧ではなかった");
					}
				  model.addAttribute("dailyReport", dailyReport);
				  
				  //研修の開始日と終了日を取得する
				  //DateをLocalDateに変換
				  Date startDate = dailyReport.getTraining().getStartDate();
				  LocalDate start = ((java.sql.Date)startDate).toLocalDate();
				  Date endDate = dailyReport.getTraining().getEndDate();
				  LocalDate end = ((java.sql.Date)endDate).toLocalDate();
				  //１日ごとに表示させる.
				  List<String> dates = new ArrayList<>();
				  for(LocalDate startDay = start; startDay.isBefore(end); startDay = startDay.plusDays(1)) {
					  DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); //yyyy/MM/dd形式に変換.
					  String formatStartDate = datetimeformatter.format(startDay);
					  dates.add(formatStartDate);
				  }
				  //講義最終日のみ最後に追加.
				  DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); //yyyy/MM/dd形式に変換.
				  String formatEndDate = datetimeformatter.format(end);
				  dates.add(formatEndDate);
				  model.addAttribute("dates", dates);
				  
				//日報閲覧画面で生徒一覧のプルダウン.
				  Training training = trainingService.loadForAdmin(trainingId);
				  model.addAttribute("training", training);
				  return "company/company_view_daily_report";
	}
	
	/**
	 * 企業が受け持つ研修の講師の週報を閲覧する.
	 * @return
	 */
	@RequestMapping("/view_weekly_report")
	public String viewWeeklyReport(Integer id, Model model) {
		session.setAttribute("trainingId", id);
		
		WeeklyReport weeklyReport = weeklyReportService.load(id);
		model.addAttribute("weeklyReport", weeklyReport);
		
		Date date = weeklyReport.getStartDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate = dateFormat.format(date);
		model.addAttribute("formattedDate", formattedDate);
		
		//週報のプルダウンを表示させる
		Training training = weeklyReportService.loadForWeeklyReport(id);
		List<String> dates = new ArrayList<>();
		for(int i = 0; i < training.getWeeklyReportList().size(); i++) {
			String str = new SimpleDateFormat("yyyy/MM/dd").format(training.getWeeklyReportList().get(i).getStartDate());
			dates.add(str);
		}
		model.addAttribute("dates",dates);
				
		model.addAttribute("training", training);
		return "company/company_view_weekly_report";
	}
	
	/**
	 * 企業が受け持つ研修の講師の週報を日付で検索して閲覧するためのコントローラー.
	 * @param date
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/weekly_search")
	public String weeklySearch(String date, Model model) throws ParseException {
		Integer trainingId = (Integer) session.getAttribute("trainingId");
		//String型からDate型へ変換
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date changeDate = sdFormat.parse(date);
		WeeklyReport weeklyReport = weeklyReportService.loadByDate(changeDate, trainingId);
		model.addAttribute("weeklyReport", weeklyReport);
		
		Date dateStart = weeklyReport.getStartDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate = dateFormat.format(dateStart);
		model.addAttribute("formattedDate", formattedDate);
		
		//週報のプルダウンを表示させる
		Training training = weeklyReportService.loadForWeeklyReport(trainingId);
		List<String> dates = new ArrayList<>();
		for(int i = 0; i < training.getWeeklyReportList().size(); i++) {
			String str = new SimpleDateFormat("yyyy/MM/dd").format(training.getWeeklyReportList().get(i).getStartDate());
			dates.add(str);
		}
		model.addAttribute("dates",dates);
		return "company/company_view_weekly_report";
	}
	
	/**
	 * 日報を印刷するための初期画面.
	 * @param dailyReportId
	 * @param model
	 * @return
	 */
	@RequestMapping("/company_daily_report_print")
	public String companyDailyReportPrint(Integer dailyReportId, Model model) {
		DailyReport dailyReport = dailyReportService.printDailyReport(dailyReportId);

		//日付を年月日形式に変換
		Date date = dailyReport.getDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate = dateFormat.format(date);
		model.addAttribute("formattedDate", formattedDate);

		if(dailyReport.getIntelligibility() == 1) {
			model.addAttribute("intelligibility", "良く理解できた");
		}else if(dailyReport.getIntelligibility() == 2) {
			model.addAttribute("intelligibility", "概ね理解できた");
		}else if(dailyReport.getIntelligibility() == 3) {
			model.addAttribute("intelligibility", "ふつう");
		}else if(dailyReport.getIntelligibility() == 4) {
			model.addAttribute("intelligibility", "少し難しかった");
		}else if(dailyReport.getIntelligibility() == 5) {
			model.addAttribute("intelligibility", "とても難しかった");
		}

		if(dailyReport.getAboutInstructor() == 1) {
			model.addAttribute("aboutInstructor", "とても丁寧だった");
		}else if(dailyReport.getAboutInstructor() == 2) {
			model.addAttribute("aboutInstructor", "概ね丁寧だった");
		}else if(dailyReport.getAboutInstructor() == 3) {
			model.addAttribute("aboutInstructor", "どちらともいえない");
		}else if(dailyReport.getAboutInstructor() == 4) {
			model.addAttribute("aboutInstructor", "やや丁寧ではなかった");
		}else if(dailyReport.getAboutInstructor() == 5) {
			model.addAttribute("aboutInstructor", "全く丁寧ではなかった");
		}
		model.addAttribute("dailyReport", dailyReport);
		return "company/company_print_daily_report";
	}
	
	/**
	 * 週報を印刷するための初期画面.
	 * @param weeklyReportId
	 * @param model
	 * @return
	 */
	@RequestMapping("/company_weekly_report_print")
	public String companyWeeklyReportPrint(Integer weeklyReportId, Model model) {
		WeeklyReport weeklyReport = weeklyReportService.printWeeklyReport(weeklyReportId);

		//日付を年月日形式に変換
		Date date = weeklyReport.getStartDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate = dateFormat.format(date);
		model.addAttribute("formattedDate", formattedDate);

		model.addAttribute("weeklyReport", weeklyReport);
		return "company/company_print_weekly_report";
	}
	
	////////////////////////////////////////////////////////////////////////
	/**
	 * パスワード変更初期画面.
	 * @return
	 */
	@RequestMapping("/companyMember_forgot_password")
	public String forgotPassword() {
		return "company/company_forgotPassword";
	}

	/**
	 * パスワードを変更するためのリンク付きメールを送るためのコントローラー.
	 * @param request
	 * @param email
	 * @return
	 */
	@RequestMapping(value="/companyMember_resetPassword", method= RequestMethod.POST)
	//@ResponseBody
	public String resetPassword(HttpServletRequest request,@RequestParam("email") String email) {
		CompanyMember companyMember = companyMemberService.findByEmail(email); //入力されたメールアドレスからstudentを検索する.
		String token = UUID.randomUUID().toString(); //トークンを発行.
		companyMemberService.createPasswordResetTokenForStudent(companyMember, token); //studentとtokenをstudent_tokenテーブルにinsertする.
		mailSender.send(constructResetTokenEmail(request.getLocale(), token, companyMember)); //メールを送る.
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
	private SimpleMailMessage constructResetTokenEmail(Locale locale, String token, CompanyMember companyMember) {
		String url = "http://localhost:8080/company/companyMember_changePassword?id=" + companyMember.getId() + "&token=" + token; //トークンとstudentID付きのURL.
		String message = messages.getMessage("message.resetPassword", null, Locale.JAPANESE); //リンク付きURLと一緒に送るメッセージ(メッセージは別途、messages.propertiesに記載).
		return constructEmail("Reset Password", message + " \r\n" + url, companyMember);
	}

	/**
	 * メールを送るためのメソッド.
	 * @param subject
	 * @param body
	 * @param student
	 * @return
	 */
	private SimpleMailMessage constructEmail(String subject, String body, CompanyMember companyMember) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject); //タイトルの設定
		email.setText(body); //メールの本文
		email.setTo(companyMember.getEmail());
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
	@RequestMapping(value="/companyMember_changePassword", method= RequestMethod.GET)
	public String showChangePasswordPage(Locale locale, Model model, @RequestParam("id") long id, @RequestParam("token") String token) {
		String result = companyMemberSecurityService.validatePasswordResetToken(id, token);
		if(result != null) {
			model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
			return "redirect:/login?lang=" + locale.getLanguage();
		}
		return "company/company_updatePassword";
	}

	/**
	 * 新しいパスワードを保存するためのコントローラー.
	 * @param locale
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/companyMember_savePassword", method= RequestMethod.POST)
	//@ResponseBody
	public String savePassword(Locale locale, ChangePasswordForm form) {
		CompanyMember companyMember = (CompanyMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //生徒の情報を取得.
		companyMemberService.changeStudentPassword(companyMember,form.getNewPassword()); //新しいパスワードをインサートする.
		return "changePasswordComplete";
	}
	
}
