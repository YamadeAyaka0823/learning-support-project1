package com.example.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.DailyReport;
import com.example.domain.GenericResponse;
import com.example.domain.LoginStudent;
import com.example.domain.Student;
import com.example.domain.Training;
import com.example.form.DailyReportForm;
import com.example.form.DailyReportUpdateForm;
import com.example.form.StudentLoginForm;
import com.example.service.DailyReportService;
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
	private DailyReportService dailyReportService;
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
    private MessageSource messages;
	
	@ModelAttribute
	public StudentLoginForm setUpForm() {
		return new StudentLoginForm();
	}
	
	@ModelAttribute
	public DailyReportForm setUpForm2() {
		return new DailyReportForm();
	}
	

	/**
	 * 受講生のログイン.
	 * @param model
	 * @param error
	 * @return
	 */
	@RequestMapping("/student_login")
	public String login(Model model,@RequestParam(required = false) String error) {
		
		if (error != null) {
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");

			
		}
		return "student/student_login";
	}
	
	/**
	 * 受講生の研修一覧のためのコントローラー.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/student_load")
	public String load(Model model, @AuthenticationPrincipal LoginStudent loginStudent) {
		Integer id = loginStudent.getStudent().getId(); //student_idで検索.
		Student student = studentService.load(id);
		model.addAttribute("student", student);
//		//研修の開始日と終了日をyyyy/mm/dd形式に変換.
//		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
//		String startDate= dateFormat.format(student.getTrainingList().get(0).getStartDate());
//		model.addAttribute("startDate", startDate);
//		String endDate= dateFormat.format(student.getTrainingList().get(0).getEndDate());
//		model.addAttribute("endDate", endDate);
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
		model.addAttribute("studentId", studentId); //受講生のID
		return "student/student_register_daily_report";
	}
	
	/**
	 * 受講生の日報をインサートするコントローラー.
	 * @param form
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/insert")
	public String insert(@Validated DailyReportForm form, BindingResult result, Model model, RedirectAttributes redirectAttributes) throws ParseException {
		if(result.hasErrors()) {
			return daily(form.getTrainingId(), form.getStudentId(), model);
		}
		studentService.insert(form);
		return "redirect:/student/student_load";
	}
	
	/**
	 * 受講生の日報の閲覧のためのコントローラー.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/daily_load")
	public String dailyLoad(Integer id, Integer studentId, Model model) {
		DailyReport dailyReport = studentService.dailyLoad(id, studentId);
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
		//講義最終日のみ最後に追加.
		dates.add(end);
		model.addAttribute("dates", dates);
		return "student/student_view_daily_report";
	}
	
	/**
	 * 日報を日付で1件検索するためのコントローラー.
	 * @param date
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/date_load")
	public String dateLoad(Integer trainingId, Integer studentId, String date, Model model) throws ParseException {
		 SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
         Date date2 = sdFormat.parse(date);
		DailyReport dailyReport = studentService.dateLoad(trainingId,studentId,date2);
		
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
		  return "student/student_view_daily_report";
	}
	
	/**
	 * 日報を編集する初期画面.
	 * @return
	 */
	@RequestMapping("/daily_edit")
	public String dailyEdit(Integer id, Integer studentId, Model model) {
		DailyReport dailyReport = studentService.dailyLoad(id, studentId);
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
		//講義最終日のみ最後に追加.
		dates.add(end);
		model.addAttribute("dates", dates);
		return "student/student_edit_daily_report";
	}
	
	/**
	 * 日報編集画面で日付で1件検索するためのコントローラー.
	 * @param trainingId
	 * @param date
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/edit_date_load")
	public String editDateLoad(Integer trainingId, Integer studentId, String date, Model model) throws ParseException {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = sdFormat.parse(date);
		DailyReport dailyReport = studentService.dateLoad(trainingId,studentId,date2);
		
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
		return "student/student_edit_daily_report";
	}
	
	/**
	 * 日報を更新する.
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/update")
	public String update(DailyReportUpdateForm form) throws ParseException {
		dailyReportService.update(form);
		return "redirect:/student/student_load";
	}

	////////////////////////////////////////////////////////////////////////
	@RequestMapping("/forgot_password")
	public String forgotPassword() {
		return "/student/forgotPassword";
	}
	
	@RequestMapping(value="/resetPassword", method= RequestMethod.POST)
	@ResponseBody
	public GenericResponse resetPassword(HttpServletRequest request,@RequestParam("email") String email) {
		Student student = studentService.findByEmail(email);
		String token = UUID.randomUUID().toString();
//		studentService.createPasswordResetTokenForStudent(student, token);
		mailSender.send(constructResetTokenEmail(request.getContextPath(), request.getLocale(), token, student));
		return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
	}
	
	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, Student student) {
		String url = contextPath + "/student/changePassword?id=" + student.getId() + "&token=" + token;
		String message = messages.getMessage("message.resetPassword", null, Locale.JAPANESE);
		return constructEmail("Reset Password", message + " \r\n" + url, student);
	}
	
	private SimpleMailMessage constructEmail(String subject, String body, Student student) {
		SimpleMailMessage email = new SimpleMailMessage();
	    email.setSubject(subject); //タイトルの設定
	    email.setText(body); //メールの本文
	    email.setTo(student.getEmail());
	    return email;
	}
	
//	@RequestMapping("/resetPassword")
//	public String sendEmail(String email) {
//		SimpleMailMessage mail = new SimpleMailMessage();
//		mail.setSubject("ヤッホー");
//		mail.setText("わーい");
//		mail.setTo(email);
//		mailSender.send(mail);
//		return "success";
//	}
	
	

}
