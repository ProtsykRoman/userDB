package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import ua.com.userdb.model.Department;
import ua.com.userdb.model.User;

public interface DepartmentService {

    List<Department> findAll();
    List<Department> getDepartmentsHierarchy();
    List<Department> getAllowedDepartmentsForUser(User user);
    Optional<Department> findDepartmentById(Integer id);
    Optional<Department> findDepartmentByName(String name);
    List<Department> findAllChildrenDepartments(Department department);
    List<Integer> getSubDepartmentIds(Integer departmentId);
    Department createDepartment(Department department);
    Optional<Department> updateDepartment(Integer id, Department department);
    boolean deleteDepartment(Integer id);
}
