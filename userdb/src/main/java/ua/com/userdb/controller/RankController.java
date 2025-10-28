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

import ua.com.userdb.model.Rank;
import ua.com.userdb.service.RankService;

@Controller
@RequestMapping("/ranks")
public class RankController {
	private final RankService rankService;

    public RankController(RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping
    public String listRanks(Model model) {
        List<Rank> ranks = rankService.findAll();
        model.addAttribute("ranks", ranks);
        return "ranks/list"; // -> templates/ranks/list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("rank", new Rank());
        return "ranks/create"; // -> templates/ranks/create.html
    }

    @PostMapping
    public String createRank(@ModelAttribute Rank rank) {
        rankService.createRank(rank);
        return "redirect:/ranks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Rank> rank = rankService.findRankById(id);
        if (rank.isPresent()) {
            model.addAttribute("rank", rank.get());
            return "ranks/edit"; // -> templates/ranks/edit.html
        } else {
            return "redirect:/ranks";
        }
    }

    @PostMapping("/update/{id}")
    public String updateRank(@PathVariable Integer id, @ModelAttribute Rank rank) {
        rankService.updateRank(id, rank);
        return "redirect:/ranks";
    }

    @GetMapping("/delete/{id}")
    public String deleteRank(@PathVariable Integer id) {
        rankService.deleteRank(id);
        return "redirect:/ranks";
    }
}
