package com.ai.testai.controller;

import com.ai.testai.dto.*;
import com.ai.testai.service.LoyaltyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {
    
    @Autowired
    private LoyaltyService loyaltyService;
    
    @PostMapping("/rewards")
    public ResponseEntity<LoyaltyRewardDTO> issueReward(
            @Valid @RequestBody IssueRewardRequestDTO rewardRequest) {
        return ResponseEntity.ok(loyaltyService.issueReward(rewardRequest));
    }
    
    @GetMapping("/rewards/{id}")
    public ResponseEntity<LoyaltyRewardDTO> getReward(@PathVariable Long id) {
        return ResponseEntity.ok(loyaltyService.getReward(id));
    }
    
    @GetMapping("/customers/{customerId}/rewards")
    public ResponseEntity<List<LoyaltyRewardDTO>> getCustomerRewards(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(loyaltyService.getCustomerRewards(customerId));
    }
    
    @PostMapping("/rewards/{id}/redeem")
    public ResponseEntity<LoyaltyRewardDTO> redeemReward(
            @PathVariable Long id,
            @Valid @RequestBody RedeemRewardRequestDTO redeemRequest) {
        return ResponseEntity.ok(loyaltyService.redeemReward(id, redeemRequest));
    }
    
    @GetMapping("/customers/{customerId}/points")
    public ResponseEntity<CustomerPointsDTO> getCustomerPoints(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(loyaltyService.getCustomerPoints(customerId));
    }
    
    @PostMapping("/customers/{customerId}/points")
    public ResponseEntity<AddPointsResponseDTO> addPoints(
            @PathVariable Long customerId,
            @Valid @RequestBody AddPointsRequestDTO pointsRequest) {
        return ResponseEntity.ok(loyaltyService.addPoints(customerId, pointsRequest));
    }
    
    @GetMapping("/tiers")
    public ResponseEntity<List<LoyaltyTierDTO>> getLoyaltyTiers() {
        return ResponseEntity.ok(loyaltyService.getLoyaltyTiers());
    }
}
