package com.etiya.telekomaktivasyon.service.mapper;

import com.etiya.telekomaktivasyon.dto.PackageResponseDto;
import com.etiya.telekomaktivasyon.entity.PackageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PackageMapper {
    PackageResponseDto toDto(PackageEntity entity);
}
