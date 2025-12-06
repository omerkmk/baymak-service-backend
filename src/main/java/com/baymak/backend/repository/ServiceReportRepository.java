package com.baymak.backend.repository;

import com.baymak.backend.model.Appointment;
import com.baymak.backend.model.ServiceReport;
import com.baymak.backend.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceReportRepository extends JpaRepository<ServiceReport, Long> {
    Optional<ServiceReport> findByAppointment(Appointment appointment);
    List<ServiceReport> findByTechnician(Technician technician);
    List<ServiceReport> findByAppointmentId(Long appointmentId);
}

