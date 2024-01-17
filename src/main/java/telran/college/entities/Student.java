package telran.college.entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import telran.college.dto.PersonDto;

@Entity
@NoArgsConstructor
public class Student extends Person {
	public Student(PersonDto person) {
		super(person);
	}
}
