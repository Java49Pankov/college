package telran.college;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.IntStream;

import org.checkerframework.common.value.qual.IntRange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import telran.college.dto.*;

import telran.college.service.CollegeService;

@SpringBootTest
@Sql(scripts = { "db_test.sql" })
class CollegeServiceTest {
	@Autowired
	CollegeService collegeService;

	@Test
	void bestStudentsTypeTest() {
		List<String> students = collegeService.bestStudentsSubjectType("BACK_END", 2);
		String[] expected = { "David", "Yosef" };
		assertArrayEquals(expected, students.toArray(String[]::new));
	}

	@Test
	void studentsAvgScoreTest() {
		List<NameScore> studentMarks = collegeService.studentsAvgMarks();
		String[] expectedName = { "David", "Rivka", "Vasya", "Sara", "Yosef" };
		int[] expectedScore = { 96, 95, 83, 80, 78 };
		NameScore[] studentMarkArr = studentMarks.toArray(NameScore[]::new);
		IntStream.range(0, expectedName.length)
				.forEach(i -> {
					assertEquals(expectedName[i], studentMarkArr[i].getName());
					assertEquals(expectedScore[i], studentMarkArr[i].getScore());
				});
	}

	@Test
	void studentsLessScoreTest() {
		List<StudentCity> studentCityList = collegeService.studentsScoreLess(3);
		String[] expectedName = { "Rivka", "Yakob", "Yosef" };
		String[] expectedCity = { "Lod", "Rehovot", "Rehovot" };
		StudentCity[] studentCityArr = studentCityList.toArray(StudentCity[]::new);
		IntStream.range(0, expectedName.length)
				.forEach(i -> {
					assertEquals(expectedName[i], studentCityArr[i].getName());
					assertEquals(expectedCity[i], studentCityArr[i].getCity());
				});
	}

	@Test
	void studentBornMonthTest() {
		List<NamePhone> studentsBorn = collegeService.studentsBorn(10);
		String[] expectedName = { "Vasya", "Yakob" };
		String[] expectedPhone = { "054-1234567", "051-6677889" };
		NamePhone[] studentBornArr = studentsBorn.toArray(NamePhone[]::new);
		IntStream.range(0, expectedName.length)
				.forEach(i -> {
					assertEquals(expectedName[i], studentBornArr[i].getName());
					assertEquals(expectedPhone[i], studentBornArr[i].getPhone());
				});
	}

	@Test
	void lecturersByCityTest() {
		List<NamePhone> lecturerCity = collegeService.lectureCity("Jerusalem");
		String[] expectedName = { "Abraham", "Mozes" };
		String[] expectedPhone = { "050-1111122", "054-3334567" };
		NamePhone[] lecturersCityArr = lecturerCity.toArray(NamePhone[]::new);
		IntStream.range(0, expectedName.length)
				.forEach(i -> {
					assertEquals(expectedName[i], lecturersCityArr[i].getName());
					assertEquals(expectedPhone[i], lecturersCityArr[i].getPhone());
				});
	}

	@Test
	void subjectsScoreByNameTest() {
		List<NameScore> subjectsScore = collegeService.subjectsScores("Yosef");
		String[] expectedSubjects = { "Java Core", "Java Technologies" };
		int[] expectedScores = { 80, 75 };
		NameScore[] subjectsScoresArr = subjectsScore.toArray(NameScore[]::new);
		IntStream.range(0, expectedScores.length)
				.forEach(i -> {
					assertEquals(expectedSubjects[i], subjectsScoresArr[i].getName());
					assertEquals(expectedScores[i], subjectsScoresArr[i].getScore());
				});
	}

	@Test
	void lecturersMostHoursTest() {
		List<LecturerHours> lecturersHours = collegeService.lecturersMostHours(1);
		String expectedName = "Abraham";
		int expectedHours = 225;
		lecturersHours.forEach(lh -> {
			assertEquals(expectedName, lh.getName());
			assertEquals(expectedHours, lh.getHours());
		});
	}

}
