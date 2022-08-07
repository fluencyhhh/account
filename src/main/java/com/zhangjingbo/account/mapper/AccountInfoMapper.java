package com.zhangjingbo.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjingbo.account.entity.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountInfoMapper extends BaseMapper {
    int saveAccountInfo(@Param("accountInfo") AccountInfo accountInfo);

    List<AccountInfo> queryAllAccountInfo();
}
