package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	/**
	 * 受講生ログイン初期画面.
	 * @return
	 */
	@RequestMapping("")
	public String index() {
		return "student/student_login";
	}

}
