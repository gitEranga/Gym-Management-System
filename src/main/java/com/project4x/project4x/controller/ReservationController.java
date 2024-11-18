package com.project4x.project4x.controller;

import com.project4x.project4x.entity.Reservation;
import com.project4x.project4x.repository.ReservationRepository;
import com.project4x.project4x.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkSeatAvailability(@RequestParam LocalDate date, @RequestParam int reservationNumber) {
        boolean available = reservationService.isReservationAvailable(date, reservationNumber);
        return ResponseEntity.ok(available);
    }

    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> bookSeat(@RequestParam LocalDate date,
                                                        @RequestParam int reservationNumber,
                                                        HttpSession session) {
        String userName = (String) session.getAttribute("userName");
        Map<String, Object> response = new HashMap<>();

        if (userName != null) {
            try {
                reservationService.reserveReservation(date, reservationNumber, userName);
                response.put("success", true);
                response.put("message", "Seat successfully booked.");
                return ResponseEntity.ok(response);
            } catch (IllegalStateException e) {
                response.put("success", false);
                response.put("message", e.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
        }

        response.put("success", false);
        response.put("message", "User not logged in.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @GetMapping("/date")
    public ResponseEntity<List<Reservation>> getReservationsByDate(@RequestParam("date") String date) {
        LocalDate scheduleDate = LocalDate.parse(date);
        List<Reservation> reservations = reservationRepository.findByScheduleDate(scheduleDate);
        return ResponseEntity.ok(reservations);
    }
}
