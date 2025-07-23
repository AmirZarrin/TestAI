package com.ai.testai.service;

import com.ai.testai.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LoyaltyService {
    // In-memory storage for demonstration purposes
    private final Map<Long, LoyaltyRewardDTO> rewards = new ConcurrentHashMap<>();
    private final AtomicLong rewardIdGenerator = new AtomicLong(1);
    private final Map<Long, Integer> customerPoints = new ConcurrentHashMap<>();
    
    
    public LoyaltyRewardDTO issueReward(IssueRewardRequestDTO rewardRequest) {
        // Create a new reward
        LoyaltyRewardDTO reward = new LoyaltyRewardDTO();
        long rewardId = rewardIdGenerator.getAndIncrement();
        
        reward.setRewardId(rewardId);
        reward.setPoints(rewardRequest.getPoints());
        reward.setExpiryDate(LocalDate.now().plusYears(1)); // 1 year expiry
        reward.setStatus("ACTIVE");
        reward.setRewardType(rewardRequest.getRewardType() != null ? 
                           rewardRequest.getRewardType() : "POINTS");
        
        // Store the reward
        rewards.put(rewardId, reward);
        
        // Update customer points if needed
        if (rewardRequest.getPoints() != null && rewardRequest.getPoints() > 0) {
            customerPoints.merge(rewardRequest.getCustomerId(), 
                               rewardRequest.getPoints(), 
                               Integer::sum);
        }
        
        return reward;
    }
    
    public LoyaltyRewardDTO getReward(Long id) {
        // Get reward by ID
        LoyaltyRewardDTO reward = rewards.get(id);
        if (reward == null) {
            throw new RuntimeException("Reward not found with id: " + id);
        }
        return reward;
    }
    
    public List<LoyaltyRewardDTO> getCustomerRewards(Long customerId) {
        // In a real application, we would filter rewards by customerId
        // For this example, we'll return all rewards
        return List.copyOf(rewards.values());
    }
    
    public LoyaltyRewardDTO redeemReward(Long id, RedeemRewardRequestDTO redeemRequest) {
        // Get the reward
        LoyaltyRewardDTO reward = getReward(id);
        
        // Update reward status
        reward.setStatus("REDEEMED");
        
        // In a real application, we would create a redemption record here
        
        return reward;
    }
    
    public CustomerPointsDTO getCustomerPoints(Long customerId) {
        // In a real application, this would be fetched from the database
        CustomerPointsDTO points = new CustomerPointsDTO();
        points.setCustomerId(customerId);
        points.setTotalPoints(1000);
        points.setAvailablePoints(750);
        points.setPendingPoints(250);
        points.setTier("GOLD");
        points.setPointsExpiringSoon(100);
        points.setNextTier("PLATINUM");
        points.setPointsToNextTier(1500);
        return points;
    }
    
    public AddPointsResponseDTO addPoints(Long customerId, AddPointsRequestDTO pointsRequest) {
        // In a real application, this would be a database transaction
        CustomerPointsDTO currentPoints = getCustomerPoints(customerId);
        int newTotalPoints = currentPoints.getTotalPoints() + pointsRequest.getPoints();
        
        // Create response
        AddPointsResponseDTO response = new AddPointsResponseDTO();
        response.setCustomerId(customerId);
        response.setPointsAdded(pointsRequest.getPoints());
        response.setTransactionId("TXN" + System.currentTimeMillis());
        response.setNewBalance(newTotalPoints);
        response.setTransactionDate(java.time.LocalDateTime.now());
        response.setStatus("COMPLETED");
        response.setMessage("Points added successfully");
        
        return response;
    }
    
    public List<LoyaltyTierDTO> getLoyaltyTiers() {
        // Create loyalty tiers
        LoyaltyTierDTO bronze = new LoyaltyTierDTO();
        bronze.setTierId(1L);
        bronze.setName("BRONZE");
        bronze.setDescription("Bronze Tier");
        bronze.setMinPoints(0);
        bronze.setDiscountRate(5.0);
        bronze.setBenefits(new String[]{"5% discount on select items", "Basic support"});
        
        LoyaltyTierDTO silver = new LoyaltyTierDTO();
        silver.setTierId(2L);
        silver.setName("SILVER");
        silver.setDescription("Silver Tier");
        silver.setMinPoints(1000);
        silver.setDiscountRate(10.0);
        silver.setBenefits(new String[]{"10% discount on all items", "Priority support", "Free shipping"});
        
        LoyaltyTierDTO gold = new LoyaltyTierDTO();
        gold.setTierId(3L);
        gold.setName("GOLD");
        gold.setDescription("Gold Tier");
        gold.setMinPoints(5000);
        gold.setDiscountRate(15.0);
        gold.setBenefits(new String[]{
            "15% discount on all items", 
            "24/7 VIP support", 
            "Free shipping & returns", 
            "Early access to sales"
        });
        
        return List.of(bronze, silver, gold);
    }
}
