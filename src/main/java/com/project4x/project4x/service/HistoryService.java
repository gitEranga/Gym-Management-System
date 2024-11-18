package com.project4x.project4x.service;

import com.project4x.project4x.repository.MemberRepository;
import com.project4x.project4x.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;



    public List<HistoryDetails> getPastBookingsByUserName(String userName) {
        LocalDate today = LocalDate.now();
        return reservationRepository.findByUserName(userName).stream()
                .filter(reservation -> !reservation.getScheduleDate().isAfter(today))  // Includes today and past dates
                .map(reservation -> new HistoryDetails(
                        reservation.getId(),
                        reservation.getReservationNumber(),
                        reservation.getScheduleDate(),
                        reservation.isAttendance()))  // Assuming Seat has a method isAttendance()
                .collect(Collectors.toList());
    }

    public List<HistoryService.HistoryDetails> getPastBookingsByDate(LocalDate date, String userName) {
        LocalDate today = LocalDate.now();
        return reservationRepository.findByScheduleDate(date).stream()
                .filter(reservation -> reservation.getUserName().equals(userName) && !reservation.getScheduleDate().isAfter(today))  // Includes today and past dates
                .map(reservation -> new HistoryService.HistoryDetails(
                        reservation.getId(),
                        reservation.getReservationNumber(),
                        reservation.getScheduleDate(),
                        reservation.isAttendance()))
                .collect(Collectors.toList());
    }



    public List<HistoryDetails> getFutureBookingsByUserName(String userName) {
        LocalDate today = LocalDate.now();
        return reservationRepository.findByUserName(userName).stream()
                .filter(reservation -> reservation.getScheduleDate().isAfter(today))
                .map(reservation -> new HistoryDetails(
                        reservation.getId(),
                        reservation.getReservationNumber(),
                        reservation.getScheduleDate()))  // Attendance is null for future bookings
                .collect(Collectors.toList());
    }

    public List<HistoryDetails> getFutureBookingsByDate(LocalDate date, String userName) {
        LocalDate today = LocalDate.now();
        return reservationRepository.findByScheduleDate(date).stream()
                .filter(reservation -> reservation.getUserName().equals(userName) && reservation.getScheduleDate().isAfter(today))
                .map(reservation -> new HistoryDetails(
                        reservation.getId(),
                        reservation.getReservationNumber(),
                        reservation.getScheduleDate()))  // Attendance is irrelevant for future bookings
                .collect(Collectors.toList());
    }


    public static class HistoryDetails {
        private Long id;
        private int reservationNumber;
        private LocalDate scheduleDate;
        private boolean attendance;

        // Constructor for past bookings
        public HistoryDetails(Long id, int reservationNumber, LocalDate scheduleDate, boolean attendance) {
            this.id = id;
            this.reservationNumber = reservationNumber;
            this.scheduleDate = scheduleDate;
            this.attendance = attendance;
        }

        // Constructor for future bookings
        public HistoryDetails(Long id, int reservationNumber, LocalDate scheduleDate) {
            this.id = id;
            this.reservationNumber = reservationNumber;
            this.scheduleDate = scheduleDate;
        }

        public Long getId() { return id; }

        public int getReservationNumber() {
            return reservationNumber;
        }

        public LocalDate getScheduleDate() {
            return scheduleDate;
        }

        public boolean isAttendance() {
            return attendance;
        }
    }
}
