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

import ua.com.userdb.model.DBUserAccess;
import ua.com.userdb.service.DBUserAccessService;
import ua.com.userdb.service.DBUserService;
import ua.com.userdb.service.DatabaseService;

@Controller
@RequestMapping("/dbuseraccess")
public class DBUserAccessController {

	private final DBUserAccessService dbUserAccessService;
    private final DBUserService dbUserService;
    private final DatabaseService databaseService;

    public DBUserAccessController(DBUserAccessService dbUserAccessService,
                                  DBUserService dbUserService,
                                  DatabaseService databaseService) {
        this.dbUserAccessService = dbUserAccessService;
        this.dbUserService = dbUserService;
        this.databaseService = databaseService;
    }

    @GetMapping
    public String listDBUserAccess(Model model) {
        List<DBUserAccess> accesses = dbUserAccessService.findAllDBUserAccess();
        model.addAttribute("dbUserAccessList", accesses);
        return "dbuseraccess/list"; // -> templates/dbuseraccess/list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("dbUserAccess", new DBUserAccess());
        model.addAttribute("dbUsers", dbUserService.findAll());
        model.addAttribute("databases", databaseService.findAllDatabase());
        return "dbuseraccess/create"; // -> templates/dbuseraccess/create.html
    }

    @PostMapping
    public String createDBUserAccess(@ModelAttribute DBUserAccess dbUserAccess) {
        dbUserAccessService.createDBUserAccess(dbUserAccess);
        return "redirect:/dbuseraccess";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<DBUserAccess> dbUserAccess = dbUserAccessService.findDBUserAccessById(id);
        if (dbUserAccess.isPresent()) {
            model.addAttribute("dbUserAccess", dbUserAccess.get());
            model.addAttribute("dbUsers", dbUserService.findAll());
            model.addAttribute("databases", databaseService.findAllDatabase());
            return "dbuseraccess/edit"; // -> templates/dbuseraccess/edit.html
        } else {
            return "redirect:/dbuseraccess";
        }
    }

    @PostMapping("/update/{id}")
    public String updateDBUserAccess(@PathVariable Integer id, @ModelAttribute DBUserAccess dbUserAccess) {
        dbUserAccessService.updateDBUserAccess(id, dbUserAccess);
        return "redirect:/dbuseraccess";
    }

    @GetMapping("/delete/{id}")
    public String deleteDBUserAccess(@PathVariable Integer id) {
        dbUserAccessService.deleteDBUserAccess(id);
        return "redirect:/dbuseraccess";
    }
}
