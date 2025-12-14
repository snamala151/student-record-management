package edu.klu.it.studentrecord.repository;

import org.springframework.data.jpa.domain.Specification;

import edu.klu.it.studentrecord.dto.StudentReportFilterRequest;
import edu.klu.it.studentrecord.entity.*;

public class StudentReportSpecification {

    public static Specification<Student> build(StudentReportFilterRequest f) {

        return (root, query, cb) -> {

            if (f.getFilterType() == null) {
                return cb.conjunction();
            }

            return switch (f.getFilterType()) {

                case "id" ->
                    cb.equal(root.get("id"), f.getId());

                case "name" ->
                    cb.like(cb.lower(root.get("name")),
                            "%" + f.getName().toLowerCase() + "%");

                case "program" ->
                    cb.equal(root.get("programType"), f.getProgramType());

                case "course" ->
                    cb.like(cb.lower(root.get("course")),
                            "%" + f.getCourse().toLowerCase() + "%");

                case "year" ->
                    cb.between(root.get("yearOfAdmission"),
                            f.getYearFrom(), f.getYearTo());

                case "cgpa" ->
                    cb.between(root.get("cgpa"),
                            f.getCgpaMin(), f.getCgpaMax());

                default -> cb.conjunction();
            };
        };
    }
}
