package com.example.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
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
		
		assertThat("件数が一致しません", trainingList.size(), is(7));
		assertThat("日付順に並んでいません", trainingList.get(3).getName(), is("Java研修"));
		assertThat("日付順に並んでいません", trainingList.get(5), is("JavaScript研修"));
	}

}
