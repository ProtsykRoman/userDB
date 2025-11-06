package ua.com.userdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ua.com.userdb.model.Rank;
import ua.com.userdb.service.RankService;

@Controller
@RequestMapping("/ranks")
public class RankController {

    private final RankService rankService;

    public RankController(RankService rankService) {
        this.rankService = rankService;
    }

    // 🧾 Список усіх рангів
    @GetMapping
    public String listRanks(Model model) {
        List<Rank> ranks = rankService.findAll();
        model.addAttribute("ranks", ranks);
        model.addAttribute("activePage", "ranks");
        return "pages/ranks/list"; // -> templates/ranks/list.html
    }

    // 🆕 Форма створення нового
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("rank", new Rank());
        return "pages/ranks/form"; // єдина форма
    }

    // ✏️ Форма редагування
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Rank> rank = rankService.findRankById(id);
        if (rank.isPresent()) {
            model.addAttribute("rank", rank.get());
            return "pages/ranks/form";
        } else {
            return "redirect:/ranks";
        }
    }

    // 💾 Створити новий
    @PostMapping
    public String createRank(@ModelAttribute Rank rank) {
        rankService.createRank(rank);
        return "redirect:/ranks";
    }

    // ♻️ Оновити існуючий
    @PostMapping("/update/{id}")
    public String updateRank(@PathVariable Integer id, @ModelAttribute Rank rank) {
        rankService.updateRank(id, rank);
        return "redirect:/ranks";
    }

    // 🗑️ Видалити
    @GetMapping("/delete/{id}")
    public String deleteRank(@PathVariable Integer id) {
        rankService.deleteRank(id);
        return "redirect:/ranks";
    }
}
