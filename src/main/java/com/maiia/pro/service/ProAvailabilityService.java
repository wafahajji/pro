package com.maiia.pro.service;

import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.entity.TimeSlot;
import com.maiia.pro.repository.AppointmentRepository;
import com.maiia.pro.repository.AvailabilityRepository;
import com.maiia.pro.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProAvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    public List<Availability> findByPractitionerId(final Integer practitionerId) {
        return this.availabilityRepository.findByPractitionerId(practitionerId);
    }

    @Transactional
    public List<Availability> generateAvailabilities(final Integer practitionerId) {

        if (practitionerId == null) {
            throw new IllegalArgumentException("Practitioner ID cannot be null");
        }
        final List<TimeSlot> timeSlots = this.timeSlotRepository.findByPractitionerId(practitionerId) ;

        if (timeSlots.isEmpty()) {
            return Collections.emptyList();
        }

        this.availabilityRepository.deleteAvailabilitiesByPractitionerId(practitionerId);

        final List<Availability> availabilities = new ArrayList<>();

        for (final TimeSlot slot : timeSlots) {
            LocalDateTime startTime = slot.getStartDate();
            final LocalDateTime endTime = slot.getEndDate();
            while (startTime.isBefore(endTime) || Duration.between(startTime, endTime).toMinutes() == 10) {
                LocalDateTime nextTime = startTime.plusMinutes(15);

                final Availability availability = Availability.builder()
                        .practitionerId(practitionerId)
                        .startDate(startTime)
                        .endDate(nextTime).build();
                final Optional<Appointment> appointment = getAppointment(availability);

                if (appointment.isEmpty()) {
                    availabilities.add(availability);
                }else{
                    nextTime = appointment.get().getEndDate();
                }
                startTime = nextTime;
            }
        }
        this.availabilityRepository.saveAll(availabilities);
        return availabilities;
    }

    private Optional<Appointment> getAppointment(final Availability availability) {
        return this.appointmentRepository.checkAppointment(
                availability.getPractitionerId(), availability.getStartDate(), availability.getEndDate()
        );
    }






}
