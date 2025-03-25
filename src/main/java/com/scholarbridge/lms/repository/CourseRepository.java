package com.scholarbridge.lms.repository;

import com.scholarbridge.lms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}