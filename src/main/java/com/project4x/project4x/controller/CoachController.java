package com.project4x.project4x.controller;

import com.project4x.project4x.entity.Admin;
import com.project4x.project4x.entity.Coach;
import com.project4x.project4x.entity.Member;
import com.project4x.project4x.entity.Reservation;
import com.project4x.project4x.repository.CoachRepository;
import com.project4x.project4x.repository.MemberRepository;
import com.project4x.project4x.repository.ReservationRepository;
import com.project4x.project4x.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/coach")
public class CoachController {

    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationService reservationService;


    @GetMapping("/register")
    public String showRegistration(Model model) {
        model.addAttribute("coach", new Coach());
        return "Coach/cRegPage";
    }

    // Process registration
    @PostMapping("/register")
    public String processRegistration(Coach coach, String confirmPassword, Model model) {
        if (coach.getPassword().equals(confirmPassword)) {
            coachRepository.save(coach);
            return "redirect:/coach/login";
        } else {
            model.addAttribute("error", "Passwords do not match");
            return "Coach/cRegPage";
        }
    }

    // Show login page
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("coach", new Coach());
        return "Coach/loginCoach";
    }

    // Process login credentials
    @PostMapping("/login")
    public String processLogin(String userName, String password,HttpSession session, Model model) {
        Coach coach = coachRepository.findByUserName(userName);
        if (coach != null && coach.getPassword().equals(password)) {
            session.setAttribute("userName_Coach", userName);
            return "redirect:/coach/coach_dashboard";
        } else {
            model.addAttribute("error", "Invalid Username or Password");
            model.addAttribute("coach", new Coach());

            return "Coach/loginCoach";
        }
    }



    //show dashboard
    @GetMapping("/coach_dashboard")
    public String showDashboard(Model model, HttpSession session) {
        String userName_Coach = (String) session.getAttribute("userName_Coach");

        if (userName_Coach != null) {
            model.addAttribute("userName_Coach", userName_Coach);

            // Fetch the coach entity
            Coach coach = coachRepository.findByUserName(userName_Coach);

            if (coach != null) {
                // Fetch bookings assigned to the coach
                List<Reservation> bookings = reservationRepository.findByAssignedCoach(coach);

                // Sort bookings by booking id in descending order
                bookings.sort(Comparator.comparing(Reservation::getId).reversed());

                model.addAttribute("bookings", bookings);
            }
        }

        return "Coach/dashboard_coach";
    }


    // Get details of member
    @PostMapping("/member_history")
    public String memberDetails(@RequestParam("userName") String userName,
                                @RequestParam("id") Long id,
                                Model model) {
        List<ReservationService.ReservationDetails> members = reservationService.getMemberDetailsByUserNameAndId(userName, id);
        model.addAttribute("members", members);

        return "Coach/member_history";
    }

}
