package com.project4x.project4x.service;
import com.project4x.project4x.DTO.request.MealPlanItemRequestDTO;
import com.project4x.project4x.DTO.request.MealPlanRequestDTO;
import com.project4x.project4x.DTO.response.MealPlanItemResponseDTO;
import com.project4x.project4x.DTO.response.MealPlanResponseDTO;
import com.project4x.project4x.entity.MealItem;
import com.project4x.project4x.entity.MealPlan;
import com.project4x.project4x.entity.MealPlanItem;
import com.project4x.project4x.entity.Member;
import com.project4x.project4x.repository.MealItemRepository;
import com.project4x.project4x.repository.MealPlanRepository;
import com.project4x.project4x.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealPlanService {
    private final MealPlanRepository mealPlanRepository;
    private final MemberRepository memberRepository;
    private final MealItemRepository mealItemRepository;

    public MealPlanService(MealPlanRepository mealPlanRepository, MemberRepository memberRepository, MealItemRepository mealItemRepository) {
        this.mealPlanRepository = mealPlanRepository;
        this.memberRepository = memberRepository;
        this.mealItemRepository = mealItemRepository;
    }

    public MealPlanResponseDTO getMealPlanByMemberId(Long memberId) {
        MealPlan mealPlan =  mealPlanRepository.findMealPlanByMemberId(memberId);

        if (mealPlan == null) {
            throw new IllegalArgumentException("Meal plan not found for member ID: " + memberId);
        }

        return mapMealPlanToResponseDTO(mealPlan);
    }

    private MealPlanResponseDTO mapMealPlanToResponseDTO(MealPlan mealPlan) {
        List<MealPlanItemResponseDTO> mealPlanItems = mealPlan.getMealPlanItems().stream()
                .map(item -> MealPlanItemResponseDTO.builder()
                        .id(item.getId())
                        .amount(item.getAmount())
                        .mealType(item.getMealType())
                        .mealItemId(item.getMealItem().getId())
                        .mealItemName(item.getMealItem().getName())
                        .build())
                .toList();

        return MealPlanResponseDTO.builder()
                .id(mealPlan.getId())
                .memberId(mealPlan.getMember().getId())
                .mealPlanItems(mealPlanItems)
                .build();

    }


    public MealPlan createMealPlan(MealPlanRequestDTO request) {
        // Find the member by ID
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // Create a new MealPlan entity
        MealPlan mealPlan = new MealPlan();
        mealPlan.setMember(member);

        // Convert MealPlanItemDTO to MealPlanItem entities
        List<MealPlanItem> mealPlanItems = request.getMealPlanItems().stream()
                .map(itemDTO -> {
                    MealItem mealItem = mealItemRepository.findById(itemDTO.getMealItemId())
                            .orElseThrow(() -> new IllegalArgumentException("MealItem not found"));

                    return MealPlanItem.builder().amount(itemDTO.getAmount())
                            .mealType(itemDTO.getMealType())
                            .mealItem(mealItem)
                            .mealPlan(mealPlan)
                            .build();
                })
                .toList();

        // Set items to the meal plan
        mealPlan.setMealPlanItems(mealPlanItems);

        // Save the meal plan (cascades to meal plan items)
        return mealPlanRepository.save(mealPlan);
    }

    public MealPlan updateMealPlan(MealPlanRequestDTO requestDTO) {
        // Fetch the existing MealPlan by member ID
        MealPlan existingMealPlan = mealPlanRepository.findMealPlanByMemberId(requestDTO.getMemberId());

        if (existingMealPlan == null) {
            throw new EntityNotFoundException("MealPlan for the member ID " + requestDTO.getMemberId() + " not found.");
        }

        // Update MealPlanItems
        existingMealPlan.getMealPlanItems().clear(); // Remove existing items

        for (MealPlanItemRequestDTO itemDTO : requestDTO.getMealPlanItems()) {
            MealPlanItem mealPlanItem = MealPlanItem.builder()
                    .amount(itemDTO.getAmount())
                    .mealType(itemDTO.getMealType())
                    .mealItem(MealItem.builder().id(itemDTO.getMealItemId()).build()) // Reference MealItem
                    .mealPlan(existingMealPlan)
                    .build();
            existingMealPlan.getMealPlanItems().add(mealPlanItem);
        }
        return mealPlanRepository.save(existingMealPlan);
    }



}
