package com.ugor.scw.project.mapper;

import com.ugor.scw.project.bean.TProjectImages;
import com.ugor.scw.project.bean.TProjectImagesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TProjectImagesMapper {
    long countByExample(TProjectImagesExample example);

    int deleteByExample(TProjectImagesExample example);

    int insert(TProjectImages record);

    int insertSelective(TProjectImages record);

    List<TProjectImages> selectByExample(TProjectImagesExample example);

    int updateByExampleSelective(@Param("record") TProjectImages record, @Param("example") TProjectImagesExample example);

    int updateByExample(@Param("record") TProjectImages record, @Param("example") TProjectImagesExample example);
}