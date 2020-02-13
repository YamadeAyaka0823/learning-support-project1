package com.example.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
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

import com.example.domain.DailyReport;
import com.example.domain.Instructor;
import com.example.domain.LoginInstructor;
import com.example.domain.Training;
import com.example.domain.WeeklyReport;
import com.example.form.InstructorLoginForm;
import com.example.form.StudentImpressionForm;
import com.example.form.WeeklyReportForm;
import com.example.service.DailyReportService;
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
    private HttpSession session;
	
	@ModelAttribute
	public InstructorLoginForm setUpForm() {
		return new InstructorLoginForm();
	}
	
	/**
	 * 講師のログイン初期画面.
	 * @return
	 */
//	@RequestMapping("")
//	public String index() {
//		return "instructor/instructor_login";
//	}
	
	/**
	 * 講師がログインするためのコントローラー.
	 * @param form
	 * @return
	 */
//	@RequestMapping("/instructor_login")
//	public String login(@Validated InstructorLoginForm form, BindingResult result, Model model) {
//		Instructor instructor = instructorService.findByEmailAndPassword(form);
//		if(instructor == null) {
//			model.addAttribute("error", "メールアドレスかパスワードが間違っています");
//			return index();
//		}
//		if(result.hasErrors()) {
//			return index();
//		}
//		session.setAttribute("id", instructor.getId());
//		return "forward:/instructor/load";
//	}
	
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
		Integer id = (Integer) session.getAttribute("id");
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
//		//週の始めの日を取得
//		LocalDate monday = now.with(DayOfWeek.MONDAY);
//		//年月日形式に変換
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
//		String date = dtf.format(monday);
//		model.addAttribute("date", date);
		
//		WeeklyReport weeklyReport = weeklyReportService.maxStartDate(startDate);
		
		Training training = trainingService.load(id);
		Date date2 = training.getStartDate();
		LocalDate start = ((java.sql.Date)date2).toLocalDate();
		Date date3 = training.getEndDate();
		LocalDate end = ((java.sql.Date)date3).toLocalDate();
//		List<LocalDate> dates = new ArrayList<>();
		//研修中の毎週月曜日を表示させる.
		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(7)) {
			//現在の日付取得
			LocalDate now = LocalDate.now();
			LocalDate monday = now.with(DayOfWeek.MONDAY);
//			dates.add(monday);
			model.addAttribute("monday", monday);
	    }
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
	public String insert(WeeklyReportForm form, StudentImpressionForm form2) throws ParseException {
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
		List<LocalDate> dates = new ArrayList<>();
		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(7)) {
			dates.add(startDate);
	    }
		model.addAttribute("dates", dates);


		model.addAttribute("training", training);
		return "instructor/instructor_view_weekly_report";
	}
	
	/**
	 * 週報を日付で1件検索するためのコントローラー.
	 * @param weeklyReportId
	 * @param date
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/weekly_report_search")
	public String weeklyReportSearch(String date, Model model) throws ParseException {
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
		  List<LocalDate> dates = new ArrayList<>();
		  for(LocalDate startDay = start; startDay.isBefore(end); startDay = startDay.plusDays(1)) {
			  dates.add(startDay);
		  }
		  //研修の最終日を追加.
		  dates.add(end);
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
		return "instructor/instructor_view_daily_report";
	}
}
