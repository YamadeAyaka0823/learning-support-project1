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

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.DailyReport;
import com.example.domain.Instructor;
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
	@RequestMapping("")
	public String index() {
		return "instructor/instructor_login";
	}
	
	/**
	 * 講師がログインするためのコントローラー.
	 * @param form
	 * @return
	 */
	@RequestMapping("/login")
	public String login(@Validated InstructorLoginForm form, BindingResult result, Model model) {
		Instructor instructor = instructorService.findByEmailAndPassword(form);
		if(instructor == null) {
			model.addAttribute("error", "メールアドレスかパスワードが間違っています");
			return index();
		}
		if(result.hasErrors()) {
			return index();
		}
		Instructor instructor2 = instructorService.load(instructor.getId());
		model.addAttribute("instructor", instructor2);
		return "instructor/instructor_training_list";
	}
	
	/**
	 * 週報登録初期画面.
	 * @return
	 */
	@RequestMapping("/weekly_report")
	public String weeklyReport(Integer id, Model model) { //trainingIdを取得
		//現在の日付取得
		LocalDate now = LocalDate.now();
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
		List<LocalDate> dates = new ArrayList<>();
		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(7)) {
			dates.add(startDate);
			model.addAttribute("dates", dates);
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
		return "redirect:/instructor/login";
	}
	
	/**
	 * 週報を閲覧するためのコントローラー.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/view")
	public String view(Integer id, Model model) {
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
	 * 講師が生徒の日報を閲覧するためのコントローラー.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/view_daily_report")
	public String viewDailyReport(Integer id, Model model) {
		DailyReport dailyReport = dailyReportService.instructorViewDailyReport(id);
		
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
		return "instructor/instructor_view_daily_report";
	}
}
