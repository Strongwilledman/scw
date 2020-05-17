package com.ugor.scw.user.mapper;

import com.ugor.scw.user.bean.TMemberCert;
import com.ugor.scw.user.bean.TMemberCertExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TMemberCertMapper {
    long countByExample(TMemberCertExample example);

    int deleteByExample(TMemberCertExample example);

    int insert(TMemberCert record);

    int insertSelective(TMemberCert record);

    List<TMemberCert> selectByExample(TMemberCertExample example);

    int updateByExampleSelective(@Param("record") TMemberCert record, @Param("example") TMemberCertExample example);

    int updateByExample(@Param("record") TMemberCert record, @Param("example") TMemberCertExample example);
}