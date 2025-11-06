package ua.com.userdb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.userdb.dao.DepartmentRepository;
import ua.com.userdb.model.Department;

@Service
public class DepartmentServiceImpl implements DepartmentService{
	private DepartmentRepository departmentRepository;
	
	public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	@Override
	public List<Department> findAll() {
		return departmentRepository.findAll();
	}
	
	public List<Department> getDepartmentsHierarchy() {
	    List<Department> allDepartments = findAll();
	    List<Department> sorted = new ArrayList<>();
	    buildHierarchy(null, allDepartments, 0, sorted);
	    return sorted;
	}

	@Override
	public Optional<Department> findDepartmentById(Integer id) {
		return departmentRepository.findById(id);
	}

	@Override
	public Optional<Department> findDepartmentByName(String name) {
		return departmentRepository.findByName(name);
	}

	@Override
	public List<Department> findAllChildrenDepartments(Department department) {
		return departmentRepository.findByParent(department);
	}

	@Override
	public Department createDepartment(Department department) {
		return departmentRepository.save(department);
	}

	@Override
	public Optional<Department> updateDepartment(Integer id, Department department) {
		Optional<Department> exitingDepartment = departmentRepository.findById(id);
		if(exitingDepartment.isPresent()) {
			Department updated = exitingDepartment.get();
			updated.setChildren(department.getChildren());
			updated.setName(department.getName());
			updated.setParent(department.getParent());
			departmentRepository.save(updated);
			return Optional.of(updated);
		}
		return Optional.empty();
	}

	@Override
	public boolean deleteDepartment(Integer id) {
		if(departmentRepository.existsById(id)) {
			departmentRepository.deleteById(id);
			return true;
		}else {
			return false;
		}
	}
	
	private void buildHierarchy(Department parent, List<Department> all, int level, List<Department> sorted) {
	    for (Department dept : all) {
	        if ((parent == null && dept.getParent() == null) || 
	            (dept.getParent() != null && dept.getParent().equals(parent))) {
	            dept.setLevel(level); // Додайте поле level у Department
	            sorted.add(dept);
	            buildHierarchy(dept, all, level + 1, sorted);
	        }
	    }
	}

}
