package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.domain.Admin;
import com.example.domain.AdminCompany;
import com.example.domain.Company;
import com.example.domain.CompanyMember;

@Repository
public class AdminRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private SimpleJdbcInsert insert;
	
	/**
	 * INSERT時に採番されたIDを取得する方法.
	 */
	@PostConstruct
	public void init() {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert((JdbcTemplate)template.getJdbcOperations());
		SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName("admins");
		insert = withTableName.usingGeneratedKeyColumns("id");
	}
	
	private static final RowMapper<Admin> ADMIN_ROW_MAPPER = (rs,i) -> {
		Admin admin = new Admin();
		admin.setId(rs.getInt("id"));
		admin.setKana(rs.getString("kana"));
		admin.setName(rs.getString("name"));
		admin.setPassword(rs.getString("password"));
		admin.setEmail(rs.getString("email"));
		admin.setCanShowAllCompany(rs.getBoolean("can_show_all_company"));
		return admin;
	};
	
	// adminとadmin_companyとcompanyテーブルのマッパー.
	private static final ResultSetExtractor<List<Admin>> ADMIN_RESULT_SET_EXTRACTOR = (rs) -> {
		List<Admin> adminList = new ArrayList<>();
		List<Company> companyList = null;
		
		int preId = -1;
		
		while(rs.next()) {
			int id = rs.getInt("A_id");
			
			if(id != preId) {
				Admin admin = new Admin();
				admin.setId(rs.getInt("A_id"));
				admin.setKana(rs.getString("A_kana"));
				admin.setName(rs.getString("A_name"));
				admin.setPassword(rs.getString("A_password"));
				admin.setEmail(rs.getString("A_email"));
				admin.setCanShowAllCompany(rs.getBoolean("A_can_show_all_company"));
				
				companyList = new ArrayList<>();
				admin.setCompanyList(companyList);
				adminList.add(admin);
			}
			
			int middleCheckId = rs.getInt("B_admin_id");
			
			if(middleCheckId != 0) {
				AdminCompany adminCompany = new AdminCompany();
				adminCompany.setAdminId(rs.getInt("B_admin_id"));
				adminCompany.setCompanyId(rs.getInt("B_company_id"));
				
				Company company = new Company();
				company.setId(rs.getInt("C_id"));
				company.setKana(rs.getString("C_kana"));
				company.setName(rs.getString("C_name"));
				company.setRemarks(rs.getString("C_remarks"));
				
				companyList.add(company);
			}
			preId = id;
		}
		return adminList;
	};
	
	// adminとadmin_companyとcompanyとcompany_memberテーブルのマッパー.
		private static final ResultSetExtractor<List<Admin>> ADMIN_RESULT_SET_EXTRACTOR2 = (rs) -> {
			List<Admin> adminList = new ArrayList<>();
			List<Company> companyList = null;
			List<CompanyMember> companyMemberList = null;
			
			int preId = -1;
			
			while(rs.next()) {
				int id = rs.getInt("A_id");
				
				if(id != preId) {
					Admin admin = new Admin();
					admin.setId(rs.getInt("A_id"));
					admin.setKana(rs.getString("A_kana"));
					admin.setName(rs.getString("A_name"));
					admin.setPassword(rs.getString("A_password"));
					admin.setEmail(rs.getString("A_email"));
					admin.setCanShowAllCompany(rs.getBoolean("A_can_show_all_company"));
					
					companyList = new ArrayList<>();
					admin.setCompanyList(companyList);
					adminList.add(admin);
				}
				
				int middleCheckId = rs.getInt("B_admin_id");
				
				if(middleCheckId != 0) {
					AdminCompany adminCompany = new AdminCompany();
					adminCompany.setAdminId(rs.getInt("B_admin_id"));
					adminCompany.setCompanyId(rs.getInt("B_company_id"));
					
					Company company = new Company();
					company.setId(rs.getInt("C_id"));
					company.setKana(rs.getString("C_kana"));
					company.setName(rs.getString("C_name"));
					company.setRemarks(rs.getString("C_remarks"));
					
					companyMemberList = new ArrayList<>();
					CompanyMember companyMember = new CompanyMember();
					companyMember.setId(rs.getInt("D_id"));
					companyMember.setCompanyId(rs.getInt("D_company_id"));
					companyMember.setEmail(rs.getString("D_email"));
					companyMember.setKana(rs.getString("D_kana"));
					companyMember.setName(rs.getString("D_name"));
					companyMember.setPassword(rs.getString("D_password"));
					companyMemberList.add(companyMember);
					company.setCompanyMemberList(companyMemberList);
					
					companyList.add(company);
				}
				preId = id;
			}
			return adminList;
		};
	
	// adminとadmin_companyとcompanyテーブルを結合したSQL.
	private String joinTable() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		sb.append(" A.id A_id, ");
		sb.append(" A.name A_name, ");
		sb.append(" A.kana A_kana, ");
		sb.append(" A.email A_email, ");
		sb.append(" A.password A_password, ");
		sb.append(" A.can_show_all_company A_can_show_all_company, ");
		sb.append(" B.admin_id B_admin_id, ");
		sb.append(" B.company_id B_company_id, ");
		sb.append(" C.id C_id, ");
		sb.append(" C.name C_name, ");
		sb.append(" C.kana C_kana, ");
		sb.append(" C.remarks C_remarks ");
		sb.append(" FROM ");
		sb.append(" admins A ");
		sb.append(" LEFT OUTER JOIN ");
		sb.append(" admin_company B ");
		sb.append(" ON A.id = B.admin_id ");
		sb.append(" LEFT OUTER JOIN ");
		sb.append(" companies C ");
		sb.append(" ON B.company_id = C.id ");
		
		String JoinSql = sb.toString();
		return JoinSql;
	}
	
	// adminとadmin_companyとcompanyとcompany_memberテーブルを結合したSQL.
		private String join4Table() {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT ");
			sb.append(" A.id A_id, ");
			sb.append(" A.name A_name, ");
			sb.append(" A.kana A_kana, ");
			sb.append(" A.email A_email, ");
			sb.append(" A.password A_password, ");
			sb.append(" A.can_show_all_company A_can_show_all_company, ");
			sb.append(" B.admin_id B_admin_id, ");
			sb.append(" B.company_id B_company_id, ");
			sb.append(" C.id C_id, ");
			sb.append(" C.name C_name, ");
			sb.append(" C.kana C_kana, ");
			sb.append(" C.remarks C_remarks, ");
			sb.append(" D.id D_id, ");
			sb.append(" D.name D_name, ");
			sb.append(" D.kana D_kana, ");
			sb.append(" D.password D_password, ");
			sb.append(" D.email D_email, ");
			sb.append(" D.company_id D_company_id ");
			sb.append(" FROM ");
			sb.append(" admins A ");
			sb.append(" LEFT OUTER JOIN ");
			sb.append(" admin_company B ");
			sb.append(" ON A.id = B.admin_id ");
			sb.append(" LEFT OUTER JOIN ");
			sb.append(" companies C ");
			sb.append(" ON B.company_id = C.id ");
			sb.append(" LEFT OUTER JOIN ");
			sb.append(" company_members D ");
			sb.append(" ON C.id = D.company_id ");
			
			String JoinSql = sb.toString();
			return JoinSql;
		}
	
	/**
	 * 管理者がログインするためのリポジトリ.
	 * @param email
	 * @param password
	 * @return
	 */
	public Admin findByEmail(String email) {
		String sql = "SELECT id, name, kana, email, password, can_show_all_company FROM admins WHERE email = :email";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
		List<Admin> adminList = template.query(sql, param, ADMIN_ROW_MAPPER);
		if(adminList.size() == 0) {
			return null;
		}
		return adminList.get(0);
	}
	
	/**
	 * 管理者画面で管理者を新規登録するためのリポジトリ.
	 * @param admin
	 * @return
	 */
	public Admin insert(Admin admin) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(admin);
		Number key = insert.executeAndReturnKey(param);
		admin.setId(key.intValue());
		return admin;
	}
	
	/**
	 * 管理者画面で管理者の一覧を取得するためのリポジトリ.
	 * @return
	 */
	public List<Admin> findAll(){
		StringBuilder sql = new StringBuilder();
		sql.append(joinTable());
		sql.append(" ORDER BY A.id ");
		List<Admin> adminList = template.query(sql.toString(), ADMIN_RESULT_SET_EXTRACTOR);
		return adminList;
	}
	
	/**
	 * 管理者画面で管理者編集画面で情報を表示するために1件検索するリポジトリ.
	 * @param id
	 * @return
	 */
	public Admin load(Integer id) {
		String sql = "SELECT id, name, kana, email, password, can_show_all_company FROM admins WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Admin admin = template.queryForObject(sql, param, ADMIN_ROW_MAPPER);
		return admin;
	}
	
	/**
	 * 管理者画面で管理者を編集するためのリポジトリ.
	 * @param admin
	 * @return 
	 */
	public void update(Admin admin) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(admin);
		String sql = "UPDATE admins SET name = :name, kana = :kana, email = :email, can_show_all_company = :canShowAllCompany WHERE id = :id";
		template.update(sql, param);
	}
	
	/**
	 * 管理者IDで担当している企業を検索するためのリポジトリ.
	 * @param id
	 * @return
	 */
	public List<Admin> loadByAdminId(Integer id){
		StringBuilder sql = new StringBuilder();
		sql.append(join4Table());
		sql.append(" WHERE A.id = :id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Admin> adminList = template.query(sql.toString(), param, ADMIN_RESULT_SET_EXTRACTOR2);
		return adminList;
	}
	
	/**
	 * 名前とadminIdで企業の曖昧検索するためのリポジトリ.
	 * @param name
	 * @param id
	 * @return
	 */
	public List<Admin> findByNameAndAdminId(String name,Integer id){
		StringBuilder sql = new StringBuilder();
		sql.append(join4Table());
		sql.append(" WHERE A.id = :id AND C.name LIKE :name ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id).addValue("name", "%" + name + "%");
		List<Admin> adminList = template.query(sql.toString(), param, ADMIN_RESULT_SET_EXTRACTOR2);
		return adminList;
	}
	
	/**
	 * 新しいパスワードをインサートするためのリポジトリ.
	 * @param student
	 */
	public void saveNewPassword(Admin admin) {
		String sql = "UPDATE admins SET password = :password WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("password", admin.getPassword()).addValue("id", admin.getId());
		template.update(sql, param);
	}

}
