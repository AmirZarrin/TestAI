package com.bnplcore.repository;

import com.bnplcore.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    // Custom query methods can be added here if needed
}
