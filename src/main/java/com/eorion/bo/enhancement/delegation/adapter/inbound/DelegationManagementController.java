package com.eorion.bo.enhancement.delegation.adapter.inbound;

import com.eorion.bo.enhancement.delegation.domain.commom.PageInfo;
import com.eorion.bo.enhancement.delegation.domain.dto.inbound.DelegationManagementSaveDTO;
import com.eorion.bo.enhancement.delegation.domain.dto.inbound.DelegationManagementUpdateDTO;
import com.eorion.bo.enhancement.delegation.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.delegation.domain.entity.Delegation;
import com.eorion.bo.enhancement.delegation.domain.exception.RequestParamException;
import com.eorion.bo.enhancement.delegation.domain.exception.UpdateFailedException;
import com.eorion.bo.enhancement.delegation.service.DelegationManagementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enhancement/delegation")
@AllArgsConstructor
public class DelegationManagementController {

    private final DelegationManagementService delegationManagementService;

    @GetMapping("/{id}")
    public Delegation getDelegationManagement(@PathVariable Long id) {
        return delegationManagementService.selectById(id);
    }

    @PostMapping()
    public IdDTO<?> createDelegationManagement(@Valid @RequestBody DelegationManagementSaveDTO saveDTO) throws RequestParamException {
        return delegationManagementService.save(saveDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDelegationManagement(@PathVariable Long id, @RequestBody DelegationManagementUpdateDTO updateDTO) throws UpdateFailedException {
        delegationManagementService.updateById(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDelegationManagement(@PathVariable Long id) throws UpdateFailedException {
        delegationManagementService.deleteById(id);
    }

    @GetMapping("")
    public PageInfo<Delegation> getMyDelegationManagements(
            @RequestParam(value = "delegateNameLike", required = false) String delegateNameLike,
            @RequestParam(value = "ownerUserId") String ownerUserId,
            @RequestParam(value = "tenant") String tenant,
            @RequestParam(value = "delegateToUserNameLike", required = false) String delegateToUserNameLike,
            @RequestParam(value = "startDateTimeAfter", required = false) Long startDateTimeAfter,
            @RequestParam(value = "startDateTimeBefore", required = false) Long startDateTimeBefore,
            @RequestParam(value = "endDateTimeAfter", required = false) Long endDateTimeAfter,
            @RequestParam(value = "endDateTimeBefore", required = false) Long endDateTimeBefore,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "groupsIn", required = false) String groupsIn,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestParam(value = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(value = "firstResult", required = false, defaultValue = "0") Integer firstResult,
            @RequestParam(value = "maxResults", required = false, defaultValue = "2147483647") Integer maxResults
    ) {
        return delegationManagementService.getMyDelegationManagements(delegateNameLike, ownerUserId, tenant, delegateToUserNameLike,
                startDateTimeAfter, startDateTimeBefore, endDateTimeBefore, endDateTimeAfter, status, groupsIn, orderBy, sort, firstResult, maxResults);
    }

    @GetMapping("/granted")
    public PageInfo<Delegation> getGrantedToMeDelegationManagements(
            @RequestParam(value = "delegateNameLike", required = false) String delegateNameLike,
            @RequestParam(value = "tenant") String tenant,
            @RequestParam(value = "delegateToUserId") String delegateToUserId,
            @RequestParam(value = "startDateTimeAfter", required = false) Long startDateTimeAfter,
            @RequestParam(value = "startDateTimeBefore", required = false) Long startDateTimeBefore,
            @RequestParam(value = "endDateTimeAfter", required = false) Long endDateTimeAfter,
            @RequestParam(value = "endDateTimeBefore", required = false) Long endDateTimeBefore,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestParam(value = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(value = "firstResult", required = false, defaultValue = "0") Integer firstResult,
            @RequestParam(value = "maxResults", required = false, defaultValue = "2147483647") Integer maxResults
    ) {
        return delegationManagementService.getGrantedToMeDelegationManagements(delegateNameLike, tenant, delegateToUserId, startDateTimeBefore,
                startDateTimeAfter, endDateTimeBefore, endDateTimeAfter, status, orderBy, sort, firstResult, maxResults);
    }
}
