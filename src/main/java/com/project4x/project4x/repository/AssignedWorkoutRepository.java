package com.project4x.project4x.repository;

import com.project4x.project4x.entity.AssignedWorkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignedWorkoutRepository extends JpaRepository<AssignedWorkout, Long> {

    List<AssignedWorkout> findByBookingId(Long bookingId);

}
