package com.eorion.bo.enhancement.delegation.adapter.outbound;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eorion.bo.enhancement.delegation.domain.entity.Delegation;
import com.eorion.bo.enhancement.delegation.mapper.DelegationManagementMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class DelegationManagementRepository extends ServiceImpl<DelegationManagementMapper, Delegation> {

}
