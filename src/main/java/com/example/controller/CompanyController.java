package com.example.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.CompanyMember;
import com.example.domain.DailyReport;
import com.example.domain.LoginCompanyMember;
import com.example.domain.Training;
import com.example.domain.WeeklyReport;
import com.example.form.CompanyMemberLoginForm;
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
	private HttpSession session;
	
	@ModelAttribute
	public CompanyMemberLoginForm setUpForm() {
		return new CompanyMemberLoginForm();
	}
	
	/**
	 * 企業のログイン初期画面.
	 * @return
	 */
//	@RequestMapping("")
//	public String index() {
//		return "company/company_login";
//	}
	
	/**
	 * ログインするためのコントローラー.
	 * @param form
	 * @return
	 */
//	@RequestMapping("/company_login")
//	public String login(@Validated CompanyMemberLoginForm form, BindingResult result, Model model) {
//		CompanyMember companyMember = companyMemberService.findByEmailAndPassword(form);
//		if(companyMember == null) {
//			model.addAttribute("error", "メールアドレスかパスワードが間違っています");
//			return index();
//		}
//		if(result.hasErrors()) {
//			return index();
//		}
//		session.setAttribute("company_id", companyMember.getCompanyId());
//		return "forward:/company/list";
//	}
	
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
		Integer id = (Integer) session.getAttribute("company_id"); //sessionに入れたcompany_idで検索.
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
		  List<LocalDate> dates = new ArrayList<>();
		  for(LocalDate startDay = start; startDay.isBefore(end); startDay = startDay.plusDays(1)) {
			  dates.add(startDay);
		  }	
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
        DailyReport dailyReport = dailyReportService.dateAndNameLoad(trainingId, date, name);
		
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
				  List<LocalDate> dates = new ArrayList<>();
				  for(LocalDate startDay = start; startDay.isBefore(end); startDay = startDay.plusDays(1)) {
					  dates.add(startDay);
				  }
				  //講義最終日のみ最後に追加.
				  dates.add(end);
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
		WeeklyReport weeklyReport = weeklyReportService.load(id);
		model.addAttribute("weeklyReport", weeklyReport);
		
		Date date = weeklyReport.getStartDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate = dateFormat.format(date);
		model.addAttribute("formattedDate", formattedDate);
		
		//日付を１週間おきに表示させる.
		Training training = trainingService.load(id);
		Date startDate = training.getStartDate();
		LocalDate start = ((java.sql.Date)startDate).toLocalDate();
		Date endDate = training.getEndDate();
		LocalDate end = ((java.sql.Date)endDate).toLocalDate();
		List<LocalDate> dates = new ArrayList<>();
		for(LocalDate startDay = start; startDay.isBefore(end); startDay = startDay.plusDays(7)) {
			dates.add(startDay);
			model.addAttribute("dates", dates);
	    }
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
		WeeklyReport weeklyReport = weeklyReportService.loadByDate(date);
		model.addAttribute("weeklyReport", weeklyReport);
		
		Date dateStart = weeklyReport.getStartDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate = dateFormat.format(dateStart);
		model.addAttribute("formattedDate", formattedDate);
		
		Integer trainingId = (Integer) session.getAttribute("trainingId");
		//研修の開始日と終了日を取得する
		Training training = trainingService.load(trainingId);
		//DateをLocalDateに変換
		Date date2 = training.getStartDate();
		LocalDate start = ((java.sql.Date)date2).toLocalDate();
		Date date3 = training.getEndDate();
		LocalDate end = ((java.sql.Date)date3).toLocalDate();
		//１週間ごとに表示させる.
		List<LocalDate> dates = new ArrayList<>();
		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(7)) {
			dates.add(startDate);
			  }
		model.addAttribute("dates", dates);
		return "company/company_view_weekly_report";
	}
	
	
}
