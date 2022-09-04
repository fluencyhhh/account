package com.zhangjingbo.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "BALANCE")
public class Balance {
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField(value = "OPERATETIME")
    private Date operateTime;

    @TableField(value = "BALANCE")
    private BigDecimal balance;

    @TableField(value = "OPERATEACCOUNTID")
    private String operateAccountId;

    @TableField(value = "BALANCETYPE")
    private String balanceType;

}