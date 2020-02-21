package com.example.form;

public class ChangePasswordForm {
	
	/** 新しいパスワード */
	private String newPassword;
	/** 新しい確認用のパスワード */
	private String confirmNewPassword;
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}
	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}
	
	@Override
	public String toString() {
		return "ChangePasswordForm [newPassword=" + newPassword + ", confirmNewPassword=" + confirmNewPassword + "]";
	}
	

}
