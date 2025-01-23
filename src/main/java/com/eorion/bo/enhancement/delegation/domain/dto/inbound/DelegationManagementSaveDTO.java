package com.eorion.bo.enhancement.delegation.domain.dto.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class DelegationManagementSaveDTO {

    @NotBlank(message = "delegateName 不能为空")
    private String delegateName;

    @NotBlank(message = "ownerUserId 不能为空")
    private String ownerUserId;

    @NotBlank(message = "ownerUserName 不能为空")
    private String ownerUserName;

    @NotBlank(message = "delegateToUserId 不能为空")
    private String delegateToUserId;

    @NotBlank(message = "delegateToUserName 不能为空")
    private String delegateToUserName;

    @NotNull(message = "delegateName 不能为空")
    private Long startDateTime;

    @NotNull(message = "endDateTime 不能为空")
    private Long endDateTime;

    @NotBlank(message = "groups 不能为空")
    private String groups;

    @NotBlank(message = "tenant 不能为空")
    private String tenant;
}
