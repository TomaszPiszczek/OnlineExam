package com.example.OnlineExam.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
//todo CSRV

@Configuration
public class Security {
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setEnableAuthorities(true);
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM public.user WHERE username = ?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT \"user\".\"username\", \"authority\".\"authority\"\n" +
                "FROM \"authority\"\n" +
                "INNER JOIN \"public\".\"user\" ON \"user\".\"user_id\" = \"authority\".\"user_id\"\n" +
                "WHERE \"user\".\"username\" = ?");

        return jdbcUserDetailsManager;

    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    //todo secure endpoints
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/student").hasRole("STUDENT")
                        .requestMatchers("/teacher").hasRole("TEACHER")
                        .requestMatchers("api/subject/**").hasRole("TEACHER")
                        .requestMatchers("api/test/createTest").hasRole("TEACHER")
                        .anyRequest().permitAll()
        );

        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);


        return http.build();
    }


}
