package com.maiia.pro.mapper.ws;

import com.maiia.pro.model.dto.AppointmentDTO;
import com.maiia.pro.model.AppointmentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppointmentRequestMapper {
    AppointmentRequestMapper MAPPER = Mappers.getMapper(AppointmentRequestMapper.class);

    AppointmentDTO toDTO(AppointmentRequest appointmentRequest);

}