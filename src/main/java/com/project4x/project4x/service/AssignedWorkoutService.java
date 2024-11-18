package com.project4x.project4x.service;

import com.project4x.project4x.entity.AssignedWorkout;
import com.project4x.project4x.repository.AssignedWorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignedWorkoutService {

    @Autowired
    private AssignedWorkoutRepository assignedWorkoutRepository;

    public AssignedWorkoutService(AssignedWorkoutRepository assignedWorkoutRepository) {
        this.assignedWorkoutRepository = assignedWorkoutRepository;
    }

    public List<AssignedWorkout> saveAll(List<AssignedWorkout> workouts) {
        return assignedWorkoutRepository.saveAll(workouts);
    }


}