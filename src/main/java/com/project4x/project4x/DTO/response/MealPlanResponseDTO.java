package com.project4x.project4x.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MealPlanResponseDTO {
    private Long id;
    private Long memberId;
    private List<MealPlanItemResponseDTO> mealPlanItems;
}

