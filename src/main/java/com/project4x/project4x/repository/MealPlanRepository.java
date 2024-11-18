package com.project4x.project4x.repository;

import com.project4x.project4x.entity.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
MealPlan findMealPlanByMemberId(Long memberId);
}
