package com.project4x.project4x.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int reservationNumber;
    private boolean isAvailable;
    private LocalDate scheduleDate;
    private String userName;
    private boolean attendance;


    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach assignedCoach; // Field for assigned coach


    // Check if a coach is already assigned
    public boolean isCoachAssigned() {
        return assignedCoach != null;
    }


    //Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(int reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
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


    public Coach getAssignedCoach() {
        return assignedCoach;
    }

    public void setAssignedCoach(Coach assignedCoach) {
        this.assignedCoach = assignedCoach;
    }


}
