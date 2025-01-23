package com.eorion.bo.enhancement.delegation.domain;

import com.eorion.bo.enhancement.delegation.domain.dto.inbound.DelegationManagementSaveDTO;
import com.eorion.bo.enhancement.delegation.domain.dto.inbound.DelegationManagementUpdateDTO;
import com.eorion.bo.enhancement.delegation.domain.entity.Delegation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DelegationManagementStructureMapper {

    Delegation saveDtoToEntity(DelegationManagementSaveDTO dto);

    Delegation updateDtoToEntity(DelegationManagementUpdateDTO dto);
}
