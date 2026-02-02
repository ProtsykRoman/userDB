package ua.com.userdb.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String certificateTypes(
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "20") int size,
	        Model model
	) {
	    var all = certificateTypeService.findAllCertificateType();

	    int totalRecords = all.size();
	    int totalPages = (int) Math.ceil((double) totalRecords / size);

	    int fromIndex = Math.min((page - 1) * size, totalRecords);
	    int toIndex = Math.min(fromIndex + size, totalRecords);

	    var pageList = all.subList(fromIndex, toIndex);

	    model.addAttribute("certificateTypes", pageList);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("pageSize", size);
	    model.addAttribute("totalRecords", totalRecords);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("activePage", "certificate-types");

	    return "pages/certificateTypes/list";
	}

	
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("certificateType", new CertificateType());
        return "pages/certificateTypes/form";
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
            return "pages/certificateTypes/form"; // -> templates/certificate-types/edit.html
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
