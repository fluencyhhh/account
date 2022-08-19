package com.zhangjingbo.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "BALANCE")
public class Balance {

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private int id;

    /**
     * 时间
     */
    @TableField(value = "operateTime")
    private Date operateTime;

    /**
     * 余额
     */
    @TableField(value = "BALANCE")
    private BigDecimal balance;

    /**
     * 关联账单表id
     */
    @TableField(value = "operateAccountId")
    private int operateAccountId;

    public Balance(){}

    public Balance(int id, Date operateTime, BigDecimal balance, int operateAccountId) {
        this.id = id;
        this.operateTime = operateTime;
        this.balance = balance;
        this.operateAccountId = operateAccountId;
    }
}
