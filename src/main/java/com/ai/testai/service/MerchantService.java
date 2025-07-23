package com.ai.testai.service;

import com.ai.testai.dto.*;
import com.ai.testai.entity.Merchant;
import com.ai.testai.exception.ResourceNotFoundException;
import com.ai.testai.repository.MerchantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Transactional
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final ModelMapper modelMapper;
    
    // In-memory storage for demonstration purposes
    private final ConcurrentHashMap<Long, MerchantProductDTO> products = new ConcurrentHashMap<>();
    private final AtomicLong productIdGenerator = new AtomicLong(1);
    
    private final ConcurrentHashMap<Long, MerchantCreditDTO> credits = new ConcurrentHashMap<>();
    private final AtomicLong creditIdGenerator = new AtomicLong(1);
    
    private final ConcurrentHashMap<Long, MerchantPanelAccessDTO> panelAccesses = new ConcurrentHashMap<>();
    private final AtomicLong panelAccessIdGenerator = new AtomicLong(1);
    
    @Autowired
    public MerchantService(MerchantRepository merchantRepository, ModelMapper modelMapper) {
        this.merchantRepository = merchantRepository;
        this.modelMapper = modelMapper;
    }
    
    public MerchantDTO createMerchant(MerchantDTO merchantDTO) {
        // Convert DTO to entity
        Merchant merchant = modelMapper.map(merchantDTO, Merchant.class);
        merchant = merchantRepository.save(merchant);
        
        // Convert entity back to DTO
        return modelMapper.map(merchant, MerchantDTO.class);
    }
    
    public MerchantDTO getMerchant(Long id) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found with id: " + id));
        return modelMapper.map(merchant, MerchantDTO.class);
    }
    
    public List<MerchantDTO> getAllMerchants() {
        return merchantRepository.findAll().stream()
                .map(merchant -> modelMapper.map(merchant, MerchantDTO.class))
                .collect(Collectors.toList());
    }
    
    public MerchantDTO updateMerchant(Long id, MerchantDTO merchantDTO) {
        // Get existing merchant
        Merchant existingMerchant = merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found with id: " + id));
        
        // Update fields from DTO to entity
        modelMapper.map(merchantDTO, existingMerchant);
        
        // Save and return updated merchant as DTO
        Merchant updatedMerchant = merchantRepository.save(existingMerchant);
        return modelMapper.map(updatedMerchant, MerchantDTO.class);
    }
    
    public void deleteMerchant(Long id) {
        if (!merchantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Merchant not found with id: " + id);
        }
        merchantRepository.deleteById(id);
    }
    
    public MerchantProductDTO addProduct(Long merchantId, MerchantProductDTO productDTO) {
        // In a real application, this would be saved to a database
        productDTO.setId(productIdGenerator.getAndIncrement());
        products.put(productDTO.getId(), productDTO);
        return productDTO;
    }
    
    public List<MerchantProductDTO> getMerchantProducts(Long merchantId) {
        // In a real application, this would query the database for products by merchantId
        return List.copyOf(products.values());
    }
    
    public List<MerchantCreditDTO> getMerchantCredits(Long merchantId) {
        // In a real application, this would query the database for credits by merchantId
        // For demonstration, we'll return sample data
        MerchantCreditDTO credit = new MerchantCreditDTO();
        credit.setId(creditIdGenerator.getAndIncrement());
        credit.setMerchantId(merchantId);
        credit.setCreditType("LINE_OF_CREDIT");
        credit.setCreditLimit(new BigDecimal("50000.00"));
        credit.setAvailableCredit(new BigDecimal("35000.00"));
        credit.setStatus("ACTIVE");
        
        credits.put(credit.getId(), credit);
        return List.of(credit);
    }
    
    public MerchantPanelAccessDTO addPanelAccess(Long merchantId, MerchantPanelAccessDTO panelAccessDTO) {
        // In a real application, this would be saved to a database
        panelAccessDTO.setId(panelAccessIdGenerator.getAndIncrement());
        panelAccessDTO.setMerchantId(merchantId);
        panelAccessDTO.setStatus("ACTIVE");
        
        panelAccesses.put(panelAccessDTO.getId(), panelAccessDTO);
        return panelAccessDTO;
    }
}
