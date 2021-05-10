package com.wayis.classrooms.springboot.studentrestapi.controller;

import com.wayis.classrooms.springboot.studentrestapi.StudentRestApiApplication;
import com.wayis.classrooms.springboot.studentrestapi.model.Course;
import java.util.Arrays;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StudentRestApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
	
    // Exercice n°1 :
	
    @Nested
    @DisplayName("GET /students/{student}/courses")
    class GetRetrieveCoursesForStudent {
		
        // Route permettant de récupérer la liste des cours d'un étudiant :
        @Test
        @DisplayName("should return student's courses")
        public void testRetrieveCoursesForStudent() throws JSONException {
            
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students/S-002/courses"),
                    HttpMethod.GET, entity, String.class);
            
            String expected = "[{\"id\":\"DEVE709\",\"name\":\"IDE\",\"description\":\"Utilisation des IDE\",\"steps\":[\"Learn Maven\",\"NetBeans\",\"Integration tests\",\"Coverage\"]}]";
            
            Assertions.assertEquals(200, response.getStatusCode().value());
            JSONAssert.assertEquals(expected, response.getBody(), true);
        }
		
	// Route de récupération de la liste des cours dans le cas ou l'étudiant est null :
	@Test
        @DisplayName("shouldn't return student's courses when the student is unknown")
        public void testRetrieveCoursesWhenStudentIsUnknown() throws JSONException
        {
            
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students/any/courses"),
                    HttpMethod.GET, entity, String.class);
            
            String expected = "[{\"id\":\"DEVE709\",\"name\":\"IDE\",\"description\":\"Utilisation des IDE\",\"steps\":[\"Learn Maven\",\"NetBeans\",\"Integration tests\",\"Coverage\"]}]";
            
            Assertions.assertEquals(200, response.getStatusCode().value());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        }
    }

    @Nested
    @DisplayName("GET /students/{student}/courses/{course}")
    class GetStudentCourse {
		
	//Route permettant de récupérer un cours en particulier de la liste des cours d'un étudiant en particulier :
        @Test
        @DisplayName("should return a JSON course object")
        public void testRetrieveStudentCourse() throws JSONException {

            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students/S-001/courses/DEVE709"),
                    HttpMethod.GET, entity, String.class);

            String expected = "{id:DEVE709,name:IDE,description:\"Utilisation des IDE\"}";

            Assertions.assertEquals(200, response.getStatusCode().value());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        }
		
	// Route renvoyant null dans le cas où l'étudiant n'existe pas :
	@Test
        @DisplayName("should return null when the student is unknown")
		public void testRetrieveStudentCourseWhenStudentUnknown() throws JSONException {

            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students/any/courses/DEVE709"),
                    HttpMethod.GET, entity, String.class);

            Assertions.assertEquals(200, response.getStatusCode().value());
            JSONAssert.assertEquals(null, response.getBody(), true);
        }
		
	// Route renvoyant null dans le cas où le cours et l'étudiant n'existent pas :
	@Test
        @DisplayName("should return null when both course and student are unknown")
		public void testRetrieveStudentCourseWhenStudentAndCourseUnknown() throws JSONException {

            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students/any/courses/any"),
                    HttpMethod.GET, entity, String.class);

            Assertions.assertEquals(200, response.getStatusCode().value());
            JSONAssert.assertEquals(null, response.getBody(), true);
        }
    }

    @Nested
    @DisplayName("POST /students/{student}/courses")
    class PostStudentCourse {
        @Test
        @DisplayName("should create a course for the user")
        public void testAddCourse() {
            Course course = new Course("Course1", "SpringBoot", "IT", Arrays.asList("Learn Maven", "Import Project", "First Example", "Second Example"));

            HttpEntity<Course> entity = new HttpEntity<>(course, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students/S-001/courses"),
                    HttpMethod.POST, entity, String.class);

            Assertions.assertEquals(201, response.getStatusCode().value());
            String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
            assertTrue(actual.contains("/students/S-001/courses/"));
        }
		
	// Je pense que pour ce test il faut plus tot essayer d'ajouter un cours à un étudiant qui n'existe pas, non pas l'inverse (qu'en penses-tu ?)
		
	@Test
        @DisplayName("shouldn't create course if user is unknown")
        public void testAddCourseWhenUserIsUnknown() {
            Student student = new Student();

            HttpEntity<Course> entity = new HttpEntity<>(student, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students/any/courses"),
                    HttpMethod.POST, entity, String.class);
					
            Assertions.assertEquals(204, response.getStatusCode().value());
        }
		
    }
    
   

	//Exercice 4 :
	
    // 1- Route permettant de supprimer un cours à un étudiant :
    @Nested
    @DisplayName("DELETE /students/{student}/courses/{course}")
    class DeleteStudentCourse {
        @Test
        @DisplayName("Should return true if student's course is correctly deleted")
        public void testDeleteCourse() {

            HttpEntity<Course> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students/S-001/courses/DEVE709"),
                    HttpMethod.DELETE, entity, String.class);
                        
            Assertions.assertEquals(200, response.getStatusCode().value());
            Assertions.assertEquals("true", response.getBody());
        }
        
        @Test
        @DisplayName("Should return false if both student and course are unknown")
        public void testDeleteStudentCourseForUnknownCourseAndStudent() {

            HttpEntity<Course> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students/any/courses/any"),
                    HttpMethod.DELETE, entity, String.class);
                        
            Assertions.assertEquals(200, response.getStatusCode().value());
            Assertions.assertEquals("false", response.getBody());
        }
    }
	
    // 2- Route permettant de récupérer un étudiant avec sa liste de cours :
    @Nested
    @DisplayName("GET /students/{studentId}")
    class GetStudentAndCourses {
    
        @Test
        @DisplayName("Should return a JSON object with the student and his courses list")
        public void testGetStudentAndCourses() throws JSONException {
            
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students/S-001/"),
                    HttpMethod.GET, entity, String.class);
            
            String expected = "{\"id\":\"S-001\",\"name\":\"Luc Labbé\",\"description\":\"Senior Developer\",\"courses\":[{\"id\":\"DEVE709\",\"name\":\"IDE\",\"description\":\"Utilisation des IDE\",\"steps\":[\"Learn Maven\",\"NetBeans\",\"Integration tests\",\"Coverage\"]},{\"id\":\"DEVE710\",\"name\":\"CI\",\"description\":\"Integration Continue\",\"steps\":[\"Jenkins\",\"Coverage\",\"Tests\"]},{\"id\":\"BDOE571\",\"name\":\"SQL\",\"description\":\"SQL Sous Oracle\",\"steps\":[\"SQL\",\"PL/SQL\",\"Triggers\"]},{\"id\":\"DEVE571\",\"name\":\"Java\",\"description\":\"Les fondamentaux\",\"steps\":[\"Variables\",\"Boucles\",\"JVM\",\"Exceptions\"]}]}";
            
            Assertions.assertEquals(200, response.getStatusCode().value());
            JSONAssert.assertEquals(expected, response.getBody(), true);
        }
		
	@Test
        @DisplayName("Should return null if the student is unknown")
        public void testGetStudentAndCoursesWhenStudentUnknown() throws JSONException {
            
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students/any"),
                    HttpMethod.GET, entity, String.class);
         
            Assertions.assertEquals(200, response.getStatusCode().value());
            JSONAssert.assertEquals(null, response.getBody(), true);
        }
        
    }
	
    // 3- Route permettant de récupérer la liste des étudiants :
    @Nested
    @DisplayName("GET /students/{studentId}")
    class GetStudentAndCourses {
    
        @Test
        @DisplayName("Should return a JSON object with the students list")
        public void testGetAllStudents() throws JSONException {
            
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students"),
                    HttpMethod.GET, entity, String.class);
					"S-002", "Thomas Gallinari", "Mobile Developer"
            
            String expected = "[{\"id\":\"S-001\",\"name\":\"Luc Labbé\",\"description\":\"Senior Developer\",\"id\":\"S-002\",\"name\":\"Thomas Gallinari\",\"description\":\"Mobile Developer\"}]";
            
            Assertions.assertEquals(200, response.getStatusCode().value());
            JSONAssert.assertEquals(expected, response.getBody(), true);
        }
		
	// (test 2 dans le cas ou le précédent ne fonctionne pas, car pour le test précédent j'ai un doute concernant le expected)	
	@Test
        @DisplayName("should return a JSON object with all students")
        public void testGetAllStudents() throws JSONException {
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students"),
                    HttpMethod.GET, entity, String.class);
            
            System.out.println(response);

            String expected = "[{\"id\":\"S-001\",\"name\":\"Luc Labbé\",\"description\":\"Senior Developer\",\"courses\":[{\"id\":\"DEVE709\",\"name\":\"IDE\",\"description\":\"Utilisation des IDE\",\"steps\":[\"Learn Maven\",\"NetBeans\",\"Integration tests\",\"Coverage\"]},{\"id\":\"DEVE710\",\"name\":\"CI\",\"description\":\"Integration Continue\",\"steps\":[\"Jenkins\",\"Coverage\",\"Tests\"]},{\"id\":\"BDOE571\",\"name\":\"SQL\",\"description\":\"SQL Sous Oracle\",\"steps\":[\"SQL\",\"PL/SQL\",\"Triggers\"]},{\"id\":\"DEVE571\",\"name\":\"Java\",\"description\":\"Les fondamentaux\",\"steps\":[\"Variables\",\"Boucles\",\"JVM\",\"Exceptions\"]}]},{\"id\":\"S-002\",\"name\":\"Thomas Gallinari\",\"description\":\"Mobile Developer\",\"courses\":[{\"id\":\"DEVE709\",\"name\":\"IDE\",\"description\":\"Utilisation des IDE\",\"steps\":[\"Learn Maven\",\"NetBeans\",\"Integration tests\",\"Coverage\"]},{\"id\":\"DEVE710\",\"name\":\"CI\",\"description\":\"Integration Continue\",\"steps\":[\"Jenkins\",\"Coverage\",\"Tests\"]},{\"id\":\"BDOE571\",\"name\":\"SQL\",\"description\":\"SQL Sous Oracle\",\"steps\":[\"SQL\",\"PL/SQL\",\"Triggers\"]},{\"id\":\"DEVE571\",\"name\":\"Java\",\"description\":\"Les fondamentaux\",\"steps\":[\"Variables\",\"Boucles\",\"JVM\",\"Exceptions\"]}]}]";

            Assertions.assertEquals(200, response.getStatusCode().value());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        } 
    } 
	
		
    // 4- Route permettant d’ajouter un étudiant à la liste des étudiants :
    @Nested
    @DisplayName("POST /students")
    class PostStudent {
        @Test
        @DisplayName("should create a student and add it to the student's array")
        public void testAddStudent() {
            Student student = new Student("S-003", "Aurélien", "Senior developer", new ArrayList<>());

            HttpEntity<Course> entity = new HttpEntity<>(student, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students"),
                    HttpMethod.POST, entity, String.class);

            Assertions.assertEquals(201, response.getStatusCode().value());
            String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
            assertTrue(actual.contains("/students"));
        }
		
	@Test
        @DisplayName("shouldn't create a student if the student is null")
		public void testAddStudentWhenStudentUnknown() {
			Student student = new Student();

            HttpEntity<Course> entity = new HttpEntity<>(student, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort("/students"),
                    HttpMethod.POST, entity, String.class);
	
	     // Je renvoie une erreur 400,car on demande d'ajouter un étudiant or ce dernier est null (je te laisse valider pour ce code d'erreur)
	    // Erreur 400 pour bad request
            Assertions.assertEquals(400, response.getStatusCode().value()); 
        }
		}
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
