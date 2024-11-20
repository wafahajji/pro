package com.maiia.pro.service;

import com.maiia.pro.constants.Messages;
import com.maiia.pro.model.dto.AppointmentDTO;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.mapper.dto.AppointmentDTOMapper;
import com.maiia.pro.repository.AppointmentRepository;
import com.maiia.pro.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProAppointmentService {

    private final AppointmentRepository appointmentRepository;


    private final AvailabilityRepository availabilityRepository;


    private final ProAvailabilityService proAvailabilityService;

    @Autowired
    public ProAppointmentService(final ProAvailabilityService proAvailabilityService, final AvailabilityRepository availabilityRepository, final AppointmentRepository appointmentRepository) {
        this.availabilityRepository = availabilityRepository;
        this.proAvailabilityService = proAvailabilityService;
        this.appointmentRepository = appointmentRepository;

    }

    public Appointment find(final Integer appointmentId) {
        return this.appointmentRepository.findById(appointmentId).orElseThrow();
    }

    public List<Appointment> findAll() {
        return this.appointmentRepository.findAll();
    }

    public List<Appointment> findByPractitionerId(final Integer practitionerId) {
        return this.appointmentRepository.findByPractitionerId(practitionerId);

    }




    @Transactional
    public AppointmentDTO createAppointment(final AppointmentDTO appointmentDTO) {

        if (appointmentDTO.getPractitionerId() == null || appointmentDTO.getPatientId() == null ||
                appointmentDTO.getStartDate() == null || appointmentDTO.getEndDate() == null) {
            throw new IllegalArgumentException(Messages.INVALID_APPOINTMENT_REQUEST_DATA);
        }

        if (appointmentDTO.getStartDate().isAfter(appointmentDTO.getEndDate())) {
            throw new IllegalArgumentException(Messages.START_DATE_BEFORE_END);
        }

        final Optional<Appointment> existingAppointment = this.appointmentRepository.checkAppointment(
                appointmentDTO.getPractitionerId(), appointmentDTO.getStartDate(), appointmentDTO.getEndDate()
        );
        if(existingAppointment.isPresent()) {
            throw new IllegalArgumentException(Messages.TIME_SLOT_ALREADY_BOOKED);
        }
        final Appointment appointment = AppointmentDTOMapper.MAPPER.toEntity(appointmentDTO);
        this.appointmentRepository.save(appointment);

        this.proAvailabilityService.generateAvailabilities(appointmentDTO.getPractitionerId());

        return AppointmentDTOMapper.MAPPER.toDTO(appointment);

    }


}

