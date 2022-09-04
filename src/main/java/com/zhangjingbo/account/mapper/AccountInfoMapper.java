package com.zhangjingbo.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.form.AccountInfoForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Repository
public interface AccountInfoMapper extends BaseMapper<AccountInfo> {
    int saveAccountInfo(@Param("accountInfo") AccountInfo accountInfo);

    List<AccountInfo> queryAllAccountInfo();

    List<AccountInfo> queryAccountInfoByParam(@Param("accountInfo") AccountInfoForm accountInfo, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<AccountInfo> queryAccountInfoByDate(@Param("startTime") String startTime,@Param("operatorType") String operatorType,@Param("accountType") String accountType);

    List<AccountInfo> queryAccountInfoByDateTime(@Param("startTime") String startTime,@Param("operTime") String operTime,@Param("operatorType") String operatorType,@Param("accountType") String accountType);

    BigDecimal queryBalanceByTime(@Param("startTime") String startTime,@Param("operTime") String operTime,@Param("operatorType") String operatorType,@Param("accountType") String accountType);
}
