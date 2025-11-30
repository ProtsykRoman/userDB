package ua.com.userdb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.userdb.model.CertificateType;
import ua.com.userdb.model.DBUser;
import ua.com.userdb.model.DBUserAccess;
import ua.com.userdb.model.DBUserCertificate;
import ua.com.userdb.model.DBUserRole;
import ua.com.userdb.model.Database;
import ua.com.userdb.model.DatabaseRole;
import ua.com.userdb.model.Department;
import ua.com.userdb.model.Rank;
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
    public String listDBUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String rank,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String identificationNumber,
            @RequestParam(required = false) String isActive,
            Model model
    ) {
        List<DBUser> users = dbUserService.findAll();

        if (name != null && !name.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getName() != null &&
                            u.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (rank != null && !rank.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getRank() != null &&
                            u.getRank().getName().toLowerCase().contains(rank.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (department != null && !department.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getDepartment() != null &&
                            u.getDepartment().getName().toLowerCase().contains(department.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (identificationNumber != null && !identificationNumber.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getIdentificationNumber() != null &&
                            String.valueOf(u.getIdentificationNumber())
                                    .contains(identificationNumber))
                    .collect(Collectors.toList());
        }

        if (isActive != null && !isActive.isEmpty()) {
            boolean active = Boolean.parseBoolean(isActive);
            users = users.stream()
                    .filter(u -> Boolean.TRUE.equals(u.getIsActive()) == active)
                    .collect(Collectors.toList());
        }

        model.addAttribute("dbUsers", users);
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
        addFormAttributes(model, dbUser);
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
            addFormAttributes(model, dbUser);
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

    private void addFormAttributes(Model model, DBUser dbUser) throws JsonProcessingException {
    	List<Rank> ranks = mergeActiveWithSelected(
    	        rankService.findAll(),
    	        dbUser.getRank(),
    	        Rank::getIsActive
    	);
    	model.addAttribute("ranks", ranks);
    	
    	List<Department> departments = mergeActiveWithSelected(
    	        departmentService.findAll(),
    	        dbUser.getDepartment(),
    	        Department::getIsActive
    	);
        model.addAttribute("allDepartments", departments);
        
        Database selectedDatabase = dbUser.getDbUserAccesses().stream()
                .map(DBUserAccess::getDatabase)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        List<Database> databases = mergeActiveWithSelected(
                databaseService.findAllDatabase(),
                selectedDatabase,
                Database::getIsActive
        );
        model.addAttribute("databases", databases);
        model.addAttribute("databasesJson", objectMapper.writeValueAsString(databases));
        
        DatabaseRole selectedDatabaseRole = dbUser.getDbUserRoles().stream()
                .map(DBUserRole::getDatabaseRole)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        List<DatabaseRole> databaseRoles = mergeActiveWithSelected(
        		databaseRoleService.findAllDatabaseRole(),
        		selectedDatabaseRole,
        		DatabaseRole::getIsActive
        );
        model.addAttribute("databaseRoles", databaseRoles);
        model.addAttribute("databaseRolesJson", objectMapper.writeValueAsString(databaseRoles));

        CertificateType selectedCertificateType = dbUser.getDbUserCertificates().stream()
                .map(DBUserCertificate::getCertificateType)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        List<CertificateType> certificateTypes = mergeActiveWithSelected(
        		certificateTypeService.findAllCertificateType(),
        		selectedCertificateType,
        		CertificateType::getIsActive
        );
        model.addAttribute("certificateTypes", certificateTypes);
        model.addAttribute("certificateTypesJson", objectMapper.writeValueAsString(certificateTypes));

    }
    
    private <T> List<T> mergeActiveWithSelected(
            List<T> allItems,
            T selectedItem,
            Predicate<T> activePredicate
    ) {
        List<T> result = allItems.stream()
                .filter(activePredicate)
                .collect(Collectors.toList());

        if (selectedItem != null && !activePredicate.test(selectedItem)) {
            if (!result.contains(selectedItem)) {
                result.add(selectedItem);
            }
        }

        return result;
    }
}
