package com.project4x.project4x.controller;

import com.project4x.project4x.DTO.request.MealPlanRequestDTO;
import com.project4x.project4x.DTO.response.MealPlanResponseDTO;
import com.project4x.project4x.service.MealPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/meal-plans")
public class MealPlanController {
    private final MealPlanService mealPlanService;

    public MealPlanController(MealPlanService mealPlanService) {
        this.mealPlanService = mealPlanService;
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MealPlanResponseDTO> getMealPlanByMemberId(@PathVariable Long memberId) {
       return new ResponseEntity<>(mealPlanService.getMealPlanByMemberId(memberId), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addMealPlan(@RequestBody MealPlanRequestDTO request) {
        mealPlanService.createMealPlan(request);
        return new ResponseEntity<>("Meal Created Successfully", HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateMealPlan(@RequestBody MealPlanRequestDTO request) {
        mealPlanService.updateMealPlan(request);
        return new ResponseEntity<>("Meal Updated Successfully", HttpStatus.OK);
    }



}
