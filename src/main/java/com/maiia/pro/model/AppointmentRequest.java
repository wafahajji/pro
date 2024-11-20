package com.maiia.pro.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest implements Serializable {

    private static final long serialVersionUID = 3478923478394723847L;

    private Integer id;

    private Integer patientId;

    private Integer practitionerId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

}