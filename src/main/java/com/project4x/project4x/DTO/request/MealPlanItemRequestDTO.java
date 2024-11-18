package com.project4x.project4x.DTO.request;

import com.project4x.project4x.utils.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MealPlanItemRequestDTO {
    private String amount;
    private MealType mealType;
    private Long mealItemId; // Reference to the meal item

    // Getters and setters
}
