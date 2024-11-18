package com.project4x.project4x.repository;

import com.project4x.project4x.entity.Coach;
import com.project4x.project4x.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByScheduleDate(LocalDate date);

    boolean existsByScheduleDateAndReservationNumber(LocalDate date, int reservationNumber);

    List<Reservation> findByUserName(String userName);

    boolean existsByScheduleDateAndUserName(LocalDate date, String userName);

    List<Reservation> findByUserNameAndScheduleDateBetween(String userName, LocalDate startOfWeek, LocalDate endOfWeek);

    Reservation findByScheduleDateAndReservationNumber(LocalDate date, int reservationNumber);

    List<Reservation> findByAssignedCoachId(Long assignedCoachId); // Keep this method

    List<Reservation> findByAssignedCoachUserName(String coachUserName);

    List<Reservation> findByAssignedCoach(Coach coach);

    Optional<Reservation> findTopByUserNameAndScheduleDateGreaterThanEqualOrderByScheduleDateAsc(String userName, LocalDate today);

    Optional<Reservation> findByReservationNumberAndScheduleDate(Integer reservationNumber, LocalDate scheduleDate);
}
