package telran.college.repo;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.college.dto.*;
import telran.college.entities.*;

public interface MarkRepo extends JpaRepository<Mark, Long> {

	List<SubjectNameScore> findByStudentName(String studentName);

	@Query("SELECT student.name as name "
			+ "FROM Mark "
			+ "WHERE subject.type=:type "
			+ "GROUP BY student.name "
			+ "ORDER BY avg(score) "
			+ "DESC LIMIT :nStudents")
	List<String> findBestStudentsSubjectType(SubjectType type, int nStudents);

	@Query("SELECT st.name as name, st.city as city "
			+ "FROM Mark m "
			+ "RIGHT JOIN m.student st "
			+ "GROUP BY st.name, city "
			+ "HAVING count(m.score) < :scoresThreshold")
	List<StudentCity> findStudentsCityScoresLess(int scoresThreshold);

	@Query("SELECT student.name as name, round(avg(score)) as score "
			+ "FROM Mark "
			+ "GROUP BY student.name "
			+ "ORDER BY avg(score) "
			+ "DESC ")
	List<NameScore> studentsMarks();

	List<Mark> findBySubjectId(long id);

	List<Mark> findByStudentId(long id);

}
