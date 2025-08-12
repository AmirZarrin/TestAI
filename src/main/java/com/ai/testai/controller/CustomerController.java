package com.ai.testai.controller;

import com.ai.testai.dto.CreditInfoDTO;
import com.ai.testai.dto.CustomerDTO;
import com.ai.testai.dto.InstallmentPlanDTO;
import com.ai.testai.dto.LoyaltyRewardDTO;
import com.ai.testai.dto.TransactionDTO;
import com.ai.testai.entity.Person;
import com.ai.testai.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "APIs for managing customer information and related data")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new customer", description = "Creates a new customer with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer created successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<CustomerDTO> createCustomer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Customer object that needs to be created",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody CustomerDTO customerDTO) {
        Person person = modelMapper.map(customerDTO, Person.class);
        Person createdPerson = customerService.createCustomer(person);
        CustomerDTO createdCustomerDTO = modelMapper.map(createdPerson, CustomerDTO.class);
        return ResponseEntity.ok(createdCustomerDTO);
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get customer by ID", description = "Returns a single customer by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer found",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerDTO> getCustomer(
            @Parameter(description = "ID of the customer to be obtained", required = true)
            @PathVariable Long id) {
        Person person = customerService.getCustomer(id);
        CustomerDTO customerDTO = modelMapper.map(person, CustomerDTO.class);
        return ResponseEntity.ok(customerDTO);
    }
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all customers", description = "Returns a list of all customers")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of customers",
                content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<Person> persons = customerService.getAllCustomers();
        List<CustomerDTO> customerDTOs = persons.stream()
                .map(person -> modelMapper.map(person, CustomerDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerDTOs);
    }
    
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existing customer", description = "Updates the details of an existing customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerDTO> updateCustomer(
            @Parameter(description = "ID of the customer to be updated", required = true)
            @PathVariable Long id, 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated customer object",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody CustomerDTO customerDTO) {
        Person person = modelMapper.map(customerDTO, Person.class);
        Person updatedPerson = customerService.updateCustomer(id, person);
        CustomerDTO updatedCustomerDTO = modelMapper.map(updatedPerson, CustomerDTO.class);
        return ResponseEntity.ok(updatedCustomerDTO);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer", description = "Deletes a customer by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID of the customer to be deleted", required = true)
            @PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping(value = "/{id}/credit", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get customer credit information", description = "Returns credit information for a specific customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Credit information retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CreditInfoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CreditInfoDTO> getCustomerCreditInfo(
            @Parameter(description = "ID of the customer whose credit information is being requested", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerCreditInfo(id));
    }
    
    @GetMapping(value = "/{id}/installment-plans", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get customer's installment plans", description = "Returns a list of all installment plans for a specific customer")
    @ApiResponse(responseCode = "200", description = "Installment plans retrieved successfully",
                content = @Content(schema = @Schema(implementation = InstallmentPlanDTO.class)))
    public ResponseEntity<List<InstallmentPlanDTO>> getCustomerInstallmentPlans(
            @Parameter(description = "ID of the customer whose installment plans are being requested", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerInstallmentPlans(id));
    }
    
    @GetMapping(value = "/{id}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get customer's transactions", description = "Returns a list of all transactions for a specific customer")
    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
                content = @Content(schema = @Schema(implementation = TransactionDTO.class)))
    public ResponseEntity<List<TransactionDTO>> getCustomerTransactions(
            @Parameter(description = "ID of the customer whose transactions are being requested", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerTransactions(id));
    }
    
    @GetMapping(value = "/{id}/loyalty-rewards", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get customer's loyalty rewards", description = "Returns a list of all loyalty rewards for a specific customer")
    @ApiResponse(responseCode = "200", description = "Loyalty rewards retrieved successfully",
                content = @Content(schema = @Schema(implementation = LoyaltyRewardDTO.class)))
    public ResponseEntity<List<LoyaltyRewardDTO>> getCustomerLoyaltyRewards(
            @Parameter(description = "ID of the customer whose loyalty rewards are being requested", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerLoyaltyRewards(id));
    }
}
