package edu.klu.it.studentrecord.repository;

import edu.klu.it.studentrecord.entity.StudentPerformance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentPerformanceRepository
        extends JpaRepository<StudentPerformance, Long> {

    List<StudentPerformance> findByStudentId(Long studentId);
}
