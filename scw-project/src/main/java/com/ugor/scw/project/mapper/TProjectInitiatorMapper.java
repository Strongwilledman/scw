package com.ugor.scw.project.mapper;

import com.ugor.scw.project.bean.TProjectInitiator;
import com.ugor.scw.project.bean.TProjectInitiatorExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TProjectInitiatorMapper {
    long countByExample(TProjectInitiatorExample example);

    int deleteByExample(TProjectInitiatorExample example);

    int insert(TProjectInitiator record);

    int insertSelective(TProjectInitiator record);

    List<TProjectInitiator> selectByExample(TProjectInitiatorExample example);

    int updateByExampleSelective(@Param("record") TProjectInitiator record, @Param("example") TProjectInitiatorExample example);

    int updateByExample(@Param("record") TProjectInitiator record, @Param("example") TProjectInitiatorExample example);
}