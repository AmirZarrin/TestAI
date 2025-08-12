package com.bnplcore.repository;

import com.bnplcore.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // Custom query methods can be added here if needed
}
