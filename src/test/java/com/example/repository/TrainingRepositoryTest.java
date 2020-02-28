package com.example.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.domain.Training;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrainingRepositoryTest {
	
	@Autowired
	private TrainingRepository trainingRepository;

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

	@Test
	public void test() {
		
       System.out.println("研修を全件検索するテスト開始");
		
		List<Training> trainingList = trainingRepository.findAllTraining();
		
		assertThat("件数が一致", trainingList.size(), is(10));
		assertThat("日付順", trainingList.get(3).getName(), is("Java研修"));
	}
	
	@Test
	public void load() {
		System.out.println("研修IDで1件検索するテスト開始");
		
		Training training = trainingRepository.load(1);
		
		Date startDate = training.getStartDate();
		String str = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
		Date endDate = training.getEndDate();
		String str2 = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
		
		assertThat("研修名", training.getName(), is("Java研修"));
		assertThat("研修開始日",str, is("2020-01-06"));
		assertThat("研修終了日", str2, is("2020-03-06"));
		assertThat("講師ID", training.getInstructorId(), is(1));
	}
	
	@Test
	public void findByName() {
		System.out.println("研修名で研修を曖昧検索するテスト開始");
		
		List<Training> trainingList = trainingRepository.findByName("P");
		
		assertThat("研修リスト", trainingList.size(), is(2));
	}

}
