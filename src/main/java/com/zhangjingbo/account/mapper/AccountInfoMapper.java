package com.zhangjingbo.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjingbo.account.entity.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AccountInfoMapper extends BaseMapper<AccountInfo> {
    int saveAccountInfo(@Param("accountInfo") AccountInfo accountInfo);

    List<AccountInfo> queryAllAccountInfo();

    List<AccountInfo> queryAccountInfoByParam(@Param("accountInfo") AccountInfo accountInfo,@Param("startTime") String startTime,@Param("endTime") String endTime);

}
