package com.wayis.classrooms.springboot.studentrestapi.controller;

import com.wayis.classrooms.springboot.studentrestapi.model.Course;
import com.wayis.classrooms.springboot.studentrestapi.service.StudentService;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;
	
	//Route permettant de récupérer un étudiant avec sa liste de cours
    @GetMapping("/students/{studentId}/courses")
    public List<Course> retrieveCoursesForStudent(@PathVariable String studentId) {
        return studentService.retrieveCourses(studentId);
    }

    @GetMapping("/students/{studentId}/courses/{courseId}")
    public Course retrieveDetailsForCourse(@PathVariable String studentId,
            @PathVariable String courseId) {
        return studentService.retrieveCourse(studentId, courseId);
    }

    @PostMapping("/students/{studentId}/courses")
    public ResponseEntity<Void> registerStudentForCourse(
            @PathVariable String studentId, @RequestBody Course newCourse) {
        Course course = studentService.addCourse(studentId, newCourse);

        if (course == null) {
            return ResponseEntity.noContent().build();
        }
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(course.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
    
    //Route permettant de supprimer un cours à un étudiant
    @DeleteMapping("/students/{studentId}/courses/{courseId}")
    public boolean deleteCourseForAStudent(
            @PathVariable String studentId, @PathVariable String courseId) {
        return studentService.deleteCourse(studentId, courseId);
    }
    
    //Route permettant de récupérer un étudiant avec sa liste de cours
    @GetMapping("/students/{studentId}")
    public List<Student> retrieveAllAStudentWithAllCourses(@PathVariable String studentId) {
        return studentService.retrieveCourses(studentId);
    }
    
    //Route permettant de récupérer la liste des étudiants
	@GetMapping("/students")
	public List<Student> retrieveAllStudents() {
		return studentService.retrieveAllStudents();
		}
		
    //Route permettant d’ajouter un étudiant à la liste des étudiants
    @PostMapping("/students")
    public ResponseEntity<Void> addStudentToStudentsList(@RequestBody Student newStudent) {
        Student student = studentService.addStudent(newStudent);

        if (student == null) {
            return ResponseEntity.noContent().build();
        }
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/").buildAndExpand(course.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
    
}
