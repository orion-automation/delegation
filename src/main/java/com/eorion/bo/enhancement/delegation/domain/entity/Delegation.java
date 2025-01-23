package com.eorion.bo.enhancement.delegation.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("ENHANCEMENT_DELEGATION")
@EqualsAndHashCode
public class Delegation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * delegate_name
     */
    @TableField("delegate_name")
    private String delegateName;

    /**
     * owner_user_id
     */
    @TableField("owner_user_id")
    private String ownerUserId;

    /**
     * owner_user_name
     */
    @TableField("owner_user_name")
    private String ownerUserName;

    /**
     * delegate_to_user_id
     */
    @TableField("delegate_to_user_id")
    private String delegateToUserId;

    /**
     * delegate_to_user_name
     */
    @TableField("delegate_to_user_name")
    private String delegateToUserName;

    /**
     * start_date_ts
     */
    @TableField("start_date_ts")
    private Long startDateTime;

    /**
     * end_date_ts
     */
    @TableField("end_date_ts")
    private Long endDateTime;

    /**
     * user_groups
     */
    @TableField("user_groups")
    private String groups;

    /**
     * tenant_txt
     */
    @TableField("tenant_txt")
    private String tenant;

    @TableField(value = "CREATED_TS", fill = FieldFill.INSERT)
    private Long createdTs;

    @TableField(value = "UPDATED_TS", fill = FieldFill.INSERT_UPDATE)
    private Long updatedTs;

    @TableField(value = "CREATE_BY_TXT", fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(value = "UPDATED_BY_TXT", fill = FieldFill.UPDATE)
    private String updatedBy;

    @TableField(value = "DELETE_FG")
    @TableLogic(value = "0", delval = "1")
    @JsonIgnore
    private Character deleteFlag;

}