package ua.com.userdb.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.userdb.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer>{
	Optional<Department> findByName(String name);
	List<Department> findAllChildrenDepartments(Department department);
}
