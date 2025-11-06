package ua.com.userdb.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String databases(Model model) {
        model.addAttribute("databases", databaseService.findAllDatabase());
        model.addAttribute("activePage", "databases");
        return "pages/databases/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("database", new Database());
        return "pages/databases/form";
    }

    @PostMapping
    public String createDatabase(@ModelAttribute Database database) {
        databaseService.createDatabase(database);
        System.out.println(database);
        return "redirect:/databases";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Database> db = databaseService.findDatabaseById(id);
        if (db.isPresent()) {
            model.addAttribute("database", db.get());
            return "pages/databases/form";
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
