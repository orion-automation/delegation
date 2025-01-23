package com.eorion.bo.enhancement.delegation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eorion.bo.enhancement.delegation.domain.entity.Delegation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DelegationManagementMapper extends BaseMapper<Delegation> {

    List<Delegation> selectDelegationPageList(@Param("delegateNameLike") String delegateNameLike, @Param("ownerUserId") String ownerUserId, @Param("tenant") String tenant,
                                              @Param("delegateToUserId") String delegateToUserId, @Param("delegateToUserNameLike") String delegateToUserNameLike,
                                              @Param("startDateTimeAfter") Long startDateTimeAfter, @Param("startDateTimeBefore") Long startDateTimeBefore, @Param("endDateTimeBefore") Long endDateTimeBefore,
                                              @Param("endDateTimeAfter") Long endDateTimeAfter, @Param("status") String status, @Param("curTs") Long curTs,
                                              @Param("groups") List<String> groups, @Param("orderSort") String orderSort,
                                              @Param("firstResult") Integer firstResult, @Param("maxResults") Integer maxResults);

    Long selectDelegationCount(@Param("delegateNameLike") String delegateNameLike, @Param("ownerUserId") String ownerUserId, @Param("tenant") String tenant,
                               @Param("delegateToUserId") String delegateToUserId, @Param("delegateToUserNameLike") String delegateToUserNameLike,
                               @Param("startDateTimeAfter") Long startDateTimeAfter, @Param("startDateTimeBefore") Long startDateTimeBefore, @Param("endDateTimeBefore") Long endDateTimeBefore,
                               @Param("endDateTimeAfter") Long endDateTimeAfter, @Param("status") String status, @Param("curTs") Long curTs,
                               @Param("groups") List<String> groups);
}
