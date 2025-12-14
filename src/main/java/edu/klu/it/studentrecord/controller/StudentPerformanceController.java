package edu.klu.it.studentrecord.controller;

import edu.klu.it.studentrecord.entity.Student;
import edu.klu.it.studentrecord.entity.StudentPerformance;
import edu.klu.it.studentrecord.repository.StudentPerformanceRepository;
import edu.klu.it.studentrecord.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentPerformanceController {

    private final StudentRepository studentRepository;
    private final StudentPerformanceRepository performanceRepository;

    public StudentPerformanceController(StudentRepository studentRepository,
                                        StudentPerformanceRepository performanceRepository) {
        this.studentRepository = studentRepository;
        this.performanceRepository = performanceRepository;
    }

    /* VIEW PERFORMANCE */
    @GetMapping("/{id}/performance")
    public String viewPerformance(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id).orElseThrow();
        model.addAttribute("student", student);
        model.addAttribute("performances", student.getPerformances());
        model.addAttribute("showUserInfo", true);
        return "student-performance-view";
    }

    /* SHOW ADD FORM */
    @GetMapping("/{id}/performance/add")
    public String showAddPerformance(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id).orElseThrow();
        model.addAttribute("student", student);
        model.addAttribute("showUserInfo", true);
        return "student-performance-add";
    }

    @PostMapping("/{id}/performance/add")
    public String savePerformance(@PathVariable Long id,
                                  @RequestParam Integer academicYear,
                                  @RequestParam Integer semester,
                                  @RequestParam Double cgpa) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student id"));

        StudentPerformance performance = new StudentPerformance();
        performance.setAcademicYear(academicYear);           
        performance.setSemester(semester);
        performance.setCgpa(cgpa);
        performance.setStudent(student);

        performanceRepository.save(performance);

        return "redirect:/students/" + id + "/performance";
    }
    
    @GetMapping("/{studentId}/performance/delete/{performanceId}")
    public String deletePerformance(@PathVariable Long studentId,
                                    @PathVariable Long performanceId) {

        performanceRepository.deleteById(performanceId);

        return "redirect:/students/" + studentId + "/performance";
    }
}