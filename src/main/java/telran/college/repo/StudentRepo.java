package telran.college.repo;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.college.dto.*;

import telran.college.entities.*;

public interface StudentRepo extends JpaRepository<Student, Long> {
	String JOIN_STUDENTS_MARKS = "FROM students_lecturers st JOIN marks m ON  stid=st.id ";
	String JOIN_ALL = JOIN_STUDENTS_MARKS
			+ "JOIN subjects sb ON sb.id=suid ";

	@Query(value = "SELECT st.name as name " + JOIN_ALL + "WHERE type=:type "
			+ "GROUP BY st.name ORDER BY AVG(score) DESC LIMIT :nStudents", nativeQuery = true)
	List<String> findBestStudentsSubjectType(String type, int nStudents);

	@Query(value = "SELECT st.name as name, ROUND(AVG(score)) as score "
			+ JOIN_STUDENTS_MARKS
			+ "GROUP BY st.name ORDER BY AVG(score) DESC", nativeQuery = true)
	List<NameScore> studentsMarks();

	@Query(value = "SELECT st.name, city "
			+ "FROM (SELECT * FROM students_lecturers WHERE dtype='Student') st "
			+ "LEFT JOIN marks m ON st.id = stid "
			+ "GROUP BY st.name, city HAVING COUNT(SCORE) < :scores", nativeQuery = true)
	List<StudentCity> findStudentsScoreLess(int scores);

	@Query(value = "SELECT name, phone "
			+ "FROM students_lecturers "
			+ "WHERE EXTRACT(MONTH FROM birth_date) = :month AND dtype = 'Student' ", nativeQuery = true)
	List<NamePhone> findStudentsBornMoth(int month);

	@Query(value = "SELECT name, phone "
			+ "FROM students_lecturers "
			+ "WHERE city = :city AND dtype = 'Lecturer' ", nativeQuery = true)
	List<NamePhone> findLecturersByCity(String city);

	@Query(value = "SELECT sb.name as name, score "
			+ JOIN_ALL
			+ "WHERE st.name=:name ", nativeQuery = true)
	List<NameScore> findSubjectsScoresByStudent(String name);
}
