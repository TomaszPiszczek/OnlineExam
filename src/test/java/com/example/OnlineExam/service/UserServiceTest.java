package com.example.OnlineExam.service;

import com.example.OnlineExam.exception.SchoolClassNotFoundException;
import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.SchoolClassRepository;
import com.example.OnlineExam.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
@Transactional
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
		userService.saveUserToClass("name3","4B");

		User user = userRepository.getUserByUsername("name3").orElseThrow();

		assertEquals("4B",user.getSchoolClass().getName());
	}
	@Test
	void saveUserToNotExistingClassShouldThrowException() {
		Exception exception = assertThrows(SchoolClassNotFoundException.class,
				() -> userService.saveUserToClass("name", "NotEXISTINGCLASS"));
		assertEquals("Class not found",exception.getMessage());
	}
	@Test
	void saveNullUserClassShouldThrowException() {

		Exception exception = assertThrows(UsernameNotFoundException.class,
				() -> userService.saveUserToClass("notExistingUser", "4B"));
		assertEquals("The user with the provided username does not exist",exception.getMessage());
	}
	@Test
	void addUserRole(){
		User user = new User("name","password",true,"name","surname","email");
		if(userRepository.getUserByUsername("name").isEmpty()){
			userService.saveUser(user);

		}
		userService.addUserRole(user,"ROLE_TEACHER");

		assertEquals("ROLE_TEACHER",user.getRoles().get(0).getAuthority());
	}
	@Test
	void addUserWithoutValidCredentialsShouldThrowException(){
		User user = new User("","password",true,"name","surname","email");
		Exception exception = assertThrows(ConstraintViolationException.class,
				() -> userService.saveUser(user));
		System.out.println(exception.getMessage());

		assertThat(exception.getMessage()).contains("Username cannot be blank");
	}
	@Test
	void addTwoUserWithSameNameShouldThrowException(){
		User user = new User("name1","password",true,"name","surname","email");
		User user1 = new User("name1","password",true,"name","surname","email");


		assertThrows(DataIntegrityViolationException.class,() ->{
			userService.saveUser(user1);
			userService.saveUser(user);
		});

	}
}
