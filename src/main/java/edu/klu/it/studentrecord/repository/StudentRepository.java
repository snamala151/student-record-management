
package edu.klu.it.studentrecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import edu.klu.it.studentrecord.entity.*;

@Repository
public interface StudentRepository
extends JpaRepository<Student, Long>,
        JpaSpecificationExecutor<Student> {
}
