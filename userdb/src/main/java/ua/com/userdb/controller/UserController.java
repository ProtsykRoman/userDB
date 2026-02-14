package ua.com.userdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.userdb.model.User;
import ua.com.userdb.model.Department;
import ua.com.userdb.model.Role;
import ua.com.userdb.service.UserService;
import ua.com.userdb.service.DepartmentService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final DepartmentService departmentService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, DepartmentService departmentService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.departmentService = departmentService;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= LIST =================
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("activePage", "users");
        return "pages/users/list";
    }

    // ================= NEW =================
    @GetMapping("/new")
    public String showNewForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("allDepartments", departmentService.getDepartmentsHierarchy());
        model.addAttribute("roles", Role.values());
        return "pages/users/form";
    }

    // ================= EDIT =================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Optional<User> userOpt = userService.findUserById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("user", user);
            model.addAttribute("allDepartments", departmentService.getDepartmentsHierarchy());
            model.addAttribute("roles", Role.values());
            return "pages/users/form";
        }
        return "redirect:/users";
    }

    // ================= SAVE NEW =================
    @PostMapping
    public String saveUser(@ModelAttribute User user, Model model) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            model.addAttribute("error", "Пароль обов'язковий");
            model.addAttribute("user", user);
            model.addAttribute("allDepartments", departmentService.getDepartmentsHierarchy());
            model.addAttribute("roles", Role.values());
            return "pages/users/form";
        }
        
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.createUser(user);
        return "redirect:/users";
    }

    // ================= UPDATE EXISTING =================
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable int id, @ModelAttribute User user, Model model) {

        User existing = userService.findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Користувач не знайдений"));

        if (user.isChangePassword()) {
            // Перевірка старого пароля
            if (!passwordEncoder.matches(user.getOldPassword(), existing.getPassword())) {
                model.addAttribute("error", "Старий пароль неправильний");
                prepareModelForError(model, user);
                return "pages/users/form";
            }
            // Перевірка нового пароля і підтвердження
            if (user.getNewPassword() == null || !user.getNewPassword().equals(user.getConfirmPassword())) {
                model.addAttribute("error", "Новий пароль і підтвердження не збігаються");
                prepareModelForError(model, user);
                return "pages/users/form";
            }
            existing.setPassword(passwordEncoder.encode(user.getNewPassword()));
        }

        existing.setUsername(user.getUsername().toLowerCase());
        existing.setRole(user.getRole());
        existing.setIsActive(user.getIsActive());
        existing.setDepartment(user.getDepartment());

        userService.createUser(existing);
        return "redirect:/users";
    }

    // ================= DELETE =================
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
    
    private void prepareModelForError(Model model, User user) {
        model.addAttribute("user", user);
        model.addAttribute("allDepartments", departmentService.getDepartmentsHierarchy());
        model.addAttribute("roles", Role.values());
    }
}
