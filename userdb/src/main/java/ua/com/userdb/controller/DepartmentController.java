package ua.com.userdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ua.com.userdb.model.Department;
import ua.com.userdb.service.DepartmentService;

@Controller
@RequestMapping("/departments")
@PreAuthorize("hasRole('ADMIN')")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listDepartments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model
    ) {
        List<Department> all = departmentService.getDepartmentsHierarchy(); // щоб враховувати ієрархію

        int totalRecords = all.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        int fromIndex = Math.min((page - 1) * size, totalRecords);
        int toIndex = Math.min(fromIndex + size, totalRecords);

        List<Department> pageList = all.subList(fromIndex, toIndex);

        model.addAttribute("departments", pageList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("activePage", "departments");

        return "pages/departments/list";
    }


    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("allDepartments", departmentService.getDepartmentsHierarchy());
        return "pages/departments/form";
    }

    @GetMapping("/edit/{id}")
    public String showForm(@PathVariable Integer id, Model model) {
        Optional<Department> departmentOpt = departmentService.findDepartmentById(id);
        if (departmentOpt.isPresent()) {
            Department department = departmentOpt.get();
            model.addAttribute("department", department);

            // Виключаємо поточний відділ зі списку, щоб не можна було вибрати себе як батька
            List<Department> hierarchy = departmentService.getDepartmentsHierarchy();
            hierarchy.removeIf(d -> d.getId().equals(id));
            model.addAttribute("allDepartments", hierarchy);

            return "pages/departments/form";
        } else {
            return "redirect:/departments";
        }
    }

    @PostMapping
    public String saveDepartment(@ModelAttribute Department department) {
        setParent(department);
        departmentService.createDepartment(department);
        return "redirect:/departments";
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable Integer id, @ModelAttribute Department department) {
        setParent(department);
        departmentService.updateDepartment(id, department);
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }

    // -------------------------
    // Допоміжні методи
    // -------------------------
    private void setParent(Department department) {
        if (department.getParent() != null && department.getParent().getId() != 0) {
            department.setParent(
                departmentService.findDepartmentById(department.getParent().getId()).orElse(null)
            );
        } else {
            department.setParent(null);
        }
    }
}
