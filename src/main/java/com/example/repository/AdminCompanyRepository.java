package com.example.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.AdminCompany;

@Repository
public class AdminCompanyRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/**
	 * 管理者画面の管理者新規登録するためのリポジトリ.
	 * @param adminCompany
	 */
	public void insert(AdminCompany adminCompany) {
		String sql = "INSERT INTO admin_company (admin_id, company_id ) VALUES (:adminId, :companyId)";
		SqlParameterSource param = new MapSqlParameterSource().addValue("adminId", adminCompany.getAdminId()).addValue("companyId", adminCompany.getCompanyId());
		template.update(sql, param);
	}
	
	/**
	 * 管理者画面で中間テーブルを削除するためのリポジトリ.
	 * @param adminId
	 */
	public void delete(Integer adminId) {
		String sql = "DELETE FROM admin_company WHERE admin_id = :adminId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("adminId", adminId);
		template.update(sql, param);
	}
	

}
