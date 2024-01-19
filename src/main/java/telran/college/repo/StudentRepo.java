package telran.college.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.college.dto.*;
import telran.college.entities.*;

public interface StudentRepo extends JpaRepository<Student, Long> {

	/**********************/
	@Query("SELECT student.name as name "
			+ "FROM Mark "
			+ "WHERE subject.type=:type "
			+ "GROUP BY student.name "
			+ "ORDER BY avg(score) "
			+ "DESC LIMIT :nStudents")
	List<String> findBestStudentsSubjectType(SubjectType type, int nStudents);

	/*************************/
	@Query(value = "SELECT student.name as studentName, round(avg(score)) as score "
			+ "FROM Mark "
			+ "GROUP BY student.name "
			+ "ORDER BY avg(score) "
			+ "DESC")
	List<NameScore> studentsMarks();

	/*********************************/
	@Query(value = "SELECT student.name as studentName, student.city as studentCity "
			+ "FROM Mark mark "
			+ "RIGHT JOIN mark.student student   "
			+ "GROUP BY student.name, student.city "
			+ "HAVING count(mark.score) < :scoresThreshold")
	List<StudentCity> findStudentCitiesScoresLess(int scoresThreshold);

	/*********************************/
	@Query("SELECT student "
			+ "FROM Mark mark "
			+ "RIGHT JOIN mark.student student "
			+ "GROUP BY student "
			+ "HAVING count(mark.score) < :scoresThreshold")
	List<Student> findStudentsScoresLess(int scoresThreshold);

	/*************************************/
	@Query("SELECT st.name as name, st.phone as phone "
			+ "FROM Student st "
			+ "WHERE EXTRACT(MONTH FROM st.birthDate) = :month ")
	List<NamePhone> findStudentsBurnMonth(int month);
	/************************************************/

}