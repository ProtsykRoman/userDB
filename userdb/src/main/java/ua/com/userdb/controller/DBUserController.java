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

import ua.com.userdb.model.DBUser;
import ua.com.userdb.service.DBUserService;

@Controller
@RequestMapping("/dbusers")
public class DBUserController {
	private final DBUserService dbUserService;

    public DBUserController(DBUserService dbUserService) {
        this.dbUserService = dbUserService;
    }

    @GetMapping
    public String listDBUsers(Model model) {
        List<DBUser> dbUsers = dbUserService.findAll();
        model.addAttribute("dbUsers", dbUsers);
        return "dbusers/list"; // -> templates/dbusers/list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("dbUser", new DBUser());
        return "dbusers/create"; // -> templates/dbusers/create.html
    }

    @PostMapping
    public String createDBUser(@ModelAttribute DBUser dbUser) {
        dbUserService.createDBUser(dbUser);
        return "redirect:/dbusers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<DBUser> dbUser = dbUserService.findById(id);
        if (dbUser.isPresent()) {
            model.addAttribute("dbUser", dbUser.get());
            return "dbusers/edit"; // -> templates/dbusers/edit.html
        } else {
            return "redirect:/dbusers";
        }
    }

    @PostMapping("/update/{id}")
    public String updateDBUser(@PathVariable Integer id, @ModelAttribute DBUser dbUser) {
        dbUserService.updateDBUser(id, dbUser);
        return "redirect:/dbusers";
    }

    @GetMapping("/delete/{id}")
    public String deleteDBUser(@PathVariable Integer id) {
        dbUserService.deleteDBUser(id);
        return "redirect:/dbusers";
    }
}
