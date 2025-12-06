package com.baymak.backend.repository;

import com.baymak.backend.model.ServiceRequest;
import com.baymak.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByUser(User user);
    Optional<ServiceRequest> findByIdAndUser(Long id, User user);
}

