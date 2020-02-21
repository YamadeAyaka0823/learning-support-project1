package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.CompanyMember;
import com.example.domain.Student;

@Repository
public class CompanyMemberRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	
	private static final RowMapper<CompanyMember> COMPANY_MEMBER_ROW_MAPPER = (rs,i) -> {
		CompanyMember companyMember = new CompanyMember();
		companyMember.setId(rs.getInt("id"));
		companyMember.setName(rs.getString("name"));
		companyMember.setKana(rs.getString("kana"));
		companyMember.setPassword(rs.getString("password"));
		companyMember.setEmail(rs.getString("email"));
		companyMember.setCompanyId(rs.getInt("company_id"));
		return companyMember;
	};
	
	
	/**
	 * 企業担当者がログインするためのリポジトリ.
	 * @param email
	 * @param password
	 * @return
	 */
	public CompanyMember findByEmailAndPassword(String email, String password) {
		String sql = "SELECT id, name, kana, password, email, company_id FROM company_members WHERE email = :email AND password = :password";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email).addValue("password", password);
		List<CompanyMember> companyMemberList = template.query(sql, param, COMPANY_MEMBER_ROW_MAPPER);
		if(companyMemberList.size() == 0) {
			return null;
		}
		return companyMemberList.get(0);
	}
	
	/**
	 * 企業担当者がログインするためのリポジトリ.
	 * @param email
	 * @param password
	 * @return
	 */
	public CompanyMember findByEmail(String email) {
		String sql = "SELECT id, name, kana, password, email, company_id FROM company_members WHERE email = :email";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
		List<CompanyMember> companyMemberList = template.query(sql, param, COMPANY_MEMBER_ROW_MAPPER);
		if(companyMemberList.size() == 0) {
			return null;
		}
		return companyMemberList.get(0);
	}
	
	/**
	 * 管理者画面で企業担当者を新規登録するためのリポジトリ.
	 * @param companyMember
	 */
	public void insert(CompanyMember companyMember) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(companyMember);
		String sql = "INSERT INTO company_members ( name, kana, email, password, company_id ) VALUES ( :name, :kana, :email, :password, :companyId )";
		template.update(sql, param);
	}
	
	/**
	 * 管理者画面の企業担当者登録画面の下に担当者一覧を表示させるためのリポジトリ.
	 * @param companyId
	 * @return
	 */
	public List<CompanyMember> findByCompanyId(Integer companyId){
		String sql = "SELECT id, name, kana, email, password, company_id FROM company_members WHERE company_id = :companyId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("companyId", companyId);
		List<CompanyMember> companyMemberList = template.query(sql, param, COMPANY_MEMBER_ROW_MAPPER);
		return companyMemberList;
	}
	
	/**
	 * 管理者画面で企業担当者を削除するためのリポジトリ.
	 * @param id
	 */
	public void delete(Integer id) {
		String sql = "DELETE FROM company_members WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}
	
	/**
	 * 新しいパスワードをインサートするためのリポジトリ.
	 * @param student
	 */
	public void saveNewPassword(CompanyMember companyMember) {
		String sql = "UPDATE company_members SET password = :password WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("password", companyMember.getPassword()).addValue("id", companyMember.getId());
		template.update(sql, param);
	}

}
