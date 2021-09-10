package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("journalist").password("{noop}journalist").roles("JOURNALIST")
                .and()
                .withUser("admin").password("{noop}admin").roles("ADMIN", "JOURNALIST");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api","/api/**").hasRole("JOURNALIST")
                .antMatchers(HttpMethod.POST,"/api", "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/api","/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/api","/api/**").hasRole("ADMIN")
                .anyRequest().authenticated().and().formLogin().and().httpBasic();

    }


}
