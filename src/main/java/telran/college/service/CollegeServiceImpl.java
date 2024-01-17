package telran.college.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.college.dto.*;
import telran.college.entities.*;
import telran.college.repo.*;
import telran.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollegeServiceImpl implements CollegeService {
	final StudentRepo studentRepo;
	final LecturerRepo lecturerRepo;
	final SubjectRepo subjectRepo;
	final MarkRepo markRepo;

	@Override
	public List<String> bestStudentsSubjectType(String type, int nStudents) {
		return markRepo.findBestStudentsSubjectType(SubjectType.valueOf(type), nStudents);
	}

	@Override
	public List<NameScore> studentsAvgMarks() {
		return markRepo.studentsMarks();
	}

	@Override
	public List<LecturerHours> lecturersMostHours(int nLecturers) {
		return lecturerRepo.findLecturersMostHours(nLecturers);
	}

	@Override
	public List<StudentCity> studentsScoresLess(int nThreshold) {
		return markRepo.findStudentsScoresLess(nThreshold);
	}

	@Override
	public List<NamePhone> studentsBurnMonth(int month) {
		return studentRepo.findStudentsBurnMonth(month);
	}

	@Override
	public List<NamePhone> lecturersCity(String city) {
		return lecturerRepo.findByCity(city);
	}

	@Override
	public List<SubjectNameScore> subjectsScores(String studentName) {
		return markRepo.findByStudentName(studentName);
	}

	@Override
	@Transactional(readOnly = false)
	public PersonDto addStudent(PersonDto personDto) {
		if (studentRepo.existsById(personDto.id())) {
			throw new IllegalStateException(personDto.id() + " already exists");
		}
		studentRepo.save(new Student(personDto));
		return personDto;
	}

	@Override
	public PersonDto addLecturer(PersonDto personDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public SubjectDto addSubject(SubjectDto subjectDto) {
		Lecturer lecturer = null;
		if (subjectDto.lecturerId() != null) {
			lecturer = lecturerRepo.findById(subjectDto.lecturerId())
					.orElseThrow(() -> new NotFoundException(subjectDto.lecturerId() + " not exists"));
		}
		Subject subject = new Subject(subjectDto);
		subject.setLecturer(lecturer);
		subjectRepo.save(subject);
		return subjectDto;
	}

	@Override
	public MarkDto addMark(MarkDto markDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersonDto updateStudent(PersonDto personDto) {
		Student student = studentRepo.findById(personDto.id())
				.orElseThrow(() -> new NotFoundException(personDto.id() + " not exists"));
		student.setCity(personDto.city());
		student.setPhone(personDto.phone());
		return personDto;
	}

	@Override
	public PersonDto updateLecturer(PersonDto personDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersonDto deleteLecturer(long id) {
		// TODO
		// find Lecturer by id (with possible NotFoundException)
		// 1. find all subjects with a given lecturer id/ findByLecturerId
		// 2. update all subjects with being deleted lecturer by setting null in field
		// lecturer
		// 3. lecturerRepo.delete(lecturer)
		// 4. returns lecturer.build()
		return null;
	}

	@Override
	public SubjectDto deleteSubject(long id) {
		// TODO
		// find Subject by id (with possible NotFoundException)
		// delete all marks with a given subject
		// delete subject
		// return subject.build()
		return null;
	}

	@Override
	public List<PersonDto> deleteStudentsHavingScoresLess(int nScores) {
		// TODO Auto-generated method stub
		return null;
	}

}