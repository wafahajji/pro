package com.maiia.pro.controller;

import com.maiia.pro.model.dto.AppointmentDTO;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.mapper.ws.AppointmentRequestMapper;
import com.maiia.pro.model.AppointmentRequest;
import com.maiia.pro.service.ProAppointmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProAppointmentController {


    private final ProAppointmentService proAppointmentService;

    @Autowired
    public ProAppointmentController(final ProAppointmentService proAppointmentService) {
       this.proAppointmentService = proAppointmentService;
    }


    @ApiOperation(value = "Get appointments by practitionerId")
    @GetMapping("/{practitionerId}")
    public List<Appointment> getAppointmentsByPractitioner(@PathVariable final Integer practitionerId) {
        return this.proAppointmentService.findByPractitionerId(practitionerId);
    }

    @ApiOperation(value = "Get all appointments")
    @GetMapping
    public List<Appointment> getAppointments() {
        return this.proAppointmentService.findAll();
    }

    @ApiOperation(value = "create appointment")
    @PostMapping("/create")
    public ResponseEntity createAppointment(@RequestBody final AppointmentRequest request) {
        try {
            final AppointmentDTO appointmentDTO = AppointmentRequestMapper.MAPPER.toDTO(request);
            this.proAppointmentService.createAppointment(appointmentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
