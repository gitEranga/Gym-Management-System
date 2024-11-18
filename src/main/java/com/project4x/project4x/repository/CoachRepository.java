package com.project4x.project4x.repository;

import com.project4x.project4x.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {

    Coach findByUserName(String userName);

    // Search coaches by userName (case-insensitive)
    List<Coach> findByUserNameContainingIgnoreCase(String userName);
}
