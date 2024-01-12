package telran.college.entities;

import jakarta.persistence.*;
import lombok.*;
import telran.college.dto.SubjectType;

@Entity
@Table(name = "subjects")
@Getter
@NoArgsConstructor
public class Subject {
	@Id
	long id;
	@Column(nullable = false)
	String name;
	int hours;
	@ManyToOne
	@JoinColumn(name = "lecturer_id")
	Lecturer lecturer;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	SubjectType type;
}
