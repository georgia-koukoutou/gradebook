package com.koukoutou.gradebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koukoutou.gradebook.models.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
