package com.bnplcore.service;

import com.bnplcore.dto.InstallmentPaymentDTO;
import com.bnplcore.dto.InstallmentPenaltyDTO;
import com.bnplcore.dto.InstallmentPlanDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstallmentPlanService {
    
    @Autowired
    private ModelMapper modelMapper;
    
    // In-memory storage for demonstration (replace with repository in production)
    private List<InstallmentPlanDTO> plans = new ArrayList<>();
    private List<InstallmentPaymentDTO> payments = new ArrayList<>();
    private List<InstallmentPenaltyDTO> penalties = new ArrayList<>();
    private long planIdCounter = 1;
    private long paymentIdCounter = 1;
    private long penaltyIdCounter = 1;
    
    public InstallmentPlanDTO createInstallmentPlan(InstallmentPlanDTO planDTO) {
        // Set IDs and timestamps
        planDTO.setId(planIdCounter++);
        planDTO.setCreatedAt(LocalDateTime.now());
        planDTO.setUpdatedAt(LocalDateTime.now());
        planDTO.setStatus("ACTIVE");
        
        // Calculate end date if not provided
        if (planDTO.getStartDate() != null && planDTO.getTermInMonths() != null) {
            planDTO.setEndDate(planDTO.getStartDate().plusMonths(planDTO.getTermInMonths()));
        }
        
        // Initialize payment tracking
        planDTO.setPaymentsMade(0);
        planDTO.setPaymentsRemaining(planDTO.getTermInMonths());
        planDTO.setPaidAmount(BigDecimal.ZERO);
        planDTO.setRemainingAmount(planDTO.getTotalAmount());
        
        // Save to in-memory list (replace with repository in production)
        plans.add(planDTO);
        
        return planDTO;
    }
    
    public InstallmentPlanDTO getInstallmentPlan(Long id) {
        return plans.stream()
                .filter(plan -> plan.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Installment plan not found with id: " + id));
    }
    
    public List<InstallmentPlanDTO> getAllInstallmentPlans() {
        return new ArrayList<>(plans);
    }
    
    public List<InstallmentPlanDTO> getCustomerInstallmentPlans(Long customerId) {
        return plans.stream()
                .filter(plan -> plan.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }
    
    public InstallmentPaymentDTO createPayment(InstallmentPaymentDTO paymentDTO) {
        // Set IDs and timestamps
        paymentDTO.setId(paymentIdCounter++);
        paymentDTO.setCreatedAt(LocalDateTime.now());
        paymentDTO.setUpdatedAt(LocalDateTime.now());
        paymentDTO.setStatus("COMPLETED");
        
        // Update related installment plan
        InstallmentPlanDTO plan = getInstallmentPlan(paymentDTO.getInstallmentPlanId());
        plan.setPaidAmount(plan.getPaidAmount().add(paymentDTO.getAmount()));
        plan.setRemainingAmount(plan.getTotalAmount().subtract(plan.getPaidAmount()));
        plan.setPaymentsMade(plan.getPaymentsMade() + 1);
        plan.setPaymentsRemaining(plan.getTermInMonths() - plan.getPaymentsMade());
        plan.setUpdatedAt(LocalDateTime.now());
        
        // Save payment to in-memory list (replace with repository in production)
        payments.add(paymentDTO);
        
        return paymentDTO;
    }
    
    public List<InstallmentPaymentDTO> getPlanPayments(Long planId) {
        return payments.stream()
                .filter(payment -> payment.getInstallmentPlanId().equals(planId))
                .collect(Collectors.toList());
    }
    
    public InstallmentPenaltyDTO addPenalty(InstallmentPenaltyDTO penaltyDTO) {
        // Set IDs and timestamps
        penaltyDTO.setId(penaltyIdCounter++);
        penaltyDTO.setCreatedAt(LocalDateTime.now());
        penaltyDTO.setUpdatedAt(LocalDateTime.now());
        penaltyDTO.setStatus("PENDING");
        
        // Save penalty to in-memory list (replace with repository in production)
        penalties.add(penaltyDTO);
        
        return penaltyDTO;
    }
    
    public List<InstallmentPenaltyDTO> getPlanPenalties(Long planId) {
        return penalties.stream()
                .filter(penalty -> penalty.getInstallmentPlanId().equals(planId))
                .collect(Collectors.toList());
    }
    
    public InstallmentPlanDTO updatePlanStatus(Long id, String status) {
        InstallmentPlanDTO plan = getInstallmentPlan(id);
        plan.setStatus(status);
        plan.setUpdatedAt(LocalDateTime.now());
        return plan;
    }
}
