package com.example.form;

import org.springframework.web.multipart.MultipartFile;

public class StudentRegisterForm {
	
	/** 生徒の情報が入ったcsvファイル */
	private MultipartFile csv;

	public MultipartFile getCsv() {
		return csv;
	}

	public void setCsv(MultipartFile csv) {
		this.csv = csv;
	}

	@Override
	public String toString() {
		return "StudentRegisterForm [csv=" + csv + "]";
	}
	
	

}
