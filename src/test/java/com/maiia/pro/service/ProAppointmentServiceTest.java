package com.maiia.pro.service;
import com.maiia.pro.EntityFactory;
import com.maiia.pro.constants.Messages;
import com.maiia.pro.model.dto.AppointmentDTO;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;

import com.maiia.pro.repository.AppointmentRepository;
import com.maiia.pro.repository.AvailabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;


import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ProAppointmentServiceTest {
    private final EntityFactory entityFactory = new EntityFactory();
    private static final Integer PATIENT_ID = 657679;

    @Autowired
    private ProAppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;


    @BeforeEach
    public void cleanUp() {
        this.appointmentRepository.deleteAll();
    }
    @Test
    void createAppointmentSuccess() {

        final LocalDateTime startDate = LocalDateTime.of(2024, Month.FEBRUARY, 5, 9, 0);
        final Availability availability = this.availabilityRepository.save(
                this.entityFactory.createAvailability(1, startDate, startDate.plusMinutes(15))
        );

        final AppointmentDTO request = AppointmentDTO.builder()
                .practitionerId(availability.getPractitionerId())
                .patientId(PATIENT_ID)
                .startDate(startDate)
                .endDate(startDate.plusMinutes(15))
                .build();

        final AppointmentDTO appointment = this.appointmentService.createAppointment(request);

        assertNotNull(appointment);
        assertEquals(request.getPractitionerId(), appointment.getPractitionerId());
        assertEquals(request.getPatientId(), appointment.getPatientId());
        assertEquals(request.getStartDate(), appointment.getStartDate());
        assertEquals(request.getEndDate(), appointment.getEndDate());

        final Appointment savedAppointment = this.appointmentService.find(appointment.getId());

        assertNotNull(savedAppointment);
        assertEquals(appointment.getId(), savedAppointment.getId());
    }

    @Test
    void createAppointmentFailsWhenTimeSlotAlreadyBooked() {
        final LocalDateTime startDate = LocalDateTime.of(2024, Month.FEBRUARY, 5, 9, 0);
        this.availabilityRepository.save(this.entityFactory.createAvailability(1, startDate, startDate.plusMinutes(15)));
        this.appointmentRepository.save(this.entityFactory.createAppointment(1, PATIENT_ID, startDate, startDate.plusMinutes(15)));

        final AppointmentDTO request = AppointmentDTO.builder()
                .practitionerId(1)
                .patientId(PATIENT_ID)
                .startDate(startDate)
                .endDate(startDate.plusMinutes(15))
                .build();

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.appointmentService.createAppointment(request);
        });
        assertEquals(Messages.TIME_SLOT_ALREADY_BOOKED, exception.getMessage());
    }


    @Test
    void createAppointmentSuccessWithOverlappingAvailabilities() {

        final LocalDateTime startDate = LocalDateTime.of(2024, Month.FEBRUARY, 5, 9, 0);
        this.availabilityRepository.save(this.entityFactory.createAvailability(1, startDate, startDate.plusMinutes(30)));

        final AppointmentDTO request = AppointmentDTO.builder()
                .practitionerId(1)
                .patientId(PATIENT_ID)
                .startDate(startDate.plusMinutes(15))
                .endDate(startDate.plusMinutes(30))
                .build();
        final AppointmentDTO appointment = this.appointmentService.createAppointment(request);

        assertNotNull(appointment);
        assertEquals(request.getStartDate(), appointment.getStartDate());
        assertEquals(request.getEndDate(), appointment.getEndDate());
    }


    @Test
    void createAppointmentFailsWhenRequestDataIsInvalid() {
        final AppointmentDTO request = AppointmentDTO.builder()
                .practitionerId(null)
                .patientId(PATIENT_ID)
                .startDate(null)
                .endDate(null)
                .build();

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.appointmentService.createAppointment(request);
        });

        assertEquals(Messages.INVALID_APPOINTMENT_REQUEST_DATA, exception.getMessage());
    }
}