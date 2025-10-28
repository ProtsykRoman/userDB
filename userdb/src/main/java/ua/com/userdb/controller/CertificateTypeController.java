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

import ua.com.userdb.model.CertificateType;
import ua.com.userdb.service.CertificateTypeService;

@Controller
@RequestMapping("/certificate-types")
public class CertificateTypeController {
	private CertificateTypeService certificateTypeService;
	
	public CertificateTypeController(CertificateTypeService certificateTypeService) {
		this.certificateTypeService = certificateTypeService;
	}
	
    @GetMapping
    public String listCertificateTypes(Model model) {
        List<CertificateType> certificateTypes = certificateTypeService.findAllCertificateType();
        model.addAttribute("certificateTypes", certificateTypes);
        return "certificate-types/list";  // -> templates/certificate-types/list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("certificateType", new CertificateType());
        return "certificate-types/create"; // -> templates/certificate-types/create.html
    }

    @PostMapping
    public String createCertificateType(@ModelAttribute CertificateType certificateType) {
        certificateTypeService.createCertificateType(certificateType);
        return "redirect:/certificate-types";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<CertificateType> certificateType = certificateTypeService.findCertificateTypeById(id);
        if (certificateType.isPresent()) {
            model.addAttribute("certificateType", certificateType.get());
            return "certificate-types/edit"; // -> templates/certificate-types/edit.html
        } else {
            return "redirect:/certificate-types";
        }
    }

    @PostMapping("/update/{id}")
    public String updateCertificateType(@PathVariable Integer id,
            @ModelAttribute CertificateType certificateType) {
        certificateTypeService.updateCertificateType(id, certificateType);
        return "redirect:/certificate-types";
    }

    @GetMapping("/delete/{id}")
    public String deleteCertificateType(@PathVariable Integer id) {
        certificateTypeService.deleteCertificateType(id);
        return "redirect:/certificate-types";
    }
}
