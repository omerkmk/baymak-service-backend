package com.baymak.backend.repository;

import com.baymak.backend.model.Appointment;
import com.baymak.backend.model.AppointmentStatus;
import com.baymak.backend.model.Technician;
import com.baymak.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUser(User user);
    List<Appointment> findByTechnician(Technician technician);
    List<Appointment> findByTechnicianAndStatus(Technician technician, AppointmentStatus status);
    List<Appointment> findByStatus(AppointmentStatus status);
    Optional<Appointment> findByIdAndUser(Long id, User user);
    Optional<Appointment> findByIdAndTechnician(Long id, Technician technician);
}

