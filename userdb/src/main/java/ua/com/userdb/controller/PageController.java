package ua.com.userdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/home"})
    public String home(Model model) {
    	model.addAttribute("activePage", "home");
        return "pages/home";
    }
    
}
