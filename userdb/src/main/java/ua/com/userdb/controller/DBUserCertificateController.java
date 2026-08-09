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

import ua.com.userdb.model.DBUserCertificate;
import ua.com.userdb.service.CertificateTypeService;
import ua.com.userdb.service.DBUserCertificateService;
import ua.com.userdb.service.DBUserService;

@Controller
@RequestMapping("/dbusercertificates")
@PreAuthorize("hasRole('ADMIN')")
public class DBUserCertificateController {
	private final DBUserCertificateService dbUserCertificateService;
    private final DBUserService dbUserService;
    private final CertificateTypeService certificateTypeService;

    public DBUserCertificateController(DBUserCertificateService dbUserCertificateService,
                                       DBUserService dbUserService,
                                       CertificateTypeService certificateTypeService) {
        this.dbUserCertificateService = dbUserCertificateService;
        this.dbUserService = dbUserService;
        this.certificateTypeService = certificateTypeService;
    }

    @GetMapping
    public String listDBUserCertificates(Model model) {
        List<DBUserCertificate> certificates = dbUserCertificateService.findAllDBUserCertificate();
        model.addAttribute("certificates", certificates);
        return "dbusercertificates/list"; // templates/dbusercertificates/list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("dbUserCertificate", new DBUserCertificate());
        model.addAttribute("dbUsers", dbUserService.findAll());
        model.addAttribute("certificateTypes", certificateTypeService.findAllCertificateType());
        return "dbusercertificates/create"; // templates/dbusercertificates/create.html
    }

    @PostMapping
    public String createDBUserCertificate(@ModelAttribute DBUserCertificate dbUserCertificate) {
        dbUserCertificateService.createDBUserCertificate(dbUserCertificate);
        return "redirect:/dbusercertificates";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<DBUserCertificate> certificate = dbUserCertificateService.findDBUserCertificateById(id);
        if (certificate.isPresent()) {
            model.addAttribute("dbUserCertificate", certificate.get());
            model.addAttribute("dbUsers", dbUserService.findAll());
            model.addAttribute("certificateTypes", certificateTypeService.findAllCertificateType());
            return "dbusercertificates/edit"; // templates/dbusercertificates/edit.html
        } else {
            return "redirect:/dbusercertificates";
        }
    }

    @PostMapping("/update/{id}")
    public String updateDBUserCertificate(@PathVariable Integer id, @ModelAttribute DBUserCertificate dbUserCertificate) {
        dbUserCertificateService.updateDBUserCertificate(id, dbUserCertificate);
        return "redirect:/dbusercertificates";
    }

    @GetMapping("/delete/{id}")
    public String deleteDBUserCertificate(@PathVariable Integer id) {
        dbUserCertificateService.deleteDBUserCertificate(id);
        return "redirect:/dbusercertificates";
    }
}
