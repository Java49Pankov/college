package telran.college.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.college.dto.LecturerHours;
import telran.college.entities.*;

public interface LecturerRepo extends JpaRepository<Lecturer, Long> {

	@Query(value = "SELECT sl.name as name, SUM(hours) as hours "
			+ "FROM students_lecturers sl "
			+ "JOIN subjects sb ON sl.id = sb.lecturer_id "
			+ "GROUP BY sl.name "
			+ "ORDER BY SUM(hours) DESC LIMIT :nLectures", nativeQuery = true)
	List<LecturerHours> findLecturesMostHours(int nLectures);
}
