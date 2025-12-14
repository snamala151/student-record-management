package edu.klu.it.studentrecord.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:courses.properties")
public class CourseConfig {

    @Autowired
    private Environment env; 

    public List<String> getBTechCourses() {
        return Arrays.asList(env.getProperty("courses.btech").split(","));
    }

    public List<String> getMTechCourses() {
        return Arrays.asList(env.getProperty("courses.mtech").split(","));
    }
}