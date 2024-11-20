package com.maiia.pro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Integer id;

    private Integer patientId;

    private Integer practitionerId;

    private LocalDateTime startDate;
    
    private LocalDateTime endDate;

}