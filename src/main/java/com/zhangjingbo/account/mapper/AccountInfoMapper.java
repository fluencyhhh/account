package com.zhangjingbo.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjingbo.account.entity.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountInfoMapper extends BaseMapper {
    int saveAccountInfo(@Param("accountInfo") AccountInfo accountInfo);
}
