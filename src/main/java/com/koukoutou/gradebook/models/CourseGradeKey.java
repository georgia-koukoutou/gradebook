package com.koukoutou.gradebook.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseGradeKey implements Serializable {

	private static final long serialVersionUID = -5319537398378186831L;

	@Column(name = "user_id")
	Long userId;

	@Column(name = "course_id")
	Long courseId;
}
