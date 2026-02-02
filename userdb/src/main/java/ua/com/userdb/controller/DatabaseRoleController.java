package ua.com.userdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.userdb.model.Database;
import ua.com.userdb.model.DatabaseRole;
import ua.com.userdb.service.DatabaseRoleService;
import ua.com.userdb.service.DatabaseService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/database-roles")
public class DatabaseRoleController {

    private final DatabaseRoleService databaseRoleService;
    private final DatabaseService databaseService;

    public DatabaseRoleController(DatabaseRoleService databaseRoleService, DatabaseService databaseService) {
        this.databaseRoleService = databaseRoleService;
        this.databaseService = databaseService;
    }

    @GetMapping
    public String listDatabaseRoles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model
    ) {
        List<DatabaseRole> all = databaseRoleService.findAllDatabaseRole();

        int totalRecords = all.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        int fromIndex = Math.min((page - 1) * size, totalRecords);
        int toIndex = Math.min(fromIndex + size, totalRecords);

        List<DatabaseRole> pageList = all.subList(fromIndex, toIndex);

        model.addAttribute("databaseRoles", pageList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("activePage", "database-roles");

        return "pages/databaseRoles/list";
    }


    @GetMapping("/new")
    public String showForm(Model model) {
        DatabaseRole databaseRole = new DatabaseRole();
        databaseRole.setDatabase(new Database()); // для binding
        model.addAttribute("databaseRole", databaseRole);
        model.addAttribute("databases", databaseService.findAllDatabase());
        return "pages/databaseRoles/form";
    }

    @GetMapping("/edit/{id}")
    public String showForm(@PathVariable Integer id, Model model) {
        Optional<DatabaseRole> databaseRoleOpt = databaseRoleService.findDatabaseRoleById(id);
        if (databaseRoleOpt.isPresent()) {
            model.addAttribute("databaseRole", databaseRoleOpt.get());
            model.addAttribute("databases", databaseService.findAllDatabase());
            return "pages/databaseRoles/form";
        }
        return "redirect:/database-roles";
    }

    @PostMapping
    public String saveDatabaseRole(@ModelAttribute DatabaseRole databaseRole, Model model) {
        if (databaseRole.getDatabase() == null || databaseRole.getDatabase().getId() == 0) {
            model.addAttribute("error", "Будь ласка, виберіть базу даних");
            model.addAttribute("databases", databaseService.findAllDatabase());
            return "pages/databaseRoles/form";
        }

        Database db = databaseService.findDatabaseById(databaseRole.getDatabase().getId())
                .orElseThrow(() -> new IllegalArgumentException("База даних не знайдена"));
        databaseRole.setDatabase(db);
        databaseRoleService.createDatabaseRole(databaseRole);
        return "redirect:/database-roles";
    }

    @PostMapping("/update/{id}")
    public String updateDatabaseRole(@PathVariable Integer id,
                                     @ModelAttribute DatabaseRole databaseRole,
                                     Model model) {
        if (databaseRole.getDatabase() == null || databaseRole.getDatabase().getId() == 0) {
            model.addAttribute("error", "Будь ласка, виберіть базу даних");
            model.addAttribute("databases", databaseService.findAllDatabase());
            return "pages/databaseRoles/form";
        }

        Database db = databaseService.findDatabaseById(databaseRole.getDatabase().getId())
                .orElseThrow(() -> new IllegalArgumentException("База даних не знайдена"));
        databaseRole.setDatabase(db);

        // Викликаємо сервіс, який оновлює об’єкт за id
        databaseRoleService.updateDatabaseRole(id, databaseRole);
        return "redirect:/database-roles";
    }


    @GetMapping("/delete/{id}")
    public String deleteDatabaseRole(@PathVariable Integer id) {
        databaseRoleService.deleteDatabaseRole(id);
        return "redirect:/database-roles";
    }
}
