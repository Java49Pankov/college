package telran.college.entities;

import jakarta.persistence.*;
import lombok.*;
import telran.college.dto.SubjectDto;
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
	@JoinColumn(name = "lecturer_id", nullable = true)
	Lecturer lecturer;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	SubjectType type;

	public Subject(SubjectDto subjectDto) {
		id = subjectDto.id();
		name = subjectDto.name();
		hours = subjectDto.hours();
		type = subjectDto.type();
	}

	public void setLecturer(Lecturer lecturer) {
		this.lecturer = lecturer;
	}

	public SubjectDto buildDto() {
		return new SubjectDto(id, name, hours, lecturer.id, type);
	}
}
