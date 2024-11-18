package com.project4x.project4x.DTO.response;

import com.project4x.project4x.utils.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MealPlanItemResponseDTO {

    private Long id;
    private String amount;
    private MealType mealType;
    private Long mealItemId;
    private String mealItemName;

}
