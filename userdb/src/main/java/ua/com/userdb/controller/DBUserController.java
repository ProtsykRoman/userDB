package ua.com.userdb.controller;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.userdb.model.*;
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
    private final UserService userService;

    public DBUserController(DBUserService dbUserService,
                            RankService rankService,
                            DepartmentService departmentService,
                            DatabaseService databaseService,
                            DatabaseRoleService databaseRoleService,
                            CertificateTypeService certificateTypeService,
                            ObjectMapper objectMapper,
                            UserService userService) {
        this.dbUserService = dbUserService;
        this.rankService = rankService;
        this.departmentService = departmentService;
        this.databaseService = databaseService;
        this.databaseRoleService = databaseRoleService;
        this.certificateTypeService = certificateTypeService;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    /* =========================================================
       LIST
       ========================================================= */

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String listDBUsers(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String rank,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String identificationNumber,
            @RequestParam(required = false) String isActive,
            @RequestParam(required = false) Integer page,
            @RequestParam(defaultValue = "20") int size,
            Model model
    ) {

        if (page == null) {
            page = 1;
        }

        User currentUser = userService
                .findUserByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Department> allowedDepartments =
                departmentService.getAllowedDepartmentsForUser(currentUser);

        model.addAttribute("allRanks", rankService.findAll());
        model.addAttribute("allDepartments", allowedDepartments);

        List<DBUser> users = dbUserService.findAll();

        if (currentUser.getRole() != Role.ADMIN) {

            List<Integer> allowedDeptIds =
                    departmentService.getSubDepartmentIds(
                            currentUser.getDepartment().getId()
                    );

            users = users.stream()
                    .filter(u -> u.getDepartment() != null &&
                            allowedDeptIds.contains(u.getDepartment().getId()))
                    .collect(Collectors.toList());
        }

        if (name != null && !name.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getName() != null &&
                            u.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (rank != null && !rank.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getRank() != null &&
                            u.getRank().getName().equals(rank))
                    .collect(Collectors.toList());
        }

        if (department != null && !department.isEmpty()) {
            try {
                Integer deptId = Integer.parseInt(department);

                if (currentUser.getRole() != Role.ADMIN &&
                        allowedDepartments.stream().noneMatch(d -> d.getId().equals(deptId))) {
                    return "redirect:/dbusers";
                }

                users = users.stream()
                        .filter(u -> u.getDepartment() != null &&
                                u.getDepartment().getId().equals(deptId))
                        .collect(Collectors.toList());
            } catch (NumberFormatException ignored) {}
        }

        if (identificationNumber != null && !identificationNumber.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getIdentificationNumber() != null &&
                            String.valueOf(u.getIdentificationNumber()).contains(identificationNumber))
                    .collect(Collectors.toList());
        }

        if (isActive != null && !isActive.isEmpty()) {
            boolean active = Boolean.parseBoolean(isActive);
            users = users.stream()
                    .filter(u -> Boolean.TRUE.equals(u.getIsActive()) == active)
                    .collect(Collectors.toList());
        }

        int totalRecords = users.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        int fromIndex = Math.min((page - 1) * size, totalRecords);
        int toIndex = Math.min(fromIndex + size, totalRecords);

        model.addAttribute("dbUsers", users.subList(fromIndex, toIndex));
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("activePage", "dbUsers");

        return "pages/dbuser/list";
    }

    /* =========================================================
       EDIT
       ========================================================= */

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String showEditForm(@PathVariable Integer id,
                               @RequestParam(required = false) String returnUrl,
                               @RequestParam(required = false) Integer page,
                               @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                               Model model) throws JsonProcessingException {

        User currentUser = userService
                .findUserByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<DBUser> dbUserOpt = dbUserService.findById(id);
        if (dbUserOpt.isEmpty()) {
            return "redirect:/dbusers";
        }

        DBUser dbUser = dbUserOpt.get();

        model.addAttribute("dbUser", dbUser);
        model.addAttribute("readOnly", currentUser.getRole() == Role.USER);
        model.addAttribute("currentPage", page);

        if (returnUrl != null && !returnUrl.isBlank()) {
            model.addAttribute("returnUrl", returnUrl);
        } else if (page != null) {
            model.addAttribute("returnUrl", "/dbusers?page=" + page);
        }

        addFormAttributes(model, dbUser, currentUser);

        return "pages/dbuser/form";
    }

    /* =========================================================
       UPDATE
       ========================================================= */

    @PostMapping("/update/{id}")
    public String updateDBUser(@PathVariable Integer id,
                               @ModelAttribute DBUser dbUser,
                               @RequestParam(required = false) String returnUrl,
                               @RequestParam(required = false) Integer page,
                               @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                               Model model) throws JsonProcessingException {

        User currentUser = userService
                .findUserByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<DBUser> existing = dbUserService.findById(id);

        if (existing.isPresent()) {

            Long oldNumber = existing.get().getIdentificationNumber();
            Long newNumber = dbUser.getIdentificationNumber();

            if (newNumber != null &&
                    !Objects.equals(oldNumber, newNumber) &&
                    dbUserService.existsByIdentificationNumber(newNumber)) {

                model.addAttribute("dbUser", dbUser);
                model.addAttribute("error",
                        "Інший користувач вже має такий ідентифікаційний номер");

                addFormAttributes(model, dbUser, currentUser);

                return "pages/dbuser/form";
            }
        }

        dbUserService.updateDBUser(id, dbUser);

        if (returnUrl != null && returnUrl.startsWith("/")) {
            return "redirect:" + returnUrl;
        }

        if (page != null) {
            return "redirect:/dbusers?page=" + page;
        }

        return "redirect:/dbusers";
    }

    /* =========================================================
       DELETE
       ========================================================= */

    @GetMapping("/delete/{id}")
    public String deleteDBUser(@PathVariable Integer id) {
        dbUserService.deleteDBUser(id);
        return "redirect:/dbusers";
    }

    /* =========================================================
       FORM HELPERS
       ========================================================= */

    private void addFormAttributes(Model model,
                                   DBUser dbUser,
                                   User currentUser) throws JsonProcessingException {

        model.addAttribute("ranks", rankService.findAll());

        List<Department> allowedDepartments =
                departmentService.getAllowedDepartmentsForUser(currentUser);

        model.addAttribute("allDepartments",
                mergeActiveWithSelected(
                        allowedDepartments,
                        dbUser.getDepartment(),
                        Department::getIsActive
                ));

        model.addAttribute("databases", databaseService.findAllDatabase());
        model.addAttribute("databaseRoles", databaseRoleService.findAllDatabaseRole());
        model.addAttribute("certificateTypes", certificateTypeService.findAllCertificateType());

        model.addAttribute("databasesJson", objectMapper.writeValueAsString(databaseService.findAllDatabase()));
        model.addAttribute("databaseRolesJson", objectMapper.writeValueAsString(databaseRoleService.findAllDatabaseRole()));
        model.addAttribute("certificateTypesJson", objectMapper.writeValueAsString(certificateTypeService.findAllCertificateType()));
    }

    private <T> List<T> mergeActiveWithSelected(
            List<T> allItems,
            T selectedItem,
            Predicate<T> activePredicate) {

        List<T> result = allItems.stream()
                .filter(activePredicate)
                .collect(Collectors.toList());

        if (selectedItem != null &&
                !activePredicate.test(selectedItem) &&
                !result.contains(selectedItem)) {
            result.add(selectedItem);
        }

        return result;
    }
}