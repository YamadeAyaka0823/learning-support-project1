package com.example.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.DailyReport;
import com.example.domain.Student;
import com.example.domain.Training;
import com.example.form.DailyReportForm;
import com.example.form.StudentLoginForm;
import com.example.service.StudentService;
import com.example.service.TrainingService;

@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TrainingService trainingService;
	
	@Autowired
	private HttpSession session;
	
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
		session.setAttribute("id", student.getId());
		return "forward:/student/load";
	}
	
	/**
	 * 受講生の研修一覧のためのコントローラー.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/load")
	public String load(Model model) {
		Integer id = (Integer) session.getAttribute("id"); //sessionに入れたstudent_idで検索.
		Student student = studentService.load(id);
		model.addAttribute("student", student);
		return "student/student_training_list";
	}
	
	/**
	 * 日報登録初期画面.
	 * @return
	 */
	@RequestMapping("/daily")
	public String daily(Integer id, Integer studentId, Model model) {
		Date date=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate=dateFormat.format(date);
		model.addAttribute("formattedDate", formattedDate);
		model.addAttribute("id", id); //トレーニングのID
		return "student/student_register_daily_report";
	}
	
	/**
	 * 受講生の日報をインサートするコントローラー.
	 * @param form
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/insert")
	public String insert(DailyReportForm form, Model model, RedirectAttributes redirectAttributes) throws ParseException {
		studentService.insert(form);
		return "redirect:/student/load";
	}
	
	/**
	 * 受講生の日報の閲覧のためのコントローラー.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/daily_load")
	public String dailyLoad(Integer id, Model model) {
		DailyReport dailyReport = studentService.dailyLoad(id);
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
		Training training = trainingService.instructorIdLoad(id);
		//DateをLocalDateに変換
		Date date2 = training.getStartDate();
		LocalDate start = ((java.sql.Date)date2).toLocalDate();
		Date date3 = training.getEndDate();
		LocalDate end = ((java.sql.Date)date3).toLocalDate();
		//１週間ごとに表示させる.
		List<LocalDate> dates = new ArrayList<>();
		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(1)) {
			dates.add(startDate);
		}	
		model.addAttribute("dates", dates);
		return "student/student_view_daily_report";
	}
	
	/**
	 * 日報を1件検索するためのコントローラー.
	 * @param date
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/date_load")
	public String dateLoad(Integer trainingId,String date, Model model) throws ParseException {
		 SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
         Date date2 = sdFormat.parse(date);
		DailyReport dailyReport = studentService.dateLoad(trainingId,date2);
		
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
//		  Training training = trainingService.instructorIdLoad(id);
		  //DateをLocalDateに変換
		  Date date4 = dailyReport.getTraining().getStartDate();
		  LocalDate start = ((java.sql.Date)date4).toLocalDate();
		  Date date5 = dailyReport.getTraining().getEndDate();
		  LocalDate end = ((java.sql.Date)date5).toLocalDate();
		  //１週間ごとに表示させる.
		  List<LocalDate> dates = new ArrayList<>();
		  for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(1)) {
			  dates.add(startDate);
		  }	
		  model.addAttribute("dates", dates);
		  return "student/student_view_daily_report";
	}
	

}
