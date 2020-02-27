package com.example.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.DailyReport;
import com.example.domain.Instructor;
import com.example.domain.LoginInstructor;
import com.example.domain.Training;
import com.example.domain.WeeklyReport;
import com.example.form.ChangePasswordForm;
import com.example.form.InstructorLoginForm;
import com.example.form.StudentImpressionForm;
import com.example.form.WeeklyReportForm;
import com.example.form.WeeklyReportUpdateForm;
import com.example.service.DailyReportService;
import com.example.service.InstructorSecurityService;
import com.example.service.InstructorService;
import com.example.service.TrainingService;
import com.example.service.WeeklyReportService;

@Controller
@RequestMapping("/instructor")
public class InstructorController {
	
	@Autowired
	private InstructorService instructorService;
	
	@Autowired
	private TrainingService trainingService;
	
	@Autowired
	private WeeklyReportService weeklyReportService;
	
	@Autowired
	private DailyReportService dailyReportService;
	
	@Autowired
	private InstructorSecurityService instructorSecurityService;
	
	@Autowired
    private HttpSession session;
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
    private MessageSource messages;
	
	@ModelAttribute
	public InstructorLoginForm setUpForm() {
		return new InstructorLoginForm();
	}
	
	@ModelAttribute
	public WeeklyReportForm setUpForm2() {
		return new WeeklyReportForm();
	}
	
	@ModelAttribute
	public StudentImpressionForm setUpForm3() {
		return new StudentImpressionForm();
	}
	
	/**
	 * 講師のログイン画面.
	 * @return
	 */
	@RequestMapping("/instructor_login")
	public String login(Model model,@RequestParam(required = false) String error) {
		
		if (error != null) {
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");

			
		}
		return "instructor/instructor_login";
	}
	
	/**
	 * 講師が担当している研修一覧のためのコントローラー.
	 * @param model
	 * @return
	 */
	@RequestMapping("/load")
	public String load(Model model, @AuthenticationPrincipal LoginInstructor loginInstructor) {
		Integer id = loginInstructor.getInstructor().getId();
		Instructor instructor = instructorService.load(id);
		model.addAttribute("instructor", instructor);
		return "instructor/instructor_training_list";
	}
	
	/**
	 * 週報登録初期画面.
	 * @return
	 */
	@RequestMapping("/weekly_report")
	public String weeklyReport(Integer id, Model model) { //trainingIdを取得
		session.setAttribute("trainingId", id); //training_idをsessionにいれる
		Training training = trainingService.load(id);
		Date date2 = training.getStartDate();
		LocalDate start = ((java.sql.Date)date2).toLocalDate();
		Date date3 = training.getEndDate();
		LocalDate end = ((java.sql.Date)date3).toLocalDate();
		//研修中の毎週月曜日を表示させる.
		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(7)) {
			LocalDate firstDate = startDate.with(DayOfWeek.MONDAY);
			Date changeDate = Date.from(firstDate.atStartOfDay(ZoneId.systemDefault()).toInstant()); //localDateをDateへ変換
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日"); //年月日形式に変換
			String formattedDate = dateFormat.format(changeDate);
			model.addAttribute("formattedDate", formattedDate);
	    }
		
		//１週間ごとに表示させる(プルダウン).
		List<String> dates = new ArrayList<>();
		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(7)) {
			LocalDate monday = startDate.with(DayOfWeek.MONDAY);
			DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); //yyyy/MM/dd形式に変換.
		    String formatStartDate = datetimeformatter.format(monday);
			dates.add(formatStartDate);
		}
		model.addAttribute("dates", dates);
		model.addAttribute("training", training);
		return "instructor/instructor_register_weekly_report";
	}
	
	/**
	 * 週報を日付で1件検索するためのコントローラー(週報登録画面で).
	 * @param weeklyReportId
	 * @param date
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/weekly_report_load")
	public String weeklyReportLoad(String date, Model model) throws ParseException {
		Integer trainingId = (Integer) session.getAttribute("trainingId");
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date changeDate = sdFormat.parse(date);
		WeeklyReport weeklyReport = weeklyReportService.loadByDate(changeDate, trainingId);
		model.addAttribute("weeklyReport", weeklyReport);
		
		Date dateStart = weeklyReport.getStartDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate = dateFormat.format(dateStart);
		model.addAttribute("formattedDate", formattedDate);
		
//		Integer trainingId = (Integer) session.getAttribute("trainingId");
		//研修の開始日と終了日を取得する
		Training training = trainingService.load(trainingId);
		//DateをLocalDateに変換
		Date date2 = training.getStartDate();
		LocalDate start = ((java.sql.Date)date2).toLocalDate();
		Date date3 = training.getEndDate();
		LocalDate end = ((java.sql.Date)date3).toLocalDate();
		//１週間ごとに表示させる.
		List<String> dates = new ArrayList<>();
		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(7)) {
			DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); //yyyy/MM/dd形式に変換.
		    String formatStartDate = datetimeformatter.format(startDate);
			dates.add(formatStartDate);
			  }
		model.addAttribute("dates", dates);
		model.addAttribute("training", training);
		return "instructor/instructor_register_weekly_report";
	}
	
	/**
	 * 週報を登録するためのコントローラー.
	 * @param form
	 * @param form2
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/insert")
	public String insert(WeeklyReportForm form, StudentImpressionForm form2, BindingResult result, Model model) throws ParseException {
//		if(result.hasErrors()) {
//			return weeklyReport(form.getTrainingId(), model);
//		}
		weeklyReportService.insert(form, form2);
		return "redirect:/instructor/load";
	}
	
	/**
	 * 週報を閲覧するためのコントローラー.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/view")
	public String view(Integer id, Model model) {
		//研修IDをセッションにいれる.
		session.setAttribute("trainingId", id);
		
		WeeklyReport weeklyReport = weeklyReportService.load(id);
		model.addAttribute("weeklyReport", weeklyReport);
		
		Date date = weeklyReport.getStartDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate = dateFormat.format(date);
		model.addAttribute("formattedDate", formattedDate);
		
		//研修の開始日と終了日を取得する
		Training training = trainingService.load(id);
		//DateをLocalDateに変換
		Date date2 = training.getStartDate();
		LocalDate start = ((java.sql.Date)date2).toLocalDate();
		Date date3 = training.getEndDate();
		LocalDate end = ((java.sql.Date)date3).toLocalDate();
		//１週間ごとに表示させる.
		List<String> dates = new ArrayList<>();
		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(7)) {
			DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); //yyyy/MM/dd形式に変換.
		    String formatStartDate = datetimeformatter.format(startDate);
			dates.add(formatStartDate);
	    }
		model.addAttribute("dates", dates);


		model.addAttribute("training", training);
		return "instructor/instructor_view_weekly_report";
	}
	
	/**
	 * 週報を日付で1件検索するためのコントローラー(週報閲覧画面で).
	 * @param weeklyReportId
	 * @param date
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/weekly_report_search")
	public String weeklyReportSearch(String date, Model model) throws ParseException {
		Integer trainingId = (Integer) session.getAttribute("trainingId");
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date changeDate = sdFormat.parse(date);
		WeeklyReport weeklyReport = weeklyReportService.loadByDate(changeDate, trainingId);
		model.addAttribute("weeklyReport", weeklyReport);
		
		Date dateStart = weeklyReport.getStartDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate = dateFormat.format(dateStart);
		model.addAttribute("formattedDate", formattedDate);
		
//		Integer trainingId = (Integer) session.getAttribute("trainingId");
		//研修の開始日と終了日を取得する
		Training training = trainingService.load(trainingId);
		//DateをLocalDateに変換
		Date date2 = training.getStartDate();
		LocalDate start = ((java.sql.Date)date2).toLocalDate();
		Date date3 = training.getEndDate();
		LocalDate end = ((java.sql.Date)date3).toLocalDate();
		//１週間ごとに表示させる.
		List<String> dates = new ArrayList<>();
		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(7)) {
			DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); //yyyy/MM/dd形式に変換.
		    String formatStartDate = datetimeformatter.format(startDate);
			dates.add(formatStartDate);
			  }
		model.addAttribute("dates", dates);
		return "instructor/instructor_view_weekly_report";
	}
	
	/**
	 * 講師が生徒の日報を閲覧するためのコントローラー.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/view_daily_report")
	public String viewDailyReport(Integer id, Model model) {
		DailyReport dailyReport = dailyReportService.instructorViewDailyReport(id);
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
		  //研修の最終日を追加.
		  DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); //yyyy/MM/dd形式に変換.
		  String formatEndDate = datetimeformatter.format(end);
		  dates.add(formatEndDate);
		  model.addAttribute("dates", dates);
		  //日報閲覧画面で生徒一覧のプルダウン.
		  Training training = trainingService.loadForAdmin(id);
		  model.addAttribute("training", training);
		return "instructor/instructor_view_daily_report";
	}
	
	/**
	 * 講師が受講生の日報を日付と名前で検索して閲覧するためのコントローラー.
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
//				  Training training = trainingService.instructorIdLoad(id);
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
		return "instructor/instructor_view_daily_report";
	}
	
	/**
	 * 日報を印刷するための初期画面.
	 * @return
	 */
	@RequestMapping("/instructor_daily_report_print")
	public String instructorDailyReportPrint(Integer dailyReportId, Model model) {
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
		return "instructor/instructor_print_daily_report";
	}
	
	/**
	 * 週報を印刷するための初期画面.
	 * @param weeklyReportId
	 * @param model
	 * @return
	 */
	@RequestMapping("/instructor_weekly_report_print")
	public String instructorWeeklyReportPrint(Integer weeklyReportId, Model model) {
		WeeklyReport weeklyReport = weeklyReportService.printWeeklyReport(weeklyReportId);
		
		//日付を年月日形式に変換
		Date date = weeklyReport.getStartDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate = dateFormat.format(date);
		model.addAttribute("formattedDate", formattedDate);
		
		model.addAttribute("weeklyReport", weeklyReport);
		return "instructor/instructor_print_weekly_report";
	}
	
//	/**
//	 * 週報編集の初期画面.
//	 * @return
//	 */
//	@RequestMapping("/weekly_report_edit")
//	public String weeklyReportEdit(Integer id, Model model) {
//		//研修IDをセッションにいれる.
//		session.setAttribute("trainingId", id);
//				
//		WeeklyReport weeklyReport = weeklyReportService.load(id);
//		model.addAttribute("weeklyReport", weeklyReport);
//		
//		Date date = weeklyReport.getStartDate();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
//		String formattedDate = dateFormat.format(date);
//		model.addAttribute("formattedDate", formattedDate);
//
//		//研修の開始日と終了日を取得する
//		Training training = trainingService.load(id);
//		//DateをLocalDateに変換
//		Date date2 = training.getStartDate();
//		LocalDate start = ((java.sql.Date)date2).toLocalDate();
//		Date date3 = training.getEndDate();
//		LocalDate end = ((java.sql.Date)date3).toLocalDate();
//		//１週間ごとに表示させる.
//		List<LocalDate> dates = new ArrayList<>();
//		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(7)) {
//			dates.add(startDate);
//		}
//		model.addAttribute("dates", dates);
//
//
//		model.addAttribute("training", training);
//		return "instructor/instructor_edit_weekly_report";
//	}
	
//	/**
//	 * 週報を変更するために日付で1件検索する.
//	 * @param trainingId
//	 * @param date
//	 * @param name
//	 * @param model
//	 * @return
//	 * @throws ParseException
//	 */
//	@RequestMapping("/weekly_report_edit_search")
//	public String weeklyReportEditSearch(String date, Integer trainingId, Model model) throws ParseException {
//		WeeklyReport weeklyReport = weeklyReportService.loadByDate(date, trainingId);
//		model.addAttribute("weeklyReport", weeklyReport);
//		
//		Date dateStart = weeklyReport.getStartDate();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
//		String formattedDate = dateFormat.format(dateStart);
//		model.addAttribute("formattedDate", formattedDate);
//		
////		Integer trainingId = (Integer) session.getAttribute("trainingId");
//		//研修の開始日と終了日を取得する
//		Training training = trainingService.load(trainingId);
//		//DateをLocalDateに変換
//		Date date2 = training.getStartDate();
//		LocalDate start = ((java.sql.Date)date2).toLocalDate();
//		Date date3 = training.getEndDate();
//		LocalDate end = ((java.sql.Date)date3).toLocalDate();
//		//１週間ごとに表示させる.
//		List<LocalDate> dates = new ArrayList<>();
//		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(7)) {
//			dates.add(startDate);
//			  }
//		model.addAttribute("dates", dates);
//		return "instructor/instructor_edit_weekly_report";
//	}
	
//	/**
//	 * 週報を編集する.
//	 * @param weeklyReportForm
//	 * @param studentImpressionForm
//	 * @return
//	 */
//	@RequestMapping("/update")
//	public String update(WeeklyReportUpdateForm weeklyReportForm) {
//		weeklyReportService.update(weeklyReportForm);
//		return "redirect:/instructor/load";
//	}
	
	////////////////////////////////////////////////////////////////////////
	/**
	 * パスワード変更初期画面.
	 * @return
	 */
	@RequestMapping("/instructor_forgot_password")
	public String forgotPassword() {
		return "instructor/instructor_forgotPassword";
	}
	
	/**
	 * パスワードを変更するためのリンク付きメールを送るためのコントローラー.
	 * @param request
	 * @param email
	 * @return
	 */
	@RequestMapping(value="/instructor_resetPassword", method= RequestMethod.POST)
//	@ResponseBody
	public String resetPassword(HttpServletRequest request,@RequestParam("email") String email) {
		Instructor instructor = instructorService.findByEmail(email); //入力されたメールアドレスからstudentを検索する.
		String token = UUID.randomUUID().toString(); //トークンを発行.
		instructorService.createPasswordResetTokenForInstructor(instructor, token); //studentとtokenをstudent_tokenテーブルにinsertする.
		mailSender.send(constructResetTokenEmail(request.getLocale(), token, instructor)); //メールを送る.
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
	private SimpleMailMessage constructResetTokenEmail(Locale locale, String token, Instructor instructor) {
		String url = "http://localhost:8080/instructor/instructor_changePassword?id=" + instructor.getId() + "&token=" + token; //トークンとstudentID付きのURL.
		String message = messages.getMessage("message.resetPassword", null, Locale.JAPANESE); //リンク付きURLと一緒に送るメッセージ(メッセージは別途、messages.propertiesに記載).
		return constructEmail("Reset Password", message + " \r\n" + url, instructor);
	}
	
	/**
	 * メールを送るためのメソッド.
	 * @param subject
	 * @param body
	 * @param student
	 * @return
	 */
	private SimpleMailMessage constructEmail(String subject, String body, Instructor instructor) {
		SimpleMailMessage email = new SimpleMailMessage();
	    email.setSubject(subject); //タイトルの設定
	    email.setText(body); //メールの本文
	    email.setTo(instructor.getEmail());
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
	@RequestMapping(value="/instructor_changePassword", method= RequestMethod.GET)
	public String showChangePasswordPage(Locale locale, Model model, @RequestParam("id") long id, @RequestParam("token") String token) {
		String result = instructorSecurityService.validatePasswordResetToken(id, token);
		if(result != null) {
			model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
			return "redirect:/login?lang=" + locale.getLanguage();
		}
		return "instructor/instructor_updatePassword";
	}
	
	/**
	 * 新しいパスワードを保存するためのコントローラー.
	 * @param locale
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/instructor_savePassword", method= RequestMethod.POST)
//	@ResponseBody
	public String savePassword(Locale locale, ChangePasswordForm form) {
		Instructor instructor = (Instructor) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //生徒の情報を取得.
		instructorService.changeStudentPassword(instructor,form.getNewPassword()); //新しいパスワードをインサートする.
		return "changePasswordComplete";
	}
}
