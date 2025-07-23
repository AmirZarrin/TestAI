package com.ai.testai.controller;

import com.ai.testai.dto.CreditInfoDTO;
import com.ai.testai.dto.CustomerDTO;
import com.ai.testai.dto.InstallmentPlanDTO;
import com.ai.testai.dto.LoyaltyRewardDTO;
import com.ai.testai.dto.TransactionDTO;
import com.ai.testai.entity.Person;
import com.ai.testai.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        Person person = modelMapper.map(customerDTO, Person.class);
        Person createdPerson = customerService.createCustomer(person);
        CustomerDTO createdCustomerDTO = modelMapper.map(createdPerson, CustomerDTO.class);
        return ResponseEntity.ok(createdCustomerDTO);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        Person person = customerService.getCustomer(id);
        CustomerDTO customerDTO = modelMapper.map(person, CustomerDTO.class);
        return ResponseEntity.ok(customerDTO);
    }
    
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<Person> persons = customerService.getAllCustomers();
        List<CustomerDTO> customerDTOs = persons.stream()
                .map(person -> modelMapper.map(person, CustomerDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerDTOs);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id, 
            @RequestBody CustomerDTO customerDTO) {
        Person person = modelMapper.map(customerDTO, Person.class);
        Person updatedPerson = customerService.updateCustomer(id, person);
        CustomerDTO updatedCustomerDTO = modelMapper.map(updatedPerson, CustomerDTO.class);
        return ResponseEntity.ok(updatedCustomerDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/credit")
    public ResponseEntity<CreditInfoDTO> getCustomerCreditInfo(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerCreditInfo(id));
    }
    
    @GetMapping("/{id}/installment-plans")
    public ResponseEntity<List<InstallmentPlanDTO>> getCustomerInstallmentPlans(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerInstallmentPlans(id));
    }
    
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDTO>> getCustomerTransactions(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerTransactions(id));
    }
    
    @GetMapping("/{id}/loyalty-rewards")
    public ResponseEntity<List<LoyaltyRewardDTO>> getCustomerLoyaltyRewards(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerLoyaltyRewards(id));
    }
}
