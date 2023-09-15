package com.example.OnlineExam.service;

import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.SchoolClassRepository;
import com.example.OnlineExam.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	SchoolClassRepository schoolClassRepository;
	@Autowired
	UserRepository userRepository;

	@Test
	void saveUserToClass() {
		User user = new User("name", "password", true, "name", "surname", "email");
		SchoolClass schoolClass = schoolClassRepository.getSchoolClassByName("4B");


		userService.saveUserToClass(user,schoolClass);


		assertEquals("4B",user.getSchoolClass().getName());

	}
	@Test
	void saveUserToNotExistingClassShouldThrowException() {
		User user = new User("name", "password", true, "name", "surname", "email");
		SchoolClass schoolClass = schoolClassRepository.getSchoolClassByName("notExisting");

		Exception exception = assertThrows(Exception.class, () -> userService.saveUserToClass(user, schoolClass));

		assertEquals("Class not found",exception.getMessage());
	}
	@Test
	void addUserRole(){
		User user = userRepository.getUserByUserId(4);

		userService.addUserRole(user,"TEACHER");

		assertEquals("TEACHER",user.getRoles().get(0).getAuthority());
	}

}
