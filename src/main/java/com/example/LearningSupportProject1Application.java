package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.service.AdminDetailServiceImpl;
import com.example.service.CompanyDetailServiceImpl;
import com.example.service.InstructorDetailServiceImpl;
import com.example.service.StudentDetailServiceImpl;

@SpringBootApplication
public class LearningSupportProject1Application {

	public static void main(String[] args) {
		SpringApplication.run(LearningSupportProject1Application.class, args);
	}
	
	@Bean(name="AdminDetailServiceImpl")
	public UserDetailsService adminDetailService() {
		return new AdminDetailServiceImpl();
	}
	
	@Bean(name="StudentDetailServiceImpl")
	public UserDetailsService studentDetailService() {
		return new StudentDetailServiceImpl();
	}
	
	@Bean(name="InstructorDetailServiceImpl")
	public UserDetailsService instructorDetailService() {
		return new InstructorDetailServiceImpl();
	}
	
	@Bean(name="CompanyDetailServiceImpl")
	public UserDetailsService companyDetailService() {
		return new CompanyDetailServiceImpl();
	}

}
