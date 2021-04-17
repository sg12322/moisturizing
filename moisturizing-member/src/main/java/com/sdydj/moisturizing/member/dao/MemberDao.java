package com.sdydj.moisturizing.member.dao;

import com.sdydj.moisturizing.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 03:19:39
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
