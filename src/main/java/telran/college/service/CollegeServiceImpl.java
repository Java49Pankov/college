package telran.college.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
		return markRepo.findStudentsCityScoresLess(nThreshold);
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
	@Transactional(readOnly = false)
	public PersonDto addLecturer(PersonDto personDto) {
		if (lecturerRepo.existsById(personDto.id())) {
			throw new IllegalStateException(personDto.id() + " already exists");
		}
		lecturerRepo.save(new Lecturer(personDto));
		return personDto;
	}

	@Override
	@Transactional(readOnly = false)
	public SubjectDto addSubject(SubjectDto subjectDto) {
		if (subjectRepo.existsById(subjectDto.id())) {
			throw new IllegalStateException("subject " + subjectDto.id() + " already exists");
		}
		Lecturer lecturer = null;
		if (subjectDto.lecturerId() != null) {
			lecturer = findLecturer(subjectDto.lecturerId());
		}
		Subject subject = new Subject(subjectDto);
		subject.setLecturer(lecturer);
		subjectRepo.save(subject);
		return subjectDto;
	}

	private Lecturer findLecturer(@Positive Long lecturerId) {
		return lecturerRepo.findById(lecturerId)
				.orElseThrow(() -> new NotFoundException(lecturerId + " not exists"));
	}

	@Override
	@Transactional(readOnly = false)
	public MarkDto addMark(MarkDto markDto) {
		Subject subject = findSubject(markDto.subjectId());
		Student student = findStudent(markDto.studentId());
		Mark mark = new Mark(student, subject, markDto.score());
		markRepo.save(mark);
		return markDto;
	}

	private Student findStudent(@NotNull @Positive long studentId) {
		return studentRepo.findById(studentId)
				.orElseThrow(() -> new NotFoundException(studentId + " not exists"));
	}

	private Subject findSubject(@NotNull @Positive long subjectId) {
		return subjectRepo.findById(subjectId)
				.orElseThrow(() -> new NotFoundException(subjectId + " not exists"));
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
		Lecturer lecturer = lecturerRepo.findById(personDto.id())
				.orElseThrow(() -> new NotFoundException(personDto.id() + " not exist"));
		lecturer.setCity(personDto.city());
		lecturer.setPhone(personDto.phone());
		return personDto;
	}

	@Override
	@Transactional(readOnly = false)
	public PersonDto deleteLecturer(long id) {
		Lecturer lecturer = findLecturer(id);
		List<Subject> subjects = subjectRepo.findByLecturerId(id);
		subjects.forEach(lect -> lect.setLecturer(null));
		lecturerRepo.delete(lecturer);
		return lecturer.buildDto();
	}

	@Override
	@Transactional(readOnly = false)
	public SubjectDto deleteSubject(long id) {
		Subject subject = findSubject(id);
		List<Mark> listMarks = markRepo.findBySubjectId(id);
		listMarks.forEach(markRepo::delete);
		subjectRepo.delete(subject);
		return subject.buildDto();
	}

	@Override
	@Transactional(readOnly = false)
	public List<PersonDto> deleteStudentsHavingScoresLess(int nScores) {
		List<Student> deleteStudents = studentRepo.findStudentsScoresLess(nScores);
		deleteStudents.forEach(st -> {
			deleteStudent(st);
		});
		return deleteStudents.stream().map(Student::buildDto).toList();
	}

	private void deleteStudent(Student student) {
		List<Mark> mark = markRepo.findByStudentId(student.getId());
		mark.forEach(markRepo::delete);
		studentRepo.delete(student);
	}

}