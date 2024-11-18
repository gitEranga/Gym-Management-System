package com.project4x.project4x.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="member_id", nullable=false)
    @JsonIgnoreProperties("mealPlan")
    private Member member;

    @OneToMany(mappedBy = "mealPlan",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MealPlanItem> mealPlanItems;

}
