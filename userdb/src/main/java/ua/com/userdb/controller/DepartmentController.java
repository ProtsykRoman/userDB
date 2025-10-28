package ua.com.userdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.userdb.model.Department;
import ua.com.userdb.service.DepartmenService;

@Controller
@RequestMapping("/departments")
public class DepartmentController {
	private final DepartmenService departmenService;

    public DepartmentController(DepartmenService departmenService) {
        this.departmenService = departmenService;
    }

    @GetMapping
    public String listDepartments(Model model) {
        List<Department> departments = departmenService.findAll();
        model.addAttribute("departments", departments);
        return "departments/list"; // -> templates/departments/list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("allDepartments", departmenService.findAll());
        return "departments/create"; // -> templates/departments/create.html
    }

    @PostMapping
    public String createDepartment(@ModelAttribute Department department) {
        departmenService.createDepartment(department);
        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Department> department = departmenService.findDepartmenById(id);
        if (department.isPresent()) {
            model.addAttribute("department", department.get());
            model.addAttribute("allDepartments", departmenService.findAll());
            return "departments/edit"; // -> templates/departments/edit.html
        } else {
            return "redirect:/departments";
        }
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable Integer id, @ModelAttribute Department department) {
        departmenService.updateDepartment(id, department);
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Integer id) {
        departmenService.deleteDepartment(id);
        return "redirect:/departments";
    }
}
