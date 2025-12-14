package edu.klu.it.studentrecord.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import edu.klu.it.studentrecord.config.CourseConfig;
import edu.klu.it.studentrecord.entity.*;
import edu.klu.it.studentrecord.repository.StudentRepository;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repo;
    private final CourseConfig courseConfig;

    // ✅ Constructor Injection (CORRECT)
    public StudentController(StudentRepository repo, CourseConfig courseConfig) {
        this.repo = repo;
        this.courseConfig = courseConfig;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", repo.findAll());
        model.addAttribute("showUserInfo", true);
        return "students";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("btechCourses", courseConfig.getBTechCourses());
        model.addAttribute("mtechCourses", courseConfig.getMTechCourses());
        model.addAttribute("student", new Student());
        model.addAttribute("showUserInfo", true);
        return "student-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Student student) {
        repo.save(student);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);        
        return "redirect:/students";
    }
}
