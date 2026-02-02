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

    
    @GetMapping
    public String listRanks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model
    ) {
        List<Rank> ranks = rankService.findAll();

        // пагінація
        int totalRecords = ranks.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        int fromIndex = Math.min((page - 1) * size, totalRecords);
        int toIndex = Math.min(fromIndex + size, totalRecords);

        List<Rank> pageList = ranks.subList(fromIndex, toIndex);

        model.addAttribute("ranks", pageList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("activePage", "ranks");

        return "pages/ranks/list";
    }


   
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("rank", new Rank());
        return "pages/ranks/form"; // єдина форма
    }

    
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


    @PostMapping
    public String createRank(@ModelAttribute Rank rank) {
        rankService.createRank(rank);
        return "redirect:/ranks";
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
