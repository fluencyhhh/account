package com.zhangjingbo.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjingbo.account.entity.Balance;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Mapper
@Repository
public interface BalanceMapper extends BaseMapper<Balance> {


    BigDecimal queryCurrentBalance();

}
