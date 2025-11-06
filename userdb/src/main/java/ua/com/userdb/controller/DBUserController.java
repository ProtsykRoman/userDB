package ua.com.userdb.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ua.com.userdb.model.DBUser;
import ua.com.userdb.service.*;

@Controller
@RequestMapping("/dbusers")
public class DBUserController {

    private final DBUserService dbUserService;
    private final RankService rankService;
    private final DepartmentService departmentService;
    private final DatabaseService databaseService;

    public DBUserController(DBUserService dbUserService, 
    						RankService rankService, 
    						DepartmentService departmentService, 
    						DatabaseService databaseService) {
        this.dbUserService = dbUserService;
        this.rankService = rankService;
        this.departmentService = departmentService;
        this.databaseService = databaseService;
    }

    @GetMapping
    public String listDBUsers(Model model) {
        model.addAttribute("dbUsers", dbUserService.findAll());
        model.addAttribute("activePage", "dbUsers");
        return "pages/dbuser/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        DBUser dbUser = new DBUser();
        model.addAttribute("dbUser", dbUser);
        model.addAttribute("ranks", rankService.findAll());
        model.addAttribute("allDepartments", departmentService.getDepartmentsHierarchy());
        model.addAttribute("databases", databaseService.findAllDatabase());
        return "pages/dbuser/form";
    }

    @PostMapping
    public String createDBUser(@ModelAttribute DBUser dbUser) {
        dbUserService.createDBUser(dbUser);
        return "redirect:/dbusers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<DBUser> dbUserOpt = dbUserService.findById(id);
        if (dbUserOpt.isPresent()) {
            DBUser dbUser = dbUserOpt.get();
            model.addAttribute("dbUser", dbUser);
            model.addAttribute("ranks", rankService.findAll());
            model.addAttribute("allDepartments", departmentService.getDepartmentsHierarchy());
            model.addAttribute("databases", databaseService.findAllDatabase());

            return "pages/dbuser/form";
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
