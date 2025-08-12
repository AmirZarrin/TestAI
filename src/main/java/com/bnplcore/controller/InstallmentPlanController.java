package com.bnplcore.controller;

import com.bnplcore.dto.InstallmentPaymentDTO;
import com.bnplcore.dto.InstallmentPenaltyDTO;
import com.bnplcore.dto.InstallmentPlanDTO;
import com.bnplcore.service.InstallmentPlanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/installment-plans")
public class InstallmentPlanController {
    
    @Autowired
    private InstallmentPlanService installmentPlanService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @PostMapping
    public ResponseEntity<InstallmentPlanDTO> createInstallmentPlan(
            @Valid @RequestBody InstallmentPlanDTO installmentPlanDTO) {
        InstallmentPlanDTO createdPlan = installmentPlanService.createInstallmentPlan(installmentPlanDTO);
        return ResponseEntity.ok(createdPlan);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InstallmentPlanDTO> getInstallmentPlan(@PathVariable Long id) {
        InstallmentPlanDTO plan = installmentPlanService.getInstallmentPlan(id);
        return ResponseEntity.ok(plan);
    }
    
    @GetMapping
    public ResponseEntity<List<InstallmentPlanDTO>> getAllInstallmentPlans() {
        List<InstallmentPlanDTO> plans = installmentPlanService.getAllInstallmentPlans();
        return ResponseEntity.ok(plans);
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<InstallmentPlanDTO>> getCustomerInstallmentPlans(
            @PathVariable Long customerId) {
        List<InstallmentPlanDTO> plans = installmentPlanService.getCustomerInstallmentPlans(customerId);
        return ResponseEntity.ok(plans);
    }
    
    @PostMapping("/{id}/payments")
    public ResponseEntity<InstallmentPaymentDTO> createPayment(
            @PathVariable Long id,
            @Valid @RequestBody InstallmentPaymentDTO paymentDTO) {
        paymentDTO.setInstallmentPlanId(id);
        InstallmentPaymentDTO createdPayment = installmentPlanService.createPayment(paymentDTO);
        return ResponseEntity.ok(createdPayment);
    }
    
    @GetMapping("/{id}/payments")
    public ResponseEntity<List<InstallmentPaymentDTO>> getPlanPayments(@PathVariable Long id) {
        List<InstallmentPaymentDTO> payments = installmentPlanService.getPlanPayments(id);
        return ResponseEntity.ok(payments);
    }
    
    @PostMapping("/{id}/penalties")
    public ResponseEntity<InstallmentPenaltyDTO> addPenalty(
            @PathVariable Long id,
            @Valid @RequestBody InstallmentPenaltyDTO penaltyDTO) {
        penaltyDTO.setInstallmentPlanId(id);
        InstallmentPenaltyDTO createdPenalty = installmentPlanService.addPenalty(penaltyDTO);
        return ResponseEntity.ok(createdPenalty);
    }
    
    @GetMapping("/{id}/penalties")
    public ResponseEntity<List<InstallmentPenaltyDTO>> getPlanPenalties(@PathVariable Long id) {
        List<InstallmentPenaltyDTO> penalties = installmentPlanService.getPlanPenalties(id);
        return ResponseEntity.ok(penalties);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<InstallmentPlanDTO> updatePlanStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        InstallmentPlanDTO updatedPlan = installmentPlanService.updatePlanStatus(id, status);
        return ResponseEntity.ok(updatedPlan);
    }
}
