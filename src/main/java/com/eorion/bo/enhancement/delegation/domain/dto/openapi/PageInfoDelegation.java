package com.eorion.bo.enhancement.delegation.domain.dto.openapi;

import com.eorion.bo.enhancement.delegation.domain.commom.PageInfo;
import com.eorion.bo.enhancement.delegation.domain.entity.Delegation;

import java.util.List;

public class PageInfoDelegation extends PageInfo<Delegation> {
    public PageInfoDelegation(Long total, List<Delegation> data) {
        super(total, data);
    }
}
