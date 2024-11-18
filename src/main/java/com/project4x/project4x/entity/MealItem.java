package com.project4x.project4x.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class MealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "mealItem",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonBackReference
    private List<MealPlanItem> mealPlanItems;

    @ManyToOne
    @JoinColumn(name="meal_category_id",nullable = false)
    @JsonManagedReference
    private MealCategory mealCategory;

}
