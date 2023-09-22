package com.example.OnlineExam.test;

import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.service.ClassService;
import com.example.OnlineExam.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
@RestController
public class test {
    PasswordEncoder passwordEncoder;
    UserService userService;
    ClassService classService;
    public test(PasswordEncoder passwordEncoder, UserService userService, ClassService classService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.classService = classService;
    }


    @GetMapping("/student")
    public String  user(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return "Witaj, " + username + "!";
    }
    @GetMapping("/test")
    public String  test(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getAuthorities().stream().map(Object::toString).collect(Collectors.joining());
        return "Witaj, " + username + "!";
    }

    @GetMapping("/teacher")
    public String  teacher(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return "Witaj, " + username + "!";
    }
    @PostMapping("/addUserToClass")
    public String addUserToClass(@RequestParam String username, @RequestParam String className){

        classService.saveUserToClass(username,className);
        return "dodano";
    }
    @PostMapping("/saveUser")
    public String addUserToClass(@RequestParam String username, @RequestParam String password,@RequestParam String name,@RequestParam String surname,@RequestParam String email,@RequestParam(required=false) String classname ){
        User user = new User(username,passwordEncoder.encode(password),true,name,surname,email);
        userService.saveUser(user);

        if(classname !=null){
            classService.saveUserToClass(username,classname);
        }
        return "dodano";
    }

}
