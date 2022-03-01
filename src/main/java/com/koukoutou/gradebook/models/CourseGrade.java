package com.koukoutou.gradebook.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_grade")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseGrade {

	@EmbeddedId
	private CourseGradeKey id = new CourseGradeKey();

	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@MapsId("courseId")
	@JoinColumn(name = "course_id")
	private Course course;

	@NotNull
	@DecimalMin("5.0")
	@DecimalMax("10.0")
	@Column(name = "grade", nullable = false)
	private double grade;

}
