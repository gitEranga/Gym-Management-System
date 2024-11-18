package com.project4x.project4x.controller;

import com.project4x.project4x.entity.Member;
import com.project4x.project4x.entity.Reservation;
import com.project4x.project4x.service.MemberService;
import com.project4x.project4x.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class NotificationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/pdf")
    public String generatePdf(Model model,
                              @RequestParam Integer reservationNumber,
                              @RequestParam LocalDate scheduleDate,
                              @RequestParam String userName,
                              @RequestParam String fullName,
                              @RequestParam String contactNumber) {

        // Create a new ReservationDetails object with the passed parameters
        ReservationService.ReservationDetails reservationDetails = new ReservationService.ReservationDetails(
                reservationNumber, scheduleDate, userName, fullName, contactNumber);

        // Add the reservation details to the model
        model.addAttribute("reservation", reservationDetails);

        // Retrieve the reservation record using reservationNumber and scheduleDate
        Optional<Reservation> reservationOpt = reservationService
                .findByReservationNumberAndScheduleDate(reservationNumber, scheduleDate);

        if (reservationOpt.isPresent()) {
            // Fetch the member details using only the userName
            Optional<Member> memberOpt = memberService.findByUserName(userName);

            if (memberOpt.isPresent()) {
                // Add member details to the model
                model.addAttribute("member", memberOpt.get());
            } else {
                // Handle case where member is not found (optional)
                model.addAttribute("error", "Member details not found.");
            }
        } else {
            // Handle case where reservation is not found (optional)
            model.addAttribute("error", "Reservation not found.");
        }

        return "Member/Pdf";
    }

}
