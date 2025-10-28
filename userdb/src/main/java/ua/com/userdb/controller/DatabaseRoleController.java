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

import ua.com.userdb.model.DatabaseRole;
import ua.com.userdb.service.DatabaseRoleService;

@Controller
@RequestMapping("database-roles")
public class DatabaseRoleController {
	private DatabaseRoleService databaseRoleService;
	
	public DatabaseRoleController(DatabaseRoleService databaseRoleService) {
		this.databaseRoleService = databaseRoleService;
	}
	
	@GetMapping
	public String listDatabaseRoles(Model model) {
		List<DatabaseRole> databaseRoles = databaseRoleService.findAllDatabaseRole();
		model.addAttribute("databaseRoles", databaseRoles);
		return "database-roles/list"; // -> templates/database-roles/list.html
	}
	
	@GetMapping("/new")
	public String showCreateForm(Model model) {
		model.addAttribute("databaseRole", new DatabaseRole());
		return "database-roles/create"; // -> templates/database-roles/create.html
	}
	
	@PostMapping
	public String createDatabaseRole(@ModelAttribute DatabaseRole databaseRole) {
		databaseRoleService.createDatabaseRole(databaseRole);
		return "redirect:database-roles";
	}
	
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Integer id, Model model) {
		Optional<DatabaseRole> databaseRole = databaseRoleService.findDatabaseRoleById(id);
		if (databaseRole.isPresent()) {
			model.addAttribute("databaseRole", databaseRole.get());
			return "database-role/edit"; // -> templates/database-role/edit.html
		} else {
            return "redirect:/database-roles";
        }
	}
	
	@PostMapping("/update/{id}")
	public String updateDatabaseRole(@PathVariable Integer id,
			@ModelAttribute DatabaseRole databaseRole) {
		databaseRoleService.updateDatabaseRole(id, databaseRole);
		return "redirect:/database-roles";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteCertificateType(@PathVariable Integer id) {
		databaseRoleService.deleteDatabaseRole(id);
        return "redirect:/database-roles";
    } 
}
