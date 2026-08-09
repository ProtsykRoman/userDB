package ua.com.userdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.userdb.model.DBUserRole;
import ua.com.userdb.service.DBUserRoleService;
import ua.com.userdb.service.DBUserService;
import ua.com.userdb.service.DatabaseRoleService;

@Controller
@RequestMapping("/dbuserroles")
@PreAuthorize("hasRole('ADMIN')")
public class DBUserRoleController {
	private final DBUserRoleService dbUserRoleService;
    private final DBUserService dbUserService;
    private final DatabaseRoleService databaseRoleService;

    public DBUserRoleController(DBUserRoleService dbUserRoleService,
                                DBUserService dbUserService,
                                DatabaseRoleService databaseRoleService) {
        this.dbUserRoleService = dbUserRoleService;
        this.dbUserService = dbUserService;
        this.databaseRoleService = databaseRoleService;
    }

    @GetMapping
    public String listDBUserRoles(Model model) {
        List<DBUserRole> roles = dbUserRoleService.findAllDbUserRoles();
        model.addAttribute("dbUserRoles", roles);
        return "dbuserroles/list"; // templates/dbuserroles/list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("dbUserRole", new DBUserRole());
        model.addAttribute("dbUsers", dbUserService.findAll());
        model.addAttribute("databaseRoles", databaseRoleService.findAllDatabaseRole());
        return "dbuserroles/create"; // templates/dbuserroles/create.html
    }

    @PostMapping
    public String createDBUserRole(@ModelAttribute DBUserRole dbUserRole) {
        dbUserRoleService.createDBDBUserRole(dbUserRole);
        return "redirect:/dbuserroles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<DBUserRole> role = dbUserRoleService.findDBUserRoleById(id);
        if (role.isPresent()) {
            model.addAttribute("dbUserRole", role.get());
            model.addAttribute("dbUsers", dbUserService.findAll());
            model.addAttribute("databaseRoles", databaseRoleService.findAllDatabaseRole());
            return "dbuserroles/edit"; // templates/dbuserroles/edit.html
        } else {
            return "redirect:/dbuserroles";
        }
    }

    @PostMapping("/update/{id}")
    public String updateDBUserRole(@PathVariable Integer id, @ModelAttribute DBUserRole dbUserRole) {
        dbUserRoleService.updateDBUserRole(id, dbUserRole);
        return "redirect:/dbuserroles";
    }

    @GetMapping("/delete/{id}")
    public String deleteDBUserRole(@PathVariable Integer id) {
        dbUserRoleService.deleteDBUserRole(id);
        return "redirect:/dbuserroles";
    }
}
