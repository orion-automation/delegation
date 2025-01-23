package com.eorion.bo.enhancement.delegation.domain.dto.inbound;

import lombok.Data;

@Data
public class DelegationManagementUpdateDTO {

    private String delegateName;

    private String delegateToUserId;

    private String delegateToUserName;

    private Long startDateTime;

    private Long endDateTime;

    private String groups;
}
