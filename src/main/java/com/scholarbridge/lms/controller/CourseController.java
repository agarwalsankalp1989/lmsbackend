package com.scholarbridge.lms.controller;

import com.scholarbridge.lms.model.Course;
import com.scholarbridge.lms.repository.CourseRepository;
import com.scholarbridge.lms.service.ObjectStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ObjectStorageService objectStorageService;

    @PostMapping("/admin/courses")
    public ResponseEntity<Course> createCourse(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file) throws Exception {
        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        String fileUrl = objectStorageService.uploadFile(file);
        course.setFileUrl(fileUrl);
        return ResponseEntity.ok(courseRepository.save(course));
    }

    @GetMapping("/student/courses")
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }
}