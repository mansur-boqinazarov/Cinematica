package uz.pdp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.daos.ScreenDao;
import uz.pdp.model.Screens;
import uz.pdp.model.Theater;
import uz.pdp.service.RoomService;
import uz.pdp.service.ScreenService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ScreenController {
    private final ScreenDao screenDao;
    private final ScreenService screenService;
    private final RoomService roomService;

    public ScreenController(ScreenDao screenDao, ScreenService screenService, RoomService roomService) {
        this.screenDao = screenDao;
        this.screenService = screenService;
        this.roomService = roomService;
    }

    @PostMapping("/admin/addScreen")
    public String addScreen(@RequestParam("name") String name,
                            @RequestParam("theaterId") int theaterId,
                            @RequestParam("capacity") int capacity,
                            Model model) {
        Screens screen = new Screens();
        screen.setName(name);
        screen.setRoomId(theaterId);
        screen.setCapacity(capacity);
        screenDao.save(screen);
        model.addAttribute("message", "Screen added successfully!");
        return "redirect:/admin";
    }
    @GetMapping("/admin/addScreen")
    public String showAddScreenForm(Model model) {
        List<Theater> theaters = roomService.getAll();
        Map<Integer, String> theaterMap = theaters.stream()
                .collect(Collectors.toMap(Theater::getId, Theater::getName));
        model.addAttribute("theaterMap", theaterMap);
        return "admin/addScreens";
    }


    @GetMapping("/admin/showScreens")
    public String screens(Model model) {
        List<Screens> screens = screenService.getAll();
        List<Theater> theaters = roomService.getAll();
        Map<Integer, String> theaterMap = theaters.stream()
                .collect(Collectors.toMap(Theater::getId, Theater::getName));
        model.addAttribute("screens", screens);
        model.addAttribute("theaterMap", theaterMap);
        return "admin/showScreens";
    }

    @GetMapping("/admin/addTheater")
    public String addTheaterPage() {
        return "admin/addTheater";
    }

    @PostMapping("/admin/addTheater")
    public String addTheater(@RequestParam("name") String name, @RequestParam("location") String location, Model model) {
        Theater theater = new Theater();
        theater.setName(name);
        theater.setLocation(location);
        roomService.save(theater);
        model.addAttribute("message", "Theater added successfully!");
        return "redirect:/admin";
    }


}
