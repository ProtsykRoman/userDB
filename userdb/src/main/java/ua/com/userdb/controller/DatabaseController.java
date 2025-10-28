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

import ua.com.userdb.model.Database;
import ua.com.userdb.service.DatabaseService;

@Controller
@RequestMapping("/databases")
public class DatabaseController {
	private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping
    public String listDatabases(Model model) {
        List<Database> databases = databaseService.findAllDatabase();
        model.addAttribute("databases", databases);
        return "databases/list"; // -> templates/databases/list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("database", new Database());
        return "databases/create"; // -> templates/databases/create.html
    }

    @PostMapping
    public String createDatabase(@ModelAttribute Database database) {
        databaseService.createDatabase(database);
        return "redirect:/databases";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Database> database = databaseService.findDatabaseById(id);
        if (database.isPresent()) {
            model.addAttribute("database", database.get());
            return "databases/edit"; // -> templates/databases/edit.html
        } else {
            return "redirect:/databases";
        }
    }

    @PostMapping("/update/{id}")
    public String updateDatabase(@PathVariable Integer id, @ModelAttribute Database database) {
        databaseService.updateDatabase(id, database);
        return "redirect:/databases";
    }

    @GetMapping("/delete/{id}")
    public String deleteDatabase(@PathVariable Integer id) {
        databaseService.deleteDatabase(id);
        return "redirect:/databases";
    }
}
