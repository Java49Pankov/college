package telran.college.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.college.dto.*;

import telran.college.repo.*;

@Service
@RequiredArgsConstructor
public class CollegeServiceImpl implements CollegeService {
	final StudentRepo studentRepo;
	final LecturerRepo lecturerRepo;
	final SubjectRepo subjectRepo;
	final MarkRepo markRepo;

	@Override
	public List<String> bestStudentsSubjectType(String type, int nStudents) {
		return studentRepo.findBestStudentsSubjectType(type, nStudents);
	}

	@Override
	public List<NameScore> studentsAvgMarks() {
		return studentRepo.studentsMarks();
	}

	@Override
	public List<StudentCity> studentsScoreLess(int score) {
		return studentRepo.findStudentsScoreLess(score);
	}

	@Override
	public List<NamePhone> studentsBorn(int month) {
		return studentRepo.findStudentsBornMoth(month);
	}

	@Override
	public List<NamePhone> lectureCity(String city) {
		return studentRepo.findLecturersByCity(city);
	}

	@Override
	public List<NameScore> subjectsScores(String name) {
		return studentRepo.findSubjectsScoresByStudent(name);
	}

	@Override
	public List<LecturerHours> lecturersMostHours(int nLecturers) {
		return lecturerRepo.findLecturesMostHours(nLecturers);
	}

}
