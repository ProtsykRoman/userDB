package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import ua.com.userdb.model.Department;

public interface DepartmenService {
	List<Department> findAll();
	Optional<Department> findDepartmenById(Integer id);
	Optional<Department> findDepartmentByName(String name);
	List<Department> findAllChildrenDepartments(Department department);
	Department createDepartment(Department department);
	Optional<Department> updateDepartment(Integer id, Department department);
	boolean deleteDepartment(Integer id);
}
