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

import ua.com.userdb.model.User;
import ua.com.userdb.service.DepartmenService;
import ua.com.userdb.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
    private final DepartmenService departmenService;

    public UserController(UserService userService, DepartmenService departmenService) {
        this.userService = userService;
        this.departmenService = departmenService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users/list"; // -> templates/users/list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("departments", departmenService.findAll());
        return "users/create"; // -> templates/users/create.html
    }

    @PostMapping
    public String createUser(@ModelAttribute User user) {
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }
        userService.createUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("departments", departmenService.findAll());
            return "users/edit"; // -> templates/users/edit.html
        } else {
            return "redirect:/users";
        }
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
