package com.example.OnlineExam.service;

import com.example.OnlineExam.exception.SchoolClassNotFoundException;
import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.SchoolClassRepository;
import com.example.OnlineExam.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	SchoolClassRepository schoolClassRepository;
	@Autowired
	UserRepository userRepository;
	@Test
	public void removeUserFromClass() {
		User user = userRepository.getUserByUsername("name3")
				.orElseGet(() -> new User("name3", "password", true, "name", "surname", "email"));

		userService.saveUserToClass("name3","4B");
		assertEquals("4B",user.getSchoolClass().getName());//Wyrzuca ze user.SchoolClass.getName() jest nullem

		userService.removeUserFromClass("name3","4B");
		assertNull(user.getSchoolClass());
	}
	@Test
	void saveUserFromClass(){
		User user = userRepository.getUserByUsername("name3")
				.orElseGet(() -> new User("name3", "password", true, "name", "surname", "email"));

		userService.saveUserToClass("name3","4B");
		userService.removeUserFromClass("name3","4B");
		assertNull(user.getSchoolClass());
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
		User user = userRepository.getUserByUsername("name3")
				.orElseGet(() -> new User("name3", "password", true, "name", "surname", "email"));
		userService.saveUser(user);
		userService.saveUser(user);

		//userService.addUserRole("name3","ROLE_TEACHER");

		//assertEquals("ROLE_TEACHER",user.getRoles().get(0).getAuthority());
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
