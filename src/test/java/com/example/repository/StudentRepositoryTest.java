package com.example.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.domain.Student;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentRepositoryTest {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	//メールアドレスで受講生を1件検索するテスト
	@Test
	public void findByEmail() {
		System.out.println("受講生がログインするテスト開始");
		
		Student student = studentRepository.findByEmail("minami@gmail.com");
		
		assertThat("名前", student.getName(), is("高橋みなみ"));
		assertThat("かな", student.getKana(), is("たかはしみなみ"));
		assertThat("会社ID", student.getCompanyId(), is(3));
	}
	
	//studentIdで受講生が受け持つ研修一覧を取得するテスト
	@Test
	public void load() {
		System.out.println("研修を検索するテスト開始");
		
		Student student = studentRepository.load(4);
		
		assertThat("研修一覧", student.getTrainingList().size(), is(2));
	}

}
