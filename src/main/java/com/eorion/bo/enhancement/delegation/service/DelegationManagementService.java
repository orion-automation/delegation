package com.eorion.bo.enhancement.delegation.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eorion.bo.enhancement.delegation.adapter.outbound.DelegationManagementRepository;
import com.eorion.bo.enhancement.delegation.domain.DelegationManagementStructureMapper;
import com.eorion.bo.enhancement.delegation.domain.commom.PageInfo;
import com.eorion.bo.enhancement.delegation.domain.dto.inbound.DelegationManagementSaveDTO;
import com.eorion.bo.enhancement.delegation.domain.dto.inbound.DelegationManagementUpdateDTO;
import com.eorion.bo.enhancement.delegation.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.delegation.domain.entity.Delegation;
import com.eorion.bo.enhancement.delegation.domain.exception.RequestParamException;
import com.eorion.bo.enhancement.delegation.domain.exception.UpdateFailedException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service
@AllArgsConstructor
public class DelegationManagementService {

    private final DelegationManagementRepository repository;
    private final IdentityService identityService;
    private final DelegationManagementStructureMapper structureMapper;

    public Delegation selectById(Long id) {
        return repository.getById(id);
    }

    public IdDTO<Long> save(DelegationManagementSaveDTO saveDTO) throws RequestParamException {

        if (Objects.nonNull(saveDTO) && saveDTO.getStartDateTime() > saveDTO.getEndDateTime()) {
            throw new RequestParamException("开始时间不能大于结束时间！");
        }
        var management = structureMapper.saveDtoToEntity(saveDTO);
        repository.save(management);
        return new IdDTO<>(management.getId());
    }

    public void updateById(Long id, DelegationManagementUpdateDTO updateDTO) throws UpdateFailedException {
        String userId = identityService.getCurrentAuthentication().getUserId();
        assert userId != null;
        var db = repository.getById(id);
        if (Objects.nonNull(db) && userId.equals(db.getCreatedBy())) {
            var management = structureMapper.updateDtoToEntity(updateDTO);
            management.setId(id);
            try {
                repository.updateById(management);
            } catch (RuntimeException e) {
                throw new UpdateFailedException(e.getMessage());
            }
        } else {
            throw new UpdateFailedException("请检查当前的资源是否存在或者当前登陆用户是否是资源创建者");
        }

    }

    public void deleteById(Long id) throws UpdateFailedException {
        String userId = identityService.getCurrentAuthentication().getUserId();
        assert userId != null;
        var db = repository.getById(id);
        if (Objects.nonNull(db) && userId.equals(db.getCreatedBy())) {
            try {
                repository.removeById(id);
            } catch (RuntimeException e) {
                throw new UpdateFailedException(e.getMessage());
            }
        } else {
            throw new UpdateFailedException("请检查当前的资源是否存在或者当前登陆用户是否是资源创建者");
        }
    }


    public PageInfo<Delegation> getMyDelegationManagements(String delegateNameLike, String ownerUserId, String tenant,
                                                           String delegateToUserNameLike, Long startDateTimeAfter,
                                                           Long startDateTimeBefore, Long endDateTimeBefore,
                                                           Long endDateTimeAfter, String status, String groupsIn, String orderBy,
                                                           String sort, Integer firstResult, Integer maxResults) {


        Page<Delegation> page = new Page<>(firstResult, maxResults);
        LambdaQueryWrapper<Delegation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(ownerUserId), Delegation::getOwnerUserId, ownerUserId)
                .eq(StringUtils.isNotEmpty(tenant), Delegation::getTenant, tenant)
                .like(StringUtils.isNotEmpty(delegateNameLike), Delegation::getDelegateName, delegateNameLike)
                .like(StringUtils.isNotEmpty(delegateToUserNameLike), Delegation::getDelegateToUserName, delegateToUserNameLike)
                .le(startDateTimeBefore != null, Delegation::getStartDateTime, startDateTimeBefore)
                .ge(startDateTimeAfter != null, Delegation::getStartDateTime, startDateTimeAfter)
                .le(endDateTimeBefore != null, Delegation::getEndDateTime, endDateTimeBefore)
                .ge(endDateTimeAfter != null, Delegation::getEndDateTime, endDateTimeAfter);

        if (StringUtils.isNotEmpty(groupsIn)) {
            var collect = Arrays.stream(groupsIn.split(",")).map(String::trim).toList();
            StringBuilder sql = new StringBuilder("EXISTS (SELECT 1 FROM regexp_split_to_table(USER_GROUPS, ',') AS a WHERE a IN (");
            for (int i = 0; i < collect.size(); i++) {
                sql.append("'").append(collect.get(i)).append("'");
                if (i != (collect.size() - 1)) {
                    sql.append(",");
                }
            }
            sql.append("))");
            queryWrapper.and(i -> i.apply(sql.toString()));

        }
        //1-表示在有效期内 0-表示已过期
        if (StringUtils.isNotEmpty(status)) {
            var l = System.currentTimeMillis();
            if ("1".equals(status)) {
                queryWrapper.ge(Delegation::getEndDateTime, l);
            } else if ("0".equals(status)) {
                queryWrapper.le(Delegation::getEndDateTime, l);
            }
        }
        if ("startDateTime".equals(orderBy) && StringUtils.isNotEmpty(sort)) {
            queryWrapper.last("order by START_DATE_TS " + sort);
        }

        var result = repository.page(page, queryWrapper);
        return new PageInfo<>(result.getTotal(), result.getRecords());
    }

    public PageInfo<Delegation> getGrantedToMeDelegationManagements(String delegateNameLike, String tenant, String delegateToUserId,
                                                                    Long startDateTimeBefore, Long startDateTimeAfter, Long endDateTimeBefore,
                                                                    Long endDateTimeAfter, String status, String orderBy, String sort,
                                                                    Integer firstResult, Integer maxResults) {
        Page<Delegation> page = new Page<>(firstResult, maxResults);
        LambdaQueryWrapper<Delegation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(StringUtils.isNotEmpty(delegateToUserId), Delegation::getDelegateToUserId, delegateToUserId)
                .eq(StringUtils.isNotEmpty(tenant), Delegation::getTenant, tenant)
                .like(StringUtils.isNotEmpty(delegateNameLike), Delegation::getDelegateName, delegateNameLike)
                .le(startDateTimeBefore != null, Delegation::getStartDateTime, startDateTimeBefore)
                .ge(startDateTimeAfter != null, Delegation::getStartDateTime, startDateTimeAfter)
                .le(endDateTimeBefore != null, Delegation::getEndDateTime, endDateTimeBefore)
                .ge(endDateTimeAfter != null, Delegation::getEndDateTime, endDateTimeAfter);
        //1-表示在有效期内 0-表示已过期
        if (StringUtils.isNotEmpty(status)) {
            var l = System.currentTimeMillis();
            if ("1".equals(status)) {
                queryWrapper.ge(Delegation::getEndDateTime, l);
            } else if ("0".equals(status)) {
                queryWrapper.le(Delegation::getEndDateTime, l);
            }
        }
        if ("startDateTime".equals(orderBy) && StringUtils.isNotEmpty(sort)) {
            queryWrapper.last("order by START_DATE_TS " + sort);
        }

        var result = repository.page(page, queryWrapper);
        return new PageInfo<>(result.getTotal(), result.getRecords());
    }
}
