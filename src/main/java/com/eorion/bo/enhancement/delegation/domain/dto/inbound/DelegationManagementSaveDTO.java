package com.eorion.bo.enhancement.delegation.domain.dto.inbound;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DelegationManagementSaveDTO {

    @NotBlank(message = "delegateName 不能为空")
    @Size(max = 255)
    private String delegateName;

    @NotBlank(message = "ownerUserId 不能为空")
    @Size(max = 64)
    private String ownerUserId;

    @NotBlank(message = "ownerUserName 不能为空")
    @Size(max = 255)
    private String ownerUserName;

    @NotBlank(message = "delegateToUserId 不能为空")
    @Size(max = 64)
    private String delegateToUserId;

    @NotBlank(message = "delegateToUserName 不能为空")
    @Size(max = 255)
    private String delegateToUserName;

    @NotNull(message = "startDateTime 不能为空")
    @Min(0)
    private Long startDateTime;

    @NotNull(message = "endDateTime 不能为空")
    @Min(0)
    private Long endDateTime;

    @NotBlank(message = "groups 不能为空")
    @Size(max = 255)
    private String groups;

    @NotBlank(message = "tenant 不能为空")
    @Size(max = 64)
    private String tenant;
}
