package com.project4x.project4x.service;

import com.project4x.project4x.entity.Coach;
import com.project4x.project4x.entity.Member;
import com.project4x.project4x.entity.Reservation;
import com.project4x.project4x.repository.AssignedWorkoutRepository;
import com.project4x.project4x.repository.CoachRepository;
import com.project4x.project4x.repository.MemberRepository;
import com.project4x.project4x.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private AssignedWorkoutRepository assignedWorkoutRepository;



    // Check if a reservation is available on a specific date and reservation number
    public boolean isReservationAvailable(LocalDate date, int reservationNumber) {
        return !reservationRepository.existsByScheduleDateAndReservationNumber(date, reservationNumber);
    }

    // Reservation process with validation
    public ReservationDetails reserveReservation(LocalDate date, int reservationNumber, String userName) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalStateException("Booking cannot be made for past dates.");
        }

        if (!isReservationAvailable(date, reservationNumber)) {
            throw new IllegalStateException("Seat is already booked on this date.");
        }

        if (!isBookingAllowedWithinTwoWeeks(date)) {
            throw new IllegalStateException("Booking is allowed only on weekdays of this week and next week.");
        }

        if (hasReachedDailyBookingLimit(date, userName)) {
            throw new IllegalStateException("You can only book one reserve per day.");
        }

        if (hasReachedWeeklyBookingLimit(date, userName)) {
            throw new IllegalStateException("You can only book four reserves per week.");
        }

        Reservation reservation = new Reservation();
        reservation.setScheduleDate(date);
        reservation.setReservationNumber(reservationNumber);
        reservation.setUserName(userName);
        reservation.setAttendance(false);
        reservationRepository.save(reservation);

        // Retrieve the booked seat details
        return getSeatBookingDetails(date, reservationNumber);
    }

    // Get seat booking details by date and seat number
    public ReservationDetails getSeatBookingDetails(LocalDate date, int reservationNumber) {
        Reservation reservation = reservationRepository.findByScheduleDateAndReservationNumber(date, reservationNumber);
        if (reservation == null) {
            throw new IllegalStateException("No booking found for the given date and seat number.");
        }
        Member member = memberRepository.findByUserNameForGetDetails(reservation.getUserName());
        return new ReservationDetails(
                reservation.getReservationNumber(),
                reservation.getScheduleDate(),
                member.getUserName(),
                member.getFullName(),
                member.getContactNumber()
        );}






    // Check if intern has already booked a seat for the specific date
    private boolean hasReachedDailyBookingLimit(LocalDate date, String userName) {
        return reservationRepository.existsByScheduleDateAndUserName(date, userName);
    }

    // Check if intern has reached the weekly booking limit (2 seats per week)
    private boolean hasReachedWeeklyBookingLimit(LocalDate date, String userName) {
        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        List<Reservation> seatsBookedThisWeek = reservationRepository.findByUserNameAndScheduleDateBetween(userName, startOfWeek, endOfWeek);
        return seatsBookedThisWeek.size() >= 4;
    }

    // Check if the booking date is within this week or next week and only on weekdays
    private boolean isBookingAllowedWithinTwoWeeks(LocalDate date) {
        LocalDate today = LocalDate.now();

        // Get the start and end of the current week
        LocalDate startOfThisWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfThisWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

        // Get the start and end of the next week
        LocalDate startOfNextWeek = startOfThisWeek.plusWeeks(1);
        LocalDate endOfNextWeek = endOfThisWeek.plusWeeks(1);

        // Check if the date is within this week or next week and is a weekday
        boolean isWithinAllowedWeeks = (date.isAfter(today.minusDays(1)) &&
                date.isBefore(endOfNextWeek.plusDays(1)));

        boolean isWeekday = date.getDayOfWeek() != DayOfWeek.SATURDAY &&
                date.getDayOfWeek() != DayOfWeek.SUNDAY;

        return isWithinAllowedWeeks && isWeekday;
    }

    // Get booking by date
    public List<Reservation> getReservationsByDate(LocalDate date) {
        return reservationRepository.findByScheduleDate(date);
    }

    // Get all booked seats
    public List<ReservationDetails> getAllBookedSeats() {
        return reservationRepository.findAll().stream().map(reservation -> {
            Optional<Member> member = memberRepository.findByUserName(reservation.getUserName());
            if (member.isPresent()) {
                return new ReservationDetails(
                        reservation.getId(),
                        reservation.getReservationNumber(),
                        reservation.getScheduleDate(),
                        member.get().getUserName(),
                        member.get().getFullName(),
                        member.get().getContactNumber()
                );
            }
            throw new IllegalStateException("Member not found for reservation.");
        }).collect(Collectors.toList());
    }

    // Filter bookings by date
    public List<ReservationDetails> getFilterByDate(LocalDate date) {
        return reservationRepository.findByScheduleDate(date).stream().map(reservation -> {
            Optional<Member> member = memberRepository.findByUserName(reservation.getUserName());
            if (member.isPresent()) {
                return new ReservationDetails(
                        reservation.getId(),
                        reservation.getReservationNumber(),
                        reservation.getScheduleDate(),
                        member.get().getUserName(),
                        member.get().getFullName(),
                        member.get().getContactNumber()
                );
            }
            throw new IllegalStateException("Member not found for reservation.");
        }).collect(Collectors.toList());
    }


    // Filter details by username (admin dashboard)
    public List<ReservationDetails> getBookingsByUserName(String userName) {
        return reservationRepository.findByUserName(userName).stream().map(reservation -> {
            Optional<Member> member = memberRepository.findByUserName(reservation.getUserName());
            if (member.isPresent()) {
                return new ReservationDetails(
                        reservation.getId(),
                        reservation.getReservationNumber(),
                        reservation.getScheduleDate(),
                        member.get().getUserName(),
                        member.get().getFullName(),
                        member.get().getContactNumber()
                );
            }
            throw new IllegalStateException("Member not found for reservation.");
        }).collect(Collectors.toList());
    }


    //get member details with bookings
    public List<ReservationDetails> getMemberDetailsByUserNameAndId(String userName,Long Id) {
        return reservationRepository.findById(Id).stream().map(reservation -> {
            Optional<Member> member = memberRepository.findByUserName(reservation.getUserName());
            if (member.isPresent()) {
                return new ReservationDetails(
                        reservation.getId(),
                        reservation.getReservationNumber(),
                        reservation.getScheduleDate(),
                        member.get().getUserName(),
                        member.get().getFullName(),
                        member.get().getMemberId(),
                        member.get().getContactNumber(),
                        member.get().getAddress(),
                        member.get().getGender(),
                        member.get().getDob(),
                        member.get().getEmergencyContact(),
                        member.get().getMedicalConditions(),
                        member.get().getMembershipType()
                );
            }
            throw new IllegalStateException("Member not found for reservation.");
        }).collect(Collectors.toList());
    }


    //Assigned coach
    public boolean assignCoachToReservation(Long coachId, Long bookingId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(bookingId);
        Optional<Coach> coachOpt = coachRepository.findById(coachId);

        if (reservationOpt.isPresent() && coachOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            Coach coach = coachOpt.get();

            // Assign the coach to the reservation
            reservation.setAssignedCoach(coach);  // Assuming you have a field for assigned coach in Reservation
            reservationRepository.save(reservation);

            return true;
        }
        return false;
    }


    // get assigned details to display
    public Coach getAssignedCoachByBookingId(Long bookingId) {
        Reservation reservation = reservationRepository.findById(bookingId).orElse(null);
        if (reservation != null) {
            return reservation.getAssignedCoach(); // Assuming the reservation has a getAssignedCoach method
        }
        return null; // Return null if no coach is assigned
    }



    public List<Reservation> getBookingsByCoach(String coachUserName) {
        return reservationRepository.findByAssignedCoachUserName(coachUserName);
    }

    public Optional<Reservation> findByReservationNumberAndScheduleDate(Integer reservationNumber, LocalDate scheduleDate) {
        return reservationRepository.findByReservationNumberAndScheduleDate(reservationNumber, scheduleDate);
    }


    // ReservationDetails class
    public static class ReservationDetails {
        private String description;
        private int reps;
        private int sets;
        private String videoLink;
        private String workoutName;
        private Long id;
        private int reservationNumber;
        private LocalDate scheduleDate;
        private String fullName;
        private String userName;
        private String memberId;
        private String contactNumber;
        private String address;
        private String gender;
        private Date dob;
        private String emergencyContact;
        private String medicalConditions;
        private String membershipType;
        private boolean attendance = false;


        public ReservationDetails(Long id, int reservationNumber, LocalDate scheduleDate, String fullName, String userName, String memberId, String contactNumber, String address, String gender, Date dob, String emergencyContact, String medicalConditions, String membershipType) {
            this.id = id;
            this.reservationNumber = reservationNumber;
            this.scheduleDate = scheduleDate;
            this.fullName = fullName;
            this.userName = userName;
            this.memberId = memberId;
            this.contactNumber = contactNumber;
            this.address = address;
            this.gender = gender;
            this.dob = dob;
            this.emergencyContact = emergencyContact;
            this.medicalConditions = medicalConditions;
            this.membershipType = membershipType;
        }

        public ReservationDetails(Long id, int reservationNumber, LocalDate scheduleDate, String userName, String fullName, String contactNumber) {
            this.id = id;
            this.reservationNumber = reservationNumber;
            this.scheduleDate = scheduleDate;
            this.userName = userName;
            this.fullName = fullName;
            this.contactNumber = contactNumber;
        }

        public ReservationDetails(int reservationNumber, LocalDate scheduleDate, String fullName, String userName, String contactNumber) {
            this.reservationNumber = reservationNumber;
            this.scheduleDate = scheduleDate;
            this.fullName = fullName;
            this.userName = userName;
            this.contactNumber = contactNumber;
        }

        public ReservationDetails(Long id, int reservationNumber, LocalDate scheduleDate, String workoutName, int reps, int sets, String description, String videoLink) {
            this.reservationNumber = reservationNumber;
            this.scheduleDate = scheduleDate;
            this.id = id;
            this.workoutName = workoutName;
            this.reps = reps;
            this.sets = sets;
            this.description = description;
            this.videoLink = videoLink;
        }

        // Getters and setters


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

        public String getWorkoutName() {
            return workoutName;
        }

        public void setWorkoutName(String workoutName) {
            this.workoutName = workoutName;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public int getReservationNumber() {
            return reservationNumber;
        }

        public void setReservationNumber(int reservationNumber) {
            this.reservationNumber = reservationNumber;
        }

        public LocalDate getScheduleDate() {
            return scheduleDate;
        }

        public void setScheduleDate(LocalDate scheduleDate) {
            this.scheduleDate = scheduleDate;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Date getDob() {
            return dob;
        }

        public void setDob(Date dob) {
            this.dob = dob;
        }

        public String getEmergencyContact() {
            return emergencyContact;
        }

        public void setEmergencyContact(String emergencyContact) {
            this.emergencyContact = emergencyContact;
        }

        public String getMedicalConditions() {
            return medicalConditions;
        }

        public void setMedicalConditions(String medicalConditions) {
            this.medicalConditions = medicalConditions;
        }

        public String getMembershipType() {
            return membershipType;
        }

        public void setMembershipType(String membershipType) {
            this.membershipType = membershipType;
        }

        public boolean isAttendance() {
            return attendance;
        }

        public void setAttendance(boolean attendance) {
            this.attendance = attendance;
        }


        // Constructors for other details (omitted for brevity)
    }
}
