package ua.com.userdb.controller;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ua.com.userdb.model.Database;
import ua.com.userdb.service.DatabaseService;

@Controller
@RequestMapping("/databases")
@PreAuthorize("hasRole('ADMIN')")
public class DatabaseController {
    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping
    public String databases(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model
    ) {
        var all = databaseService.findAllDatabase();

        int totalRecords = all.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        int fromIndex = Math.min((page - 1) * size, totalRecords);
        int toIndex = Math.min(fromIndex + size, totalRecords);

        var pageList = all.subList(fromIndex, toIndex);

        model.addAttribute("databases", pageList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", totalPages);
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
