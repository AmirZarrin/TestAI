package com.bnplcore.service;

import com.bnplcore.dto.CreditInfoDTO;
import com.bnplcore.dto.InstallmentPlanDTO;
import com.bnplcore.dto.LoyaltyRewardDTO;
import com.bnplcore.dto.TransactionDTO;
import com.bnplcore.entity.Person;
import com.bnplcore.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    public Person createCustomer(Person person) {
        // Additional validation and business logic can be added here
        return customerRepository.save(person);
    }
    
    public Person getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }
    
    public List<Person> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    public Person updateCustomer(Long id, Person personDetails) {
        Person person = getCustomer(id);
        // Update person details
        person.setFirstName(personDetails.getFirstName());
        person.setLastName(personDetails.getLastName());
        person.setEmail(personDetails.getEmail());
        person.setPhoneNumber(personDetails.getPhoneNumber());
        person.setNationalCode(personDetails.getNationalCode());
        person.setBirthDate(personDetails.getBirthDate());
        return customerRepository.save(person);
    }
    
    public void deleteCustomer(Long id) {
        Person person = getCustomer(id);
        customerRepository.delete(person);
    }
    
    public CreditInfoDTO getCustomerCreditInfo(Long customerId) {
        // Implementation to get customer's credit information
        // This is a placeholder - implement actual logic based on your requirements
        CreditInfoDTO creditInfo = new CreditInfoDTO();
        creditInfo.setCustomerId(customerId);
        creditInfo.setCreditScore(750);
        creditInfo.setCreditLimit(5000.00);
        creditInfo.setAvailableCredit(3500.00);
        return creditInfo;
    }
    
    public List<InstallmentPlanDTO> getCustomerInstallmentPlans(Long customerId) {
        // Implementation to get customer's installment plans
        // This is a placeholder - implement actual logic based on your requirements
        InstallmentPlanDTO plan = new InstallmentPlanDTO();
        plan.setId(1L);
        plan.setCustomerId(1L); // Adding required customerId field
        plan.setOrderId(1L);    // Adding required orderId field
        plan.setTotalAmount(new BigDecimal("1000.00"));
        plan.setMonthlyPayment(new BigDecimal("100.00"));
        plan.setStatus("ACTIVE");
        plan.setPaymentsMade(0);
        plan.setPaymentsRemaining(10);
        return List.of(plan);
    }
    
    public List<TransactionDTO> getCustomerTransactions(Long customerId) {
        // Implementation to get customer's transactions
        // This is a placeholder - implement actual logic based on your requirements
        TransactionDTO transaction = new TransactionDTO();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType("PAYMENT");
        transaction.setStatus("COMPLETED");
        transaction.setCustomerId(customerId);
        transaction.setCustomerName("John Doe");
        transaction.setTransactionDate(java.time.LocalDateTime.now());
        transaction.setDescription("Sample transaction");
        transaction.setPaymentMethod("CREDIT_CARD");
        transaction.setCurrency("USD");
        transaction.setCreatedAt(java.time.LocalDateTime.now().minusDays(1));
        transaction.setUpdatedAt(java.time.LocalDateTime.now());
        return List.of(transaction);
    }
    
    public List<LoyaltyRewardDTO> getCustomerLoyaltyRewards(Long customerId) {
        // Implementation to get customer's loyalty rewards
        // This is a placeholder - implement actual logic based on your requirements
        LoyaltyRewardDTO reward = new LoyaltyRewardDTO();
        reward.setRewardId(1L);
        reward.setPoints(100);
        reward.setExpiryDate(java.time.LocalDate.of(2023, 12, 31));
        reward.setStatus("ACTIVE");
        reward.setRewardType("POINTS");
        return List.of(reward);
    }
}
