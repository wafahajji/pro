package com.maiia.pro.repository;

import com.maiia.pro.entity.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Integer> {

    List<Appointment> findByPractitionerId(Integer practitionerId);

    List<Appointment> findAll();

    @Query("SELECT a  " +
            "FROM Appointment a " +
            "WHERE a.practitionerId = :practitionerId " +
            "AND ((a.startDate < :endDate) AND (a.endDate > :startDate))")
    Optional<Appointment> checkAppointment(Integer practitionerId, LocalDateTime startDate, LocalDateTime endDate);


}
