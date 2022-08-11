package com.zhangjingbo.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.sql.Timestamp;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "LOGIN_LOG")
public class LoginLog {
    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 用户名
     */
    @TableField(value = "USERNAME")
    private String username;

    @TableField(value = "LOGIN_TIME")
    private Timestamp loginTime;

    /**
     * 是否有效
     */
    @TableField(value = "ISLOGINED")
    private String islogined;

    /**
     * 权限
     */
    @TableField(value = "USERRIGHT")
    private String userright;
}