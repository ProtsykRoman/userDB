package ua.com.userdb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.userdb.dao.DepartmentRepository;
import ua.com.userdb.model.Department;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // ====================== Базові CRUD ======================
    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
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
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Optional<Department> updateDepartment(Integer id, Department department) {
        Optional<Department> existingDepartment = departmentRepository.findById(id);
        if (existingDepartment.isPresent()) {
            Department updated = existingDepartment.get();
            updated.setChildren(department.getChildren());
            updated.setName(department.getName());
            updated.setParent(department.getParent());
            updated.setIsActive(department.getIsActive());
            departmentRepository.save(updated);
            return Optional.of(updated);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteDepartment(Integer id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ====================== Ієрархія ======================
    // Повертає список всіх підрозділів у відсортованій ієрархії з level
    public List<Department> getDepartmentsHierarchy() {
        List<Department> allDepartments = findAll();
        List<Department> sorted = new ArrayList<>();
        buildHierarchy(null, allDepartments, 0, sorted);
        return sorted;
    }

    private void buildHierarchy(Department parent, List<Department> all, int level, List<Department> sorted) {
        for (Department dept : all) {
            if ((parent == null && dept.getParent() == null) ||
                (dept.getParent() != null && dept.getParent().equals(parent))) {
                dept.setLevel(level); // рівень ієрархії для відображення в UI
                sorted.add(dept);
                buildHierarchy(dept, all, level + 1, sorted);
            }
        }
    }

    // ====================== Рекурсивні підпідрозділи ======================
    @Override
    public List<Department> findAllChildrenDepartments(Department department) {
        // повертає тільки прямі діти
        return departmentRepository.findByParent(department);
    }

    // Повертає всі підпідрозділи рекурсивно + сам підрозділ
    public List<Integer> getSubDepartmentIds(Integer departmentId) {
        List<Integer> ids = new ArrayList<>();
        departmentRepository.findById(departmentId).ifPresent(d -> collectSubIds(d, ids));
        return ids;
    }

    private void collectSubIds(Department dept, List<Integer> ids) {
        ids.add(dept.getId());
        for (Department child : dept.getChildren()) {
            collectSubIds(child, ids);
        }
    }
}
