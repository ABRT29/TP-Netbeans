package com.wayis.classrooms.springboot.studentrestapi.service;

import com.wayis.classrooms.springboot.studentrestapi.model.Course;
import com.wayis.classrooms.springboot.studentrestapi.model.Student;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StudentService {

    private static List<Student> students;

    static {
        initializeData();
    }

    public static void initializeData() {
        students = new ArrayList<>();
        Course course1 = new Course("DEVE709", "IDE", "Utilisation des IDE", Arrays.asList("Learn Maven", "NetBeans", "Integration tests", "Coverage"));
        Course course2 = new Course("DEVE710", "CI", "Integration Continue", Arrays.asList("Jenkins", "Coverage", "Tests"));
        Course course3 = new Course("BDOE571", "SQL", "SQL Sous Oracle", Arrays.asList("SQL", "PL/SQL", "Triggers"));
        Course course4 = new Course("DEVE571", "Java", "Les fondamentaux", Arrays.asList("Variables", "Boucles", "JVM", "Exceptions"));

        Student student1 = new Student("S-001", "Luc Labbé", "Senior Developer", new ArrayList<>(Arrays.asList(course1)));
        Student student2 = new Student("S-002", "Thomas Gallinari", "Mobile Developer", new ArrayList<>(Arrays.asList(course1)));

        students.add(student1);
        students.add(student2);
    }

    public List<Student> retrieveAllStudents() {
        return students;
    }

    public Student retrieveStudent(String studentId) {
        for (Student student : students) {
            if (student.getId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    public List<Course> retrieveCourses(String studentId) {
        Student student = retrieveStudent(studentId);

        if (student == null) {
            return null;
        }

        return student.getCourses();
    }

    public Course retrieveCourse(String studentId, String courseId) {
        Student student = retrieveStudent(studentId);

        if (student == null) {
            return null;
        }

        for (Course course : student.getCourses()) {
            if (course.getId().equals(courseId)) {
                return course;
            }
        }

        return null;
    }
	
	public Student addStudent(Student student){
		
		if (student.getId() == null){
			return null;
			}
		students.add(student);
		return student;
		}
	
	
	
    private final SecureRandom random = new SecureRandom();

    public Course addCourse(String studentId, Course course) {
        Student student = retrieveStudent(studentId);

        if (student == null) {
            return null;
        }
	
	
        String randomId = new BigInteger(130, random).toString(32);
        course.setId(randomId);

        student.getCourses().add(course);

        return course;
    }
    
    public boolean deleteCourse(String studentId, String courseId) {
        
        Student student = retrieveStudent(studentId);

        if (student == null){
            return false;
        }
            
        return student.getCourses().removeIf(course -> (course.getId().equals(courseId)));
    }
    
    
    
}
