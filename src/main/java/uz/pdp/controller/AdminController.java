package uz.pdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.daos.ScreenDao;
import uz.pdp.model.*;
import uz.pdp.service.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Controller
public class AdminController {

    private final UserService userService;
    private final TicketService ticketService;


    @Autowired
    public AdminController(UserService userService,   TicketService ticketService) {

        this.userService = userService;

        this.ticketService = ticketService;

    }


    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("page", "ADMIN PAGE");
        return "admin/admin";
    }


    @GetMapping("/admin/profile")
    public String profilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        AuthUser user = userService.getByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        List<Tickets> userTickets = ticketService.getByUserId(user.getId());
        model.addAttribute("userTickets", userTickets);

        if ("ADMIN".equals(user.getRole())) {
            return "admin/profile";
        } else {
            return "userProfile";
        }
    }


    @GetMapping("/admin/showUsers")
    public String showUsersPage(Model model) {
        List<AuthUser> users = userService.findByRole("USER");
        model.addAttribute("users", users);
        return "admin/showUsers";
    }



}



