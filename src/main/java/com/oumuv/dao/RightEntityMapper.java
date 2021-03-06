package com.oumuv.dao;

import java.util.List;

import com.oumuv.entity.RightEntity;

public interface RightEntityMapper {
    int deleteByPrimaryKey(Long rightCode);

    int insert(RightEntity record);

    int insertSelective(RightEntity record);

    RightEntity selectByPrimaryKey(Long rightCode);

    int updateByPrimaryKeySelective(RightEntity record);

    int updateByPrimaryKey(RightEntity record);

	List<RightEntity> getRightByUId(long uid);
	
	List<RightEntity> getRightByParentId(long pid);
}