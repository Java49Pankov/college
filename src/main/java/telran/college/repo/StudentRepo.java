package telran.college.repo;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.college.dto.StudentMark;
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
	List<StudentMark> studentsMarks();
}
