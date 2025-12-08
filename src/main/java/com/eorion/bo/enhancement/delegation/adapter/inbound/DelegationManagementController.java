package com.eorion.bo.enhancement.delegation.adapter.inbound;

import com.eorion.bo.enhancement.delegation.domain.commom.PageInfo;
import com.eorion.bo.enhancement.delegation.domain.dto.openapi.PageInfoDelegation;
import com.eorion.bo.enhancement.delegation.domain.dto.inbound.DelegationManagementSaveDTO;
import com.eorion.bo.enhancement.delegation.domain.dto.inbound.DelegationManagementUpdateDTO;
import com.eorion.bo.enhancement.delegation.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.delegation.domain.entity.Delegation;
import com.eorion.bo.enhancement.delegation.domain.exception.RequestParamException;
import com.eorion.bo.enhancement.delegation.domain.exception.UpdateFailedException;
import com.eorion.bo.enhancement.delegation.service.DelegationManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enhancement/delegation")
@Tag(name = "Delegation Management")
public class DelegationManagementController {

    private final DelegationManagementService delegationManagementService;

    public DelegationManagementController(DelegationManagementService delegationManagementService) {
        this.delegationManagementService = delegationManagementService;
    }

    @Operation(summary = "Get delegation information by id")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Delegation.class))
            )
    )
    @GetMapping("/{id}")
    public Delegation getDelegationManagement(
            @Parameter(
                    description = "delegation id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        return delegationManagementService.selectById(id);
    }

    @Operation(summary = "Create delegation information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IdDTO.class))),
            @ApiResponse(responseCode = "400", description = "Request body validation failed"),
    })
    @PostMapping
    public IdDTO<?> createDelegationManagement(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Delegation information to create", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DelegationManagementSaveDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "delegateName": "delegateName",
                                        "ownerUserId": "demo",
                                        "ownerUserName": "demo",
                                        "delegateToUserId": "userId",
                                        "delegateToUserName":"userName",
                                        "startDateTime": 1763683200000,
                                        "endDateTime": 1763769600000,
                                        "groups":"group1,group2",
                                        "tenant":"tenant"
                                    }""")))
            @Valid @RequestBody DelegationManagementSaveDTO saveDTO) throws RequestParamException {
        return delegationManagementService.save(saveDTO);
    }

    @Operation(summary = "Update delegation information")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request body validation failed"),
            @ApiResponse(responseCode = "500", description = "Error when updating"),
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDelegationManagement(
            @Parameter(
                    description = "delegation id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Delegation information to update", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DelegationManagementSaveDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "delegateName": "delegateName",
                                        "delegateToUserId": "userId",
                                        "delegateToUserName":"userName",
                                        "startDateTime": 1763683200000,
                                        "endDateTime": 1763769600000,
                                        "groups":"group1,group2"
                                    }""")))
            @Valid @RequestBody DelegationManagementUpdateDTO updateDTO) throws UpdateFailedException {
        delegationManagementService.updateById(id, updateDTO);
    }

    @Operation(summary = "Delete delegation information")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Error when deleting"),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDelegationManagement(
            @Parameter(
                    description = "delegation id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) throws UpdateFailedException {
        delegationManagementService.deleteById(id);
    }

    @Operation(summary = "Get delegator's delegation information")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PageInfoDelegation.class)
                    ))
    )
    @GetMapping
    public PageInfo<Delegation> getMyDelegationManagements(
            @Parameter(
                    description = "Optional delegation name wildcard condition",
                    schema = @Schema(type = "string", maxLength = 255)
            )
            @RequestParam(value = "delegateNameLike", required = false) String delegateNameLike,
            @Parameter(
                    description = "Owner's user id condition",
                    required = true,
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @RequestParam(value = "ownerUserId") String ownerUserId,
            @Parameter(
                    description = "Tenant condition",
                    required = true,
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @RequestParam(value = "tenant") String tenant,
            @Parameter(
                    description = "Optional delegation to user name wildcard condition",
                    schema = @Schema(type = "string", maxLength = 255)
            )
            @RequestParam(value = "delegateToUserNameLike", required = false) String delegateToUserNameLike,
            @Parameter(
                    description = "Optional start timestamp after condition",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @RequestParam(value = "startDateTimeAfter", required = false) Long startDateTimeAfter,
            @Parameter(
                    description = "Optional start timestamp before condition",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @RequestParam(value = "startDateTimeBefore", required = false) Long startDateTimeBefore,
            @Parameter(
                    description = "Optional end timestamp after condition",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @RequestParam(value = "endDateTimeAfter", required = false) Long endDateTimeAfter,
            @Parameter(
                    description = "Optional end timestamp before condition",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @RequestParam(value = "endDateTimeBefore", required = false) Long endDateTimeBefore,
            @Parameter(
                    description = "Optional status condition, 1 for in the period, 0 for expired",
                    schema = @Schema(allowableValues = {"0", "1"})
            )
            @RequestParam(value = "status", required = false) String status,
            @Parameter(
                    description = "Optional group condition",
                    schema = @Schema(type = "string", maxLength = 255)
            )
            @RequestParam(value = "groupsIn", required = false) String groupsIn,
            @Parameter(
                    description = "Order by field",
                    schema = @Schema(type = "string")
            )
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @Parameter(
                    description = "Sort condition",
                    schema = @Schema(allowableValues = {"asc", "desc"})
            )
            @RequestParam(value = "sort", required = false, defaultValue = "asc") String sort,
            @Parameter(
                    description = "Zero-based page index (0..N)",
                    example = "0",
                    schema = @Schema(type = "integer", format = "int32", defaultValue = "0")
            )
            @RequestParam(value = "firstResult", required = false, defaultValue = "0") Integer firstResult,
            @Parameter(
                    description = "The size of the page to be returned",
                    example = "10",
                    schema = @Schema(type = "integer", format = "int32", defaultValue = "2147483647")
            )
            @RequestParam(value = "maxResults", required = false, defaultValue = "2147483647") Integer maxResults
    ) {
        return delegationManagementService.getMyDelegationManagements(delegateNameLike, ownerUserId, tenant, delegateToUserNameLike,
                startDateTimeAfter, startDateTimeBefore, endDateTimeBefore, endDateTimeAfter, status, groupsIn, orderBy, sort, firstResult, maxResults);
    }

    @Operation(summary = "Get delegatee's delegation information")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PageInfoDelegation.class)
                    ))
    )
    @GetMapping("/granted")
    public PageInfo<Delegation> getGrantedToMeDelegationManagements(
            @Parameter(
                    description = "Optional delegation name wildcard condition",
                    schema = @Schema(type = "string", maxLength = 255)
            )
            @RequestParam(value = "delegateNameLike", required = false) String delegateNameLike,
            @Parameter(
                    description = "Tenant condition",
                    required = true,
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @RequestParam(value = "tenant") String tenant,
            @Parameter(
                    description = "Delegatee's user id condition",
                    required = true,
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @RequestParam(value = "delegateToUserId") String delegateToUserId,
            @Parameter(
                    description = "Optional start timestamp after condition",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @RequestParam(value = "startDateTimeAfter", required = false) Long startDateTimeAfter,
            @Parameter(
                    description = "Optional start timestamp before condition",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @RequestParam(value = "startDateTimeBefore", required = false) Long startDateTimeBefore,
            @Parameter(
                    description = "Optional end timestamp after condition",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @RequestParam(value = "endDateTimeAfter", required = false) Long endDateTimeAfter,
            @Parameter(
                    description = "Optional end timestamp before condition",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @RequestParam(value = "endDateTimeBefore", required = false) Long endDateTimeBefore,
            @Parameter(
                    description = "Optional status condition, 1 for in the period, 0 for expired",
                    schema = @Schema(allowableValues = {"0", "1"})
            )
            @RequestParam(value = "status", required = false) String status,
            @Parameter(
                    description = "Order by field",
                    schema = @Schema(type = "string")
            )
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @Parameter(
                    description = "Sort condition",
                    schema = @Schema(allowableValues = {"asc", "desc"})
            )
            @RequestParam(value = "sort", required = false, defaultValue = "asc") String sort,
            @Parameter(
                    description = "Zero-based page index (0..N)",
                    example = "0",
                    schema = @Schema(type = "integer", format = "int32", defaultValue = "0")
            )
            @RequestParam(value = "firstResult", required = false, defaultValue = "0") Integer firstResult,
            @Parameter(
                    description = "The size of the page to be returned",
                    example = "10",
                    schema = @Schema(type = "integer", format = "int32", defaultValue = "2147483647")
            )
            @RequestParam(value = "maxResults", required = false, defaultValue = "2147483647") Integer maxResults
    ) {
        return delegationManagementService.getGrantedToMeDelegationManagements(delegateNameLike, tenant, delegateToUserId, startDateTimeBefore,
                startDateTimeAfter, endDateTimeBefore, endDateTimeAfter, status, orderBy, sort, firstResult, maxResults);
    }
}
