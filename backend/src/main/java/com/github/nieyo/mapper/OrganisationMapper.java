package com.github.nieyo.mapper;

import com.github.nieyo.dto.OrganisationDTO;
import com.github.nieyo.entity.Organisation;

public class OrganisationMapper {

    private OrganisationMapper(){
    }

    // Domain zu DTO
    public static OrganisationDTO toDto(Organisation organisation) {
        if (organisation == null) {
            return null;
        }
        return OrganisationDTO.builder()
                .id(organisation.id())
                .name(organisation.name())
                .adminUserId(organisation.adminUserId())
                .eventIds(organisation.eventIds())
                .build();
    }

    // DTO zu Domain
    public static Organisation fromDto(OrganisationDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Organisation(
                dto.id(),
                dto.name(),
                dto.adminUserId(),
                dto.eventIds()
        );
    }
}

