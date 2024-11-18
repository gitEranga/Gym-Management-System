package com.project4x.project4x.DTO;

import java.time.LocalDate;

public class WorkoutWithDateDTO {
    private Long bookingId;
    private LocalDate scheduleDate;
    private String workoutName;
    private String description;
    private int reps;
    private int sets;
    private String videoLink;

    // Constructor
    public WorkoutWithDateDTO(Long bookingId, LocalDate scheduleDate, String workoutName,
                              String description, int reps, int sets, String videoLink) {
        this.bookingId = bookingId;
        this.scheduleDate = scheduleDate;
        this.workoutName = workoutName;
        this.description = description;
        this.reps = reps;
        this.sets = sets;
        this.videoLink = videoLink;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }
}
