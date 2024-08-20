package uz.pdp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uz.pdp.model.Theater;
import uz.pdp.service.RoomService;

import java.util.List;

@Controller
public class TheatherController {

    private final RoomService roomService;

    public TheatherController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/admin/showTheaters")
    public String showTheatersPage(Model model) {
        List<Theater> theatersList = roomService.getAll();
        model.addAttribute("theaters", theatersList);
        return "admin/showTheater";
    }

}
