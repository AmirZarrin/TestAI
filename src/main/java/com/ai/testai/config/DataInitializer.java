package com.ai.testai.config;

import com.ai.testai.entity.*;
import com.ai.testai.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Configuration
public class DataInitializer {

    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private MerchantRepository merchantRepository;
    
    @Autowired
    private MerchantPanelAccessRepository merchantPanelAccessRepository;
    
    @PostConstruct
    @Transactional
    public void init() {
        // Only initialize if database is empty
        if (personRepository.count() == 0) {
            // Create a test person
            Person person = new Person();
            person.setFirstName("Test");
            person.setLastName("User");
            person.setNationalCode("1234567890");
            person.setPhoneNumber("09123456789");
            person.setEmail("test@example.com");
            person.setBirthDate(LocalDate.of(1990, 1, 1));
            person = personRepository.save(person);
            
            // Create a test merchant
            Merchant merchant = new Merchant();
            merchant.setName("Test Merchant");
            merchant.setRegistrationNumber("M12345678");
            merchant.setPhoneNumber("02112345678");
            merchant.setEmail("merchant@example.com");
            merchant.setAddress("123 Test St, Tehran, Iran");
            merchant = merchantRepository.save(merchant);
            
            // Create merchant panel access
            MerchantPanelAccess panelAccess = new MerchantPanelAccess();
            panelAccess.setUsername("admin");
            panelAccess.setPasswordHash("$2a$10$xVCH4IA5jlJ59x9ZOUzZP.9JvqhZyN1EpiTIpaKhZ7rT6pkNCCPAe"); // password: admin123
            panelAccess.setEmail("admin@merchant.com");
            panelAccess.setRole("ADMIN");
            panelAccess.setLastLogin(LocalDateTime.now());
            panelAccess.setIsActive(true);
            panelAccess.setCreatedAt(LocalDateTime.now());
            panelAccess.setUpdatedAt(LocalDateTime.now());
            
            // Add panel access to merchant using the helper method
            merchant.addPanelAccess(panelAccess);
            merchantRepository.save(merchant);
        }
    }
}
