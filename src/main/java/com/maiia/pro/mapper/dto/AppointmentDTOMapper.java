package com.maiia.pro.mapper.dto;

import com.maiia.pro.model.dto.AppointmentDTO;
import com.maiia.pro.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppointmentDTOMapper {

    AppointmentDTOMapper MAPPER = Mappers.getMapper(AppointmentDTOMapper.class);

    Appointment toEntity(AppointmentDTO appointmentDTO);

    AppointmentDTO toDTO(Appointment appointment);

}
