package com.koukoutou.gradebook.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.koukoutou.gradebook.models.CourseGrade;
import com.koukoutou.gradebook.models.CourseGradeKey;

public interface CourseGradeRepository extends CrudRepository<CourseGrade, CourseGradeKey> {

	List<CourseGrade> findByUserId(long id);

	CourseGrade findByUserIdAndCourseId(long userId, long courseId);

	void deleteByCourseIdAndUserId(Long courseId, long userId);

	@Modifying
	@Query("update CourseGrade set grade = :grade where course_id = :course_id and user_id = :user_id")
	void updateGrade(@Param(value = "course_id") long courseId, @Param(value = "user_id") long userId,
			@Param(value = "grade") double grade);

}
