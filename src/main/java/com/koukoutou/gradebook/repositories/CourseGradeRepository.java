package com.koukoutou.gradebook.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koukoutou.gradebook.models.CourseGrade;

public interface CourseGradeRepository extends JpaRepository<CourseGrade, Long> {

	List<CourseGrade> findByUserId(long id);

	void deleteByCourseIdAndUserId(Long courseId, long userId);

}
