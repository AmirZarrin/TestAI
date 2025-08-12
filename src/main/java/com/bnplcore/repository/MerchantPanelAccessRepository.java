package com.bnplcore.repository;

import com.bnplcore.entity.MerchantPanelAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantPanelAccessRepository extends JpaRepository<MerchantPanelAccess, Long> {
    // Custom query methods can be added here if needed
}
