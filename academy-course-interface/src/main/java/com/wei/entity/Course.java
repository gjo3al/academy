package com.wei.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="course")
public class Course {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="desciption")
	private String desciption;
	
	@ManyToOne
	@JoinColumn(name="instructor_id")
	private Users instructor;
	
	@ManyToMany
	@JoinTable(
			name="course_student",
			joinColumns=@JoinColumn(name="student_id"),
			inverseJoinColumns=@JoinColumn(name="course_id")
			)
	private List<Users> students;
	
	public Course() {
	}

	public Course(int id, String name, String desciption, Users instructor) {
		this.id = id;
		this.name = name;
		this.desciption = desciption;
		this.instructor = instructor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public Users getInstructor() {
		return instructor;
	}

	public void setInstructor(Users instructor) {
		this.instructor = instructor;
	}

	public List<Users> getStudents() {
		return students;
	}

	public void setStudents(List<Users> students) {
		this.students = students;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", desciption=" + desciption + "]";
	}
	
}
