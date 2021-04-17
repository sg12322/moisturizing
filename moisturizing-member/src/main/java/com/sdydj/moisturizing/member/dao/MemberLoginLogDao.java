package com.sdydj.moisturizing.member.dao;

import com.sdydj.moisturizing.member.entity.MemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 03:19:39
 */
@Mapper
public interface MemberLoginLogDao extends BaseMapper<MemberLoginLogEntity> {
	
}
