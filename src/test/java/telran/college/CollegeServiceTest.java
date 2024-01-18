package telran.college;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import telran.college.dto.*;
import telran.college.entities.*;
import telran.college.repo.*;
import telran.college.service.CollegeService;
import telran.exceptions.NotFoundException;

@SpringBootTest
@Sql(scripts = { "db_test.sql" })
class CollegeServiceTest {

	@Autowired
	CollegeService collegeService;
	@Autowired
	SubjectRepo subjectRepo;
	@Autowired
	LecturerRepo lecturerRepo;
	@Autowired
	StudentRepo studentRepo;

	PersonDto newStudent = new PersonDto(129, "Alex", LocalDate.of(1995, 05, 12), "Holon", "051-6677889");
	PersonDto studentExists = new PersonDto(128, "Yakob", LocalDate.of(2000, 10, 10), "Rehovot", "051-6677889");
	PersonDto studentUpdate = new PersonDto(128, "Yakob", LocalDate.of(2000, 10, 10), "Bat-yam", "051-6677888");
	PersonDto newLecturer = new PersonDto(1233, "Ivanov", LocalDate.of(1976, 04, 25), "Tel-aviv", "052-7778881");
	PersonDto LecturerExists = new PersonDto(1232, "Sockratus", LocalDate.of(1960, 03, 15), "Rehovot", "057-7664821");
	PersonDto lecturerUpdate = new PersonDto(1232, "Sockratus", LocalDate.of(1960, 03, 15), "Haifa", "052-7778887");
	PersonDto personDto = new PersonDto(1230, "Abraham", LocalDate.parse("1957-01-23"),
			"Jerusalem", "050-1111122");
	SubjectDto newSubject = new SubjectDto(326, "Angular", 70, 1232l, SubjectType.FRONT_END);
	SubjectDto lecturerIdNull = new SubjectDto(328, "Angular", 70, 1235l, SubjectType.FRONT_END);
	SubjectDto subjectExists = new SubjectDto(321, "Java Core", 150, 1230l, SubjectType.BACK_END);
	MarkDto studentNotExist = new MarkDto(129, 321, 70);
	MarkDto subjectNotExist = new MarkDto(123, 326, 90);
	MarkDto newMark = new MarkDto(123, 321, 100);

	@Test
	void bestStudentsTypeTest() {
		List<String> students = collegeService.bestStudentsSubjectType("BACK_END", 2);
		String[] expected = { "David", "Yosef" };
		assertArrayEquals(expected, students.toArray(String[]::new));
	}

	@Test
	void studentsAvgScoreTest() {
		List<NameScore> studentMarks = collegeService.studentsAvgMarks();
		String[] students = { "David", "Rivka", "Vasya", "Sara", "Yosef" };
		int[] scores = { 96, 95, 83, 80, 78 };
		NameScore[] studentMarksArr = studentMarks.toArray(NameScore[]::new);
		IntStream.range(0, students.length)
				.forEach(i -> {
					assertEquals(students[i], studentMarksArr[i].getName());
					assertEquals(scores[i], studentMarksArr[i].getScore());
				});
	}

	@Test
	void lecturersMostHoursTest() {
		List<LecturerHours> lecturersHours = collegeService.lecturersMostHours(2);
		LecturerHours[] lecturersHoursArr = lecturersHours.toArray(LecturerHours[]::new);
		String[] lecturers = { "Abraham", "Mozes" };
		int[] hours = { 225, 130 };
		IntStream.range(0, hours.length)
				.forEach(i -> {
					assertEquals(lecturers[i], lecturersHoursArr[i].getName());
					assertEquals(hours[i], lecturersHoursArr[i].getHours());
				});
	}

	@Test
	void studentsScoresLessTest() {
		List<StudentCity> studentCityList = collegeService.studentsScoresLess(1);
		assertEquals(1, studentCityList.size());
		StudentCity studentCity = studentCityList.get(0);
		assertEquals("Rehovot", studentCity.getCity());
		assertEquals("Yakob", studentCity.getName());
	}

	@Test
	void studentsBurnMonthTest() {
		String[] namesExpected = { "Vasya", "Yakob" };
		String[] phonesExpected = { "054-1234567", "051-6677889" };
		NamePhone[] studentPhonesArr = collegeService.studentsBurnMonth(10)
				.toArray(NamePhone[]::new);
		assertEquals(phonesExpected.length, studentPhonesArr.length);
		IntStream.range(0, phonesExpected.length)
				.forEach(i -> {
					assertEquals(namesExpected[i], studentPhonesArr[i].getName());
					assertEquals(phonesExpected[i], studentPhonesArr[i].getPhone());
				});
	}

	@Test
	void lecturesCityTest() {
		String[] expectedNames = { "Abraham", "Mozes" };
		String[] expectedPhones = { "050-1111122", "054-3334567" };
		NamePhone[] namePhones = collegeService.lecturersCity("Jerusalem")
				.toArray(NamePhone[]::new);
		assertEquals(expectedNames.length, namePhones.length);
		IntStream.range(0, namePhones.length)
				.forEach(i -> {
					assertEquals(expectedNames[i], namePhones[i].getName());
					assertEquals(expectedPhones[i], namePhones[i].getPhone());
				});
	}

	@Test
	void subjectsScoresTest() {
		String[] subjects = { "Java Core", "Java Technologies", "HTML/CSS", "JavaScript", "React" };
		int[] scores = { 75, 60, 95, 85, 100 };
		SubjectNameScore[] subjectScores = collegeService.subjectsScores("Vasya")
				.toArray(SubjectNameScore[]::new);
		assertEquals(scores.length, subjectScores.length);
		IntStream.range(0, scores.length).forEach(i -> {
			assertEquals(subjects[i], subjectScores[i].getSubjectName());
			assertEquals(scores[i], subjectScores[i].getScore());
		});
	}

	@Test
	void addStudentTest() {
		PersonDto addStudent = collegeService.addStudent(newStudent);
		assertEquals(newStudent, addStudent);
		assertThrowsExactly(IllegalStateException.class, () -> collegeService.addStudent(studentExists));
	}

	@Test
	void addLecturerTest() {
		PersonDto addLecturer = collegeService.addLecturer(newLecturer);
		assertEquals(newLecturer, addLecturer);
		assertThrowsExactly(IllegalStateException.class, () -> collegeService.addLecturer(LecturerExists));
	}

	@Test
	void updateStudentTest() {
		assertEquals(studentUpdate, collegeService.updateStudent(studentUpdate));
		assertThrowsExactly(NotFoundException.class, () -> collegeService.updateStudent(newStudent));
	}

	@Test
	void updateLecturerTest() {
		assertEquals(lecturerUpdate, collegeService.updateLecturer(lecturerUpdate));
		assertThrowsExactly(NotFoundException.class, () -> collegeService.updateLecturer(newLecturer));
	}

	@Test
	void addSubjectTest() {
		assertEquals(newSubject, collegeService.addSubject(newSubject));
		assertThrowsExactly(IllegalStateException.class, () -> collegeService.addSubject(subjectExists));
		assertThrowsExactly(NotFoundException.class, () -> collegeService.addSubject(lecturerIdNull));
	}

	@Test
	void addMarkTest() {
		assertThrowsExactly(NotFoundException.class, () -> collegeService.addMark(studentNotExist));
		assertThrowsExactly(NotFoundException.class, () -> collegeService.addMark(subjectNotExist));
		assertEquals(newMark, collegeService.addMark(newMark));
	}

	@Test
	void deleteLecturerTest() {
		assertThrowsExactly(NotFoundException.class,
				() -> collegeService.deleteLecturer(2000));
		assertEquals(personDto, collegeService.deleteLecturer(1230));
		assertNull(lecturerRepo.findById(1230l).orElse(null));
		Subject subject = subjectRepo.findById(322l).get();
		assertNull(subject.getLecturer());
	}

	@Test
	void deleteSubjectTest() {
		assertThrowsExactly(NotFoundException.class, () -> collegeService.deleteSubject(1000));
		assertEquals(subjectExists, collegeService.deleteSubject(321));
		assertNull(subjectRepo.findById(321l).orElse(null));
	}

	@Test
	void deleteStudentsLessScoresTest() {
		PersonDto [] expected = {
				new PersonDto(128, "Yakob", LocalDate.parse("2000-10-10"), "Rehovot",
						"051-6677889")	
			};
			PersonDto [] actual  = collegeService.deleteStudentsHavingScoresLess(2)
					.toArray(PersonDto[]::new);
			assertArrayEquals(expected, actual);
			assertNull(studentRepo.findById(128l).orElse(null));
	}

}