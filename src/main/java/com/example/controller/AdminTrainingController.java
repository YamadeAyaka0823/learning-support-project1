package com.example.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.DailyReport;
import com.example.domain.Instructor;
import com.example.domain.LoginAdmin;
import com.example.domain.Student;
import com.example.domain.StudentImpression;
import com.example.domain.Training;
import com.example.domain.TrainingStudent;
import com.example.domain.WeeklyReport;
import com.example.form.StudentRegisterForm;
import com.example.form.StudentUpdateForm;
import com.example.form.TrainingRegisterForm;
import com.example.form.TrainingUpdateForm;
import com.example.service.DailyReportService;
import com.example.service.InstructorService;
import com.example.service.StudentImpressionService;
import com.example.service.StudentService;
import com.example.service.TrainingService;
import com.example.service.TrainingStudentService;
import com.example.service.WeeklyReportService;

@Controller
@RequestMapping("/admin")
public class AdminTrainingController {
	
	@Autowired
	private TrainingService trainingService;
	
	@Autowired
	private InstructorService instructorService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TrainingStudentService trainingStudentService;
	
	@Autowired
	private WeeklyReportService weeklyReportService;
	
	@Autowired
	private DailyReportService dailyReportService;
	
	@Autowired
	private StudentImpressionService studentImpressionService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private HttpSession session;
	
	@ModelAttribute
	public TrainingRegisterForm setUpForm() {
		return new TrainingRegisterForm();
	}
	
	/**
	 * 管理者ログイン初期画面.
	 * @return
	 */
	@RequestMapping("/admin_login")
	public String login(Model model,@RequestParam(required = false) String error) {
		
		if (error != null) {
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");

			
		}
		return "admin/admin_login";
	}
	
	
	/**
	 * 管理者画面で研修一覧画面.
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/training_list")
	public String trainingList(String name, String startDate, Model model, @AuthenticationPrincipal LoginAdmin loginAdmin) throws ParseException {
		//ログインしている管理者のIDを取得
		Integer adminId = loginAdmin.getAdmin().getId();
		model.addAttribute("adminId", adminId);
		
		List<Training> trainingList = null;
		if(name == null && startDate == null) {
			trainingList = trainingService.findAll();//研修名と日付が選択されてなければ全件検索.
		}else if(name == null){
			trainingList = trainingService.findByDate(startDate);//日付だけ入っていた場合
		}else if(startDate == null) {
			trainingList = trainingService.findByName(name);//名前だけ入っていた場合
		}else {
			trainingList = trainingService.findByNameOrDate(name, startDate);//研修名か日付が選択されていれば曖昧検索.
		}
		model.addAttribute("trainingList", trainingList);
		return "admin/admin_training_list";
	}
	
	/**
	 * 管理者画面で研修を新規登録する初期画面.
	 * @return
	 */
	@RequestMapping("/t_register")
	public String register(Model model) {
		List<Instructor> instructorList = instructorService.findAll();
		model.addAttribute("instructorList", instructorList);
		return "admin/admin_training_detail";
	}
	
	/**
	 * 管理者画面で研修を新規登録するためのコントローラー.
	 * @param form
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/training_register")
	public String trainingRegister(@Validated TrainingRegisterForm form, BindingResult result, Model model) throws ParseException {
		if(result.hasErrors()) {
			return register(model);
		}
		trainingService.insert(form);
		return "redirect:/admin/training_list";
	}
	
	/**
	 * 管理者画面で研修を編集する初期画面.
	 * @return
	 */
	@RequestMapping("/t_edit")
	public String edit(Integer id, Model model) {
		Training training = trainingService.oneLoad(id); //元から情報を入れとくための1件検索
		model.addAttribute("training", training);
		List<Instructor> instructorList = instructorService.findAll(); //セレクトボックスに講師名を入れとくため.
		model.addAttribute("instructorList", instructorList);
		List<Training> trainingList = trainingService.selectStudent(id);
		model.addAttribute("trainingList", trainingList);
		return "admin/admin_training_edit";
	}
	
	/**
	 * 管理者画面で研修を編集する.
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/training_edit")
	public String trainingEdit(TrainingUpdateForm form) throws ParseException {
		trainingService.update(form);
		return "redirect:/admin/training_list";
	}
	
	/**
	 * 管理者画面で生徒を登録する初期画面.
	 * @return
	 */
	@RequestMapping("/student_register")
	public String studentRegister(Integer id, Model model) {
		Training training = trainingService.loadJoin2Table(id);
		model.addAttribute("training", training);
		return "admin/admin_training_import_students";
	}
	
	@RequestMapping("/student_import")
	public String studentImport(StudentRegisterForm form, Model model, Integer id, TrainingStudent trainingStudent) throws IOException {
		
		InputStream stream = form.getCsv().getInputStream(); //ファイルからバイトストリームを作る
		Reader reader = new InputStreamReader(stream); //バイトストリームから文字列ストリームを作る
		BufferedReader br = new BufferedReader(reader); //リーダーをバッファする
		String str;
		List<Student> studentList = new ArrayList<>();
		 while((str = br.readLine()) != null){
			 String[] results = str.split(",");
			 
			 Student student = new Student();
			 student.setName(results[0]);
			 student.setKana(results[1]);
			 student.setEmail(results[2]);
			 student.setPassword(results[3]);
			 student.setCompanyId(Integer.parseInt(results[4]));
			 studentList.add(student);
			  }
		 session.setAttribute("studentList", studentList);
		return studentRegister(id, model);
	}
	
	/**
	 * 管理者画面で生徒を新規登録する.
	 * @return
	 */
	@RequestMapping("/student_insert")
	public String studentInsert(Integer id, TrainingStudent trainingStudent) {
	    List<Student> studentList = (ArrayList)session.getAttribute("studentList");
	    for(int i = 0; i < studentList.size(); i++) {
	    	Student studentSearch = studentService.findByEmail(studentList.get(i).getEmail());
	    	if(studentSearch == null) { //emailはunique制約があるため、もしDBにすでにemailが登録されていればstudentテーブルにはinsertしない処理.
	    		Student student = new Student();
	    		student.setName(studentList.get(i).getName());
	    		student.setKana(studentList.get(i).getKana());
	    		student.setEmail(studentList.get(i).getEmail());
	    		// パスワードをハッシュ化する
	    		String encodePassword = passwordEncoder.encode(studentList.get(i).getPassword());
	    		student.setPassword(encodePassword);
	    		student.setCompanyId(studentList.get(i).getCompanyId());
	    		//studentsテーブルに受講生をinsert
	    		Student studentInsert = studentService.insert(student);
	    		trainingStudent.setStudentId(studentInsert.getId());
	    		trainingStudent.setTrainingId(id);
	    		//training_studentテーブルにinsert
	    		trainingStudentService.insert(trainingStudent);
	    		
	    		Training training = trainingService.oneLoad(id);
	    		//研修の開始日と終了日を取得する
	    		//DateをLocalDateに変換
	    		Date date2 = training.getStartDate();
	    		LocalDate start = ((java.sql.Date)date2).toLocalDate();
	    		Date date3 = training.getEndDate();
	    		LocalDate end = ((java.sql.Date)date3).toLocalDate();
	    		//１日ごとに表示させる.
	    		List<LocalDate> dates = new ArrayList<>();
	    		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(1)) {
	    			dates.add(startDate);
	    		}
	    		//講義最終日のみ最後に追加.
	    		dates.add(end);
	    		
	    		//Listにつめた日付をループしてdaily_reportテーブルにinsert.(date,training_id,student_idのみ)
	    		for(int j = 0; j < dates.size(); j++) {
	    			DailyReport dailyReport = new DailyReport();
	    			dailyReport.setStudentId(studentInsert.getId());
	    			dailyReport.setTrainingId(id);
	    			Date date = Date.from(dates.get(j).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    			dailyReport.setDate(date);
	    			dailyReportService.insert(dailyReport);
	    		}
	    	}else {
	    		Student student = studentService.findByEmail(studentList.get(i).getEmail()); //emailがDBに存在していた場合、受講生を1件検索してきて、studentテーブル以外をinsert.
	    		trainingStudent.setStudentId(student.getId());
	    		trainingStudent.setTrainingId(id);
	    		//training_studentテーブルにinsert
	    		trainingStudentService.insert(trainingStudent);
	    		
	    		Training training = trainingService.oneLoad(id);
	    		//研修の開始日と終了日を取得する
	    		//DateをLocalDateに変換
	    		Date date2 = training.getStartDate();
	    		LocalDate start = ((java.sql.Date)date2).toLocalDate();
	    		Date date3 = training.getEndDate();
	    		LocalDate end = ((java.sql.Date)date3).toLocalDate();
	    		//１日ごとに表示させる.
	    		List<LocalDate> dates = new ArrayList<>();
	    		for(LocalDate startDate = start; startDate.isBefore(end); startDate = startDate.plusDays(1)) {
	    			dates.add(startDate);
	    		}
	    		//講義最終日のみ最後に追加.
	    		dates.add(end);
	    		
	    		//Listにつめた日付をループしてdaily_reportテーブルにinsert.(date,training_id,student_idのみ)
	    		for(int j = 0; j < dates.size(); j++) {
	    			DailyReport dailyReport = new DailyReport();
	    			dailyReport.setStudentId(student.getId());
	    			dailyReport.setTrainingId(id);
	    			Date date = Date.from(dates.get(j).atStartOfDay(ZoneId.systemDefault()).toInstant());
	    			dailyReport.setDate(date);
	    			dailyReportService.insert(dailyReport);
	    		}
	    	}
	    }
	    
	    //weeklyReportテーブルにinsertするために1件検索.
	    Training training2 = trainingService.load(id);

	    //DateをLocalDateに変換
	    Date date2 = training2.getStartDate();
	    LocalDate start = ((java.sql.Date)date2).toLocalDate();
	    Date date3 = training2.getEndDate();
	    LocalDate end = ((java.sql.Date)date3).toLocalDate();
	    //１週間ごとに表示させる.
	    List<LocalDate> weeklyDates = new ArrayList<>();
	    for(LocalDate weeklyReportStartDate = start; weeklyReportStartDate.isBefore(end); weeklyReportStartDate = weeklyReportStartDate.plusDays(7)) {
	    	//			dates.add(weeklyReportStartDate);
	    	LocalDate monday = weeklyReportStartDate.with(DayOfWeek.MONDAY);
	    	weeklyDates.add(monday);
	    }
	    for(int i = 0; i < weeklyDates.size(); i++) {
	    	WeeklyReport weeklyReport = new WeeklyReport();
	    	weeklyReport.setTrainingId(training2.getId());
	    	weeklyReport.setInstructorName(training2.getInstructor().getName());
	    	weeklyReport.setStartDate(Date.from(weeklyDates.get(i).atStartOfDay(ZoneId.systemDefault()).toInstant())); //LocalDateからDateへ変換.
	    	WeeklyReport weeklyReport2 = weeklyReportService.weeklyReportInsert(weeklyReport); //weekly_reportの一部をinsert.
	    	
	    	//student_impressionの一部をinsert.
	    	for(int j = 0; j < training2.getStudentList().size(); j++) {
	    		StudentImpression studentImpression = new StudentImpression();
	    		studentImpression.setStudentName(training2.getStudentList().get(j).getName());
	    		studentImpression.setWeeklyReportId(weeklyReport2.getId());
	    		studentImpressionService.insert(studentImpression);
	    	}
	    	
	    }
	    session.removeAttribute("studentList");
		return "redirect:/admin/training_list";
	}
	
	/**
	 * 管理者画面で日報をみる.
	 * @return
	 */
	@RequestMapping("/view_dailyReport")
	public String viewDailyReport(Integer id, Model model) {
		DailyReport dailyReport = studentService.loadForAdmin(id);
		
		if(dailyReport == null) { //もし受講生が登録されていない場合エラー画面に遷移
			return "view_daily_report_error_page";
		}
		
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
		return "admin/admin_view_daily_report";
	}
	
	/**
	 * 管理者が日報を日付と名前で検索して閲覧する.
	 * @param trainingId
	 * @param date
	 * @param name
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/view_search_dailyReport")
	public String viewSearchDailyReport(Integer trainingId, String date, String name, Model model) throws ParseException {
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
				  return "admin/admin_view_daily_report";
	}
	
	/**
	 * 管理者画面で週報を閲覧する.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/view_weeklyReport")
	public String viewWeeklyReport(Integer id, Model model) {
		session.setAttribute("trainingId", id);
		
		WeeklyReport weeklyReport = weeklyReportService.load(id);
		model.addAttribute("weeklyReport", weeklyReport);
		
		if(weeklyReport.getId() == 0) { //もし受講生が登録されていない場合エラー画面に遷移
			return "view_weekly_report_error_page";
		}
		
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
		return "admin/admin_view_weekly_report";
	}
	
	/**
	 * 管理者画面で週報を日付で検索して閲覧する.
	 * @param date
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/view_search_weeklyReport")
	public String viewSearchWeeklyReport(String date, Model model) throws ParseException {
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
		return "admin/admin_view_weekly_report";
	}
	
	/**
	 * 管理者画面で受講生を編集するために受講生一覧を取得するためのコントローラー.
	 * @param trainingId
	 * @param model
	 * @return
	 */
	@RequestMapping("/student_list")
	public String studentList(Integer trainingId, Model model) {
		List<Training> trainingList = trainingService.selectStudent(trainingId);
		model.addAttribute("trainingList", trainingList);
		return "admin/admin_student_list";
	}
	
	/**
	 * 管理者画面で受講生を編集するために1件検索するコントローラー.
	 * @param studentId
	 * @param model
	 * @return
	 */
	@RequestMapping("/s_edit")
	public String studentEdit(Integer studentId, Integer trainingId, Model model) {
		Student student = studentService.findByStudentId(studentId);
		model.addAttribute("student", student);
		model.addAttribute("trainingId", trainingId);
		return "admin/admin_student_edit";
	}
	
	/**
	 * 管理者画面で受講生を編集する.
	 * @param form
	 * @return
	 */
	@RequestMapping("/student_edit")
	public String studentEdit(RedirectAttributes redirectAttributes,StudentUpdateForm form, Integer trainingId, Model model) {
		studentService.update(form);
		redirectAttributes.addAttribute("trainingId", trainingId);
		return "redirect:/admin/student_list";
	}
	
	/**
	 * 日報を印刷するための初期画面.
	 * @param dailyReportId
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin_daily_report_print")
	public String adminDailyReportPrint(Integer dailyReportId, Model model) {
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
		return "admin/admin_print_daily_report";
	}
	
	/**
	 * 週報を印刷するための初期画面.
	 * @param weeklyReportId
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin_weekly_report_print")
	public String adminWeeklyReportPrint(Integer weeklyReportId, Model model) {
		WeeklyReport weeklyReport = weeklyReportService.printWeeklyReport(weeklyReportId);

		//日付を年月日形式に変換
		Date date = weeklyReport.getStartDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String formattedDate = dateFormat.format(date);
		model.addAttribute("formattedDate", formattedDate);

		model.addAttribute("weeklyReport", weeklyReport);
		return "admin/admin_print_weekly_report";
	}

}
