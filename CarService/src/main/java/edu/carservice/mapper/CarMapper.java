package edu.carservice.mapper;

import edu.carservice.dto.CarDTO;
import edu.carservice.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.DEFAULT,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    CarDTO toCarDTO(Car user);

    List<CarDTO> toCarDTOList(List<Car> cars);
}
