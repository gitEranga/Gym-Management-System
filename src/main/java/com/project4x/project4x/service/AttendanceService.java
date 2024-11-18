package com.project4x.project4x.service;

import com.project4x.project4x.entity.Member;
import com.project4x.project4x.entity.Reservation;
import com.project4x.project4x.repository.MemberRepository;
import com.project4x.project4x.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;



    //get all details by booked seat to the dashboard admin
    public List<AttendanceDetails> getAllBookedSeats() {
        return reservationRepository.findAll().stream().map(reservation -> {
            Member member = memberRepository.findByuserName(reservation.getUserName());
            return new AttendanceDetails(reservation.getId(), reservation.getReservationNumber(), reservation.getScheduleDate(), member.getUserName(),  reservation.isAttendance());
        }).collect(Collectors.toList());
    }


    //filter details by date (admin dashboard)
    public List<AttendanceDetails> getFilterByDate(LocalDate date) {
        return reservationRepository.findByScheduleDate(date).stream().map(reservation -> {
            Member member = memberRepository.findByuserName(reservation.getUserName());
            return new AttendanceDetails(reservation.getId(), reservation.getReservationNumber(), reservation.getScheduleDate(), member.getUserName(),  reservation.isAttendance());
        }).collect(Collectors.toList());
    }

//    //filter details by intern id (admin dashboard)
    public List<AttendanceDetails> getBookingsByUserName(String userName) {
        return reservationRepository.findByUserName(userName).stream().map(reservation -> {
            Member member = memberRepository.findByuserName(reservation.getUserName());
            return new AttendanceDetails(reservation.getId(), reservation.getReservationNumber(), reservation.getScheduleDate(), member.getUserName(),  reservation.isAttendance());
        }).collect(Collectors.toList());
    }



    public void updateAttendance(Long bookingId, boolean attendance) {
        // Find the seat by booking ID
        Reservation reservation = reservationRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));

        // Update attendance status
        reservation.setAttendance(attendance);

        // Save the updated seat entity
        reservationRepository.save(reservation);
    }



    public static class AttendanceDetails {
        private Long id;
        private int reservationNumber;
        private LocalDate scheduleDate;
        private String userName;
        private boolean attendance; // Boolean to represent checkbox (checked or not)

        // Constructor
        public AttendanceDetails(Long id, int reservationNumber, LocalDate scheduleDate, String userName, boolean attendance) {
            this.id = id;
            this.reservationNumber = reservationNumber;
            this.scheduleDate = scheduleDate;
            this.userName = userName;
            this.attendance = attendance;
        }

        // Getters and Setters
        public Long getId() { return id; }

        public void setId(Long id) {
            this.id = id;
        }

        public int getReservationNumber() {
            return reservationNumber;
        }

        public void setReservationNumber(int seatNumber) {
            this.reservationNumber = seatNumber;
        }

        public LocalDate getScheduleDate() {
            return scheduleDate;
        }

        public void setScheduleDate(LocalDate scheduleDate) {
            this.scheduleDate = scheduleDate;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public boolean isAttendance() {
            return attendance;
        }

        public void setAttendance(boolean attendance) {
            this.attendance = attendance;
        }
    }


}
