package com.unicom.mapping;


import java.util.List;

import com.unicom.model.*;

public interface CardInfoMapper {
    public int deleteByPrimaryKey(Integer id);

    public int insert(UnicomInfoVo cardInfo);

    public int insertSelective(UnicomInfoVo cardInfo);

    public UnicomInfoVo selectByPrimaryKey(Integer id);
    
    public List<UnicomInfoVo> selectByWhere(UnicomInfoVo cardInfo);

    public int updateByPrimaryKeySelective(UnicomInfoVo cardInfo);

    public int updateByPrimaryKey(UnicomInfoVo cardInfo);
}