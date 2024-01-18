package telran.college.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.college.entities.*;

public interface SubjectRepo extends JpaRepository<Subject, Long> {
	List<Subject> findByLecturerId(long id);
}
