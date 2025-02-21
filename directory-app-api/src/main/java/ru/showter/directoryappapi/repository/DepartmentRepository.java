package ru.showter.directoryappapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.showter.directoryappapi.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByIdNot(Long id);

    List<Department> findByParentId(Long parentId);
}