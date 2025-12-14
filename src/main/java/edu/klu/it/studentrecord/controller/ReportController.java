package edu.klu.it.studentrecord.controller;

import edu.klu.it.studentrecord.dto.StudentReportFilterRequest;
import edu.klu.it.studentrecord.entity.Student;
import edu.klu.it.studentrecord.entity.StudentPerformance;
import edu.klu.it.studentrecord.repository.StudentRepository;
import edu.klu.it.studentrecord.repository.StudentPerformanceRepository;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final StudentRepository studentRepository;
    private final StudentPerformanceRepository studentPerformanceRepository;

    public ReportController(StudentRepository studentRepository,
                            StudentPerformanceRepository studentPerformanceRepository) {
        this.studentRepository = studentRepository;
        this.studentPerformanceRepository = studentPerformanceRepository;
    }

    /* =====================================================
       1️⃣ INITIAL PAGE LOAD — NO DATA
       ===================================================== */
    @GetMapping
    public String reportsPage(Model model) {
        model.addAttribute("filter", new StudentReportFilterRequest());
        model.addAttribute("showResults", false);
        model.addAttribute("showUserInfo", true);
        return "reports";
    }

    /* =====================================================
       2️⃣ SEARCH — STRICT FILTERING
       ===================================================== */
    @PostMapping("/search")
    public String search(
            @ModelAttribute("filter") StudentReportFilterRequest filter,
            Model model) {

        // 🚫 No filter → no results
        if (filter == null || filter.getFilterType() == null || filter.getFilterType().isBlank()) {
            model.addAttribute("students", List.of());
            model.addAttribute("showResults", true);
            return "reports";
        }

        List<Student> filteredStudents = studentRepository.findAll()
                .stream()
                .filter(buildStudentPredicate(filter))
                .collect(Collectors.toList());

        model.addAttribute("students", filteredStudents);
        model.addAttribute("filter", filter);
        model.addAttribute("showResults", true);
        model.addAttribute("queryString", buildQueryString(filter));
        model.addAttribute("showUserInfo", true);

        return "reports";
    }
    private String buildQueryString(StudentReportFilterRequest f) {

        StringBuilder qs = new StringBuilder();

        if (f.getFilterType() != null)
            qs.append("filterType=").append(f.getFilterType());

        if (f.getId() != null)
            qs.append("&id=").append(f.getId());

        if (f.getName() != null && !f.getName().isBlank())
            qs.append("&name=").append(f.getName());

        if (f.getProgramType() != null && !f.getProgramType().isBlank())
            qs.append("&programType=").append(f.getProgramType());

        if (f.getCourse() != null && !f.getCourse().isBlank())
            qs.append("&course=").append(f.getCourse());

        if (f.getYearFrom() != null)
            qs.append("&yearFrom=").append(f.getYearFrom());

        if (f.getYearTo() != null)
            qs.append("&yearTo=").append(f.getYearTo());

        return qs.toString();
    }

    

    /* =====================================================
       3️⃣ DOWNLOAD — SAME FILTER, SAME DATA
       ===================================================== */
    @GetMapping("/download")
    public void download(
            @ModelAttribute StudentReportFilterRequest filter,
            HttpServletResponse response) throws Exception {

        // 🚫 Block download without filter
        if (filter == null || filter.getFilterType() == null || filter.getFilterType().isBlank()) {
            return;
        }

        // 1️⃣ Filter students FIRST
        List<Student> filteredStudents = studentRepository.findAll()
                .stream()
                .filter(buildStudentPredicate(filter))
                .toList();

        if (filteredStudents.isEmpty()) {
            return;
        }

        // 2️⃣ Collect allowed student IDs
        List<Long> allowedStudentIds = filteredStudents.stream()
                .map(Student::getId)
                .toList();

        // 3️⃣ Fetch ONLY performances for filtered students
        List<StudentPerformance> filteredPerformances =
                studentPerformanceRepository.findAll()
                        .stream()
                        .filter(p -> allowedStudentIds.contains(p.getStudent().getId()))
                        .toList();

        if (filteredPerformances.isEmpty()) {
            return;
        }

        // 4️⃣ Prepare Excel
        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=student-performance-report.xlsx");

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Student Performance");

        // 5️⃣ Header
        Row header = sheet.createRow(0);
        String[] cols = {
                "Academic Year",
                "Semester",
                "Student ID",
                "Student Name",
                "CGPA"
        };

        for (int i = 0; i < cols.length; i++) {
            header.createCell(i).setCellValue(cols[i]);
        }

        // 6️⃣ Rows — STRICTLY FILTERED
        int rowIdx = 1;
        for (StudentPerformance p : filteredPerformances) {

            Student s = p.getStudent();
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(p.getAcademicYear());
            row.createCell(1).setCellValue(p.getSemester());
            row.createCell(2).setCellValue(s.getId());
            row.createCell(3).setCellValue(s.getName());
            row.createCell(4).setCellValue(p.getCgpa());
        }

        for (int i = 0; i < cols.length; i++) {
            sheet.autoSizeColumn(i);
        }

        wb.write(response.getOutputStream());
        wb.close();
    }

    /* =====================================================
       4️⃣ STRICT STUDENT FILTER (NO LEAKS)
       ===================================================== */
    private Predicate<Student> buildStudentPredicate(StudentReportFilterRequest f) {

        // 🚫 No filter → no student allowed
        if (f == null || f.getFilterType() == null || f.getFilterType().isBlank()) {
            return s -> false;
        }

        String type = f.getFilterType().toLowerCase();

        return switch (type) {

            case "id" -> {
                if (f.getId() == null) yield s -> false;
                yield s -> s.getId().equals(f.getId());
            }

            case "name" -> {
                if (f.getName() == null || f.getName().isBlank()) yield s -> false;
                yield s -> s.getName() != null &&
                        s.getName().toLowerCase()
                         .contains(f.getName().toLowerCase());
            }

            case "program" -> {
                if (f.getProgramType() == null || f.getProgramType().isBlank())
                    yield s -> false;
                yield s -> s.getProgramType() != null &&
                        s.getProgramType().equalsIgnoreCase(f.getProgramType());
            }

            case "course" -> {
                if (f.getCourse() == null || f.getCourse().isBlank())
                    yield s -> false;
                yield s -> s.getCourse() != null &&
                        s.getCourse().toLowerCase()
                         .contains(f.getCourse().toLowerCase());
            }

            case "year" -> {
                if (f.getYearFrom() == null || f.getYearTo() == null)
                    yield s -> false;
                yield s -> s.getYearOfAdmission() >= f.getYearFrom()
                        && s.getYearOfAdmission() <= f.getYearTo();
            }

            default -> s -> false;
        };
    }
}
