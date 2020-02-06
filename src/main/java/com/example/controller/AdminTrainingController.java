package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Training;
import com.example.service.TrainingService;

@Controller
@RequestMapping("/adminTraining")
public class AdminTrainingController {
	
	@Autowired
	private TrainingService trainingService;
	
	
	/**
	 * 管理者画面で研修一覧画面.
	 * @return
	 */
	@RequestMapping("/training_list")
	public String trainingList(Model model) {
		List<Training> trainingList = trainingService.findAll();
		model.addAttribute("trainingList", trainingList);
		return "admin/admin_training_list";
	}

}
