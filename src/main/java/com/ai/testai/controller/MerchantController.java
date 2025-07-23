package com.ai.testai.controller;

import com.ai.testai.dto.*;
import com.ai.testai.service.MerchantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {
    
    private final MerchantService merchantService;
    
    @Autowired
    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }
    
    @PostMapping
    public ResponseEntity<MerchantDTO> createMerchant(
            @Valid @RequestBody MerchantDTO merchantDTO) {
        return ResponseEntity.ok(merchantService.createMerchant(merchantDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MerchantDTO> getMerchant(@PathVariable Long id) {
        return ResponseEntity.ok(merchantService.getMerchant(id));
    }
    
    @GetMapping
    public ResponseEntity<List<MerchantDTO>> getAllMerchants() {
        return ResponseEntity.ok(merchantService.getAllMerchants());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MerchantDTO> updateMerchant(
            @PathVariable Long id, 
            @Valid @RequestBody MerchantDTO merchantDTO) {
        return ResponseEntity.ok(merchantService.updateMerchant(id, merchantDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) {
        merchantService.deleteMerchant(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/products")
    public ResponseEntity<MerchantProductDTO> addProduct(
            @PathVariable Long id, 
            @Valid @RequestBody MerchantProductDTO productDTO) {
        return ResponseEntity.ok(merchantService.addProduct(id, productDTO));
    }
    
    @GetMapping("/{id}/products")
    public ResponseEntity<List<MerchantProductDTO>> getMerchantProducts(@PathVariable Long id) {
        return ResponseEntity.ok(merchantService.getMerchantProducts(id));
    }
    
    @GetMapping("/{id}/credits")
    public ResponseEntity<List<MerchantCreditDTO>> getMerchantCredits(@PathVariable Long id) {
        return ResponseEntity.ok(merchantService.getMerchantCredits(id));
    }
    
    @PostMapping("/{id}/panel-access")
    public ResponseEntity<MerchantPanelAccessDTO> addPanelAccess(
            @PathVariable Long id, 
            @Valid @RequestBody MerchantPanelAccessDTO panelAccessDTO) {
        return ResponseEntity.ok(merchantService.addPanelAccess(id, panelAccessDTO));
    }
}
