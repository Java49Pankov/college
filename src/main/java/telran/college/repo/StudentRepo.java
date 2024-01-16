package telran.college.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.college.dto.*;
import telran.college.entities.*;

public interface StudentRepo extends JpaRepository<Student, Long> {

	@Query("SELECT st.name as name, st.phone as phone "
			+ "FROM Student st "
			+ "WHERE EXTRACT(MONTH FROM st.birthDate) = :month ")
	List<NamePhone> findStudentsBurnMonth(int month);

}