package telran.college.service;

import java.util.*;

import telran.college.dto.*;

public interface CollegeService {
	List<String> bestStudentsSubjectType(String type, int nStudents);

	List<NameScore> studentsAvgMarks();

	List<StudentCity> studentsScoreLess(int score);

	List<NamePhone> studentsBorn(int month);

	List<NamePhone> lectureCity(String city);

	List<NameScore> subjectsScores(String name);

	List<LecturerHours> lecturersMostHours(int nLecturers);
}
