package telran.college.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.college.dto.*;
import telran.college.entities.*;

public interface LecturerRepo extends JpaRepository<Lecturer, Long> {
	@Query("SELECT lecturer.name AS name, SUM(hours) AS hours "
			+ "FROM Subject "
			+ "GROUP BY lecturer.name "
			+ "ORDER BY SUM(hours) "
			+ "DESC LIMIT :nLecturers")
	List<LecturerHours> findLecturersMostHours(int nLecturers);

	
	List<NamePhone> findByCity(String city);
	
}