package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import ua.com.userdb.dao.DepartmentRepository;
import ua.com.userdb.model.Department;

public class DepartmenServiceImpl implements DepartmenService{
	private DepartmentRepository departmentRepository;
	
	public DepartmenServiceImpl(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	@Override
	public List<Department> findAll() {
		return departmentRepository.findAll();
	}

	@Override
	public Optional<Department> findDepartmenById(Integer id) {
		return departmentRepository.findById(id);
	}

	@Override
	public Optional<Department> findDepartmentByName(String name) {
		return departmentRepository.findByName(name);
	}

	@Override
	public List<Department> findAllChildrenDepartments(Department department) {
		return departmentRepository.findAllChildrenDepartments(department);
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

}
