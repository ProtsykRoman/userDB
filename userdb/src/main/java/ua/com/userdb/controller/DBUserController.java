package ua.com.userdb.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.userdb.model.DBUser;
import ua.com.userdb.service.*;

@Controller
@RequestMapping("/dbusers")
public class DBUserController {

    private final DBUserService dbUserService;
    private final RankService rankService;
    private final DepartmentService departmentService;
    private final DatabaseService databaseService;
    private final DatabaseRoleService databaseRoleService;
    private final CertificateTypeService certificateTypeService;
    private final ObjectMapper objectMapper;

    public DBUserController(DBUserService dbUserService, RankService rankService,
                            DepartmentService departmentService, DatabaseService databaseService,
                            DatabaseRoleService databaseRoleService, CertificateTypeService certificateTypeService,
                            ObjectMapper objectMapper) {
        this.dbUserService = dbUserService;
        this.rankService = rankService;
        this.departmentService = departmentService;
        this.databaseService = databaseService;
        this.databaseRoleService = databaseRoleService;
        this.certificateTypeService = certificateTypeService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String listDBUsers(Model model) {
        model.addAttribute("dbUsers", dbUserService.findAll());
        model.addAttribute("activePage", "dbUsers");
        return "pages/dbuser/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) throws JsonProcessingException {
        DBUser dbUser = new DBUser();
        dbUser.setDbUserAccesses(new ArrayList<>()); // Ініціалізація порожнього списку
        dbUser.setDbUserCertificates(new ArrayList<>()); // Ініціалізація порожнього списку
        dbUser.setDbUserRoles(new ArrayList<>()); // Ініціалізація порожнього списку
        model.addAttribute("dbUser", dbUser);
        addFormAttributes(model);
        return "pages/dbuser/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) throws JsonProcessingException {
        Optional<DBUser> dbUserOpt = dbUserService.findById(id);
        if (dbUserOpt.isPresent()) {
            DBUser dbUser = dbUserOpt.get();
            if (dbUser.getDbUserAccesses() == null) {
                dbUser.setDbUserAccesses(new ArrayList<>());
            }
            if (dbUser.getDbUserCertificates() == null) {
                dbUser.setDbUserCertificates(new ArrayList<>());
            }
            if (dbUser.getDbUserRoles() == null) {
                dbUser.setDbUserRoles(new ArrayList<>());
            }
            model.addAttribute("dbUser", dbUser);
            addFormAttributes(model);
            return "pages/dbuser/form";
        } else {
            return "redirect:/dbusers";
        }
    }

    @PostMapping
    public String createDBUser(@ModelAttribute DBUser dbUser) {
        dbUserService.createDBUser(dbUser);
        return "redirect:/dbusers";
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

    private void addFormAttributes(Model model) throws JsonProcessingException {
        model.addAttribute("ranks", rankService.findAll());
        model.addAttribute("allDepartments", departmentService.getDepartmentsHierarchy());
        
        model.addAttribute("databases", databaseService.findAllDatabase());
        model.addAttribute("databasesJson", objectMapper.writeValueAsString(databaseService.findAllDatabase()));
        
        // Для ролей бази даних
        model.addAttribute("databaseRoles", databaseRoleService.findAllDatabaseRole());
        model.addAttribute("databaseRolesJson", objectMapper.writeValueAsString(databaseRoleService.findAllDatabaseRole()));

        // Для сертифікатів
        model.addAttribute("certificateTypes", certificateTypeService.findAllCertificateType());
        model.addAttribute("certificateTypesJson", objectMapper.writeValueAsString(certificateTypeService.findAllCertificateType()));

    }
}
