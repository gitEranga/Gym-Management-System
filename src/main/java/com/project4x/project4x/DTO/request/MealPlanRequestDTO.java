package com.project4x.project4x.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MealPlanRequestDTO {
    private Long memberId;
    private List<MealPlanItemRequestDTO> mealPlanItems;
}

