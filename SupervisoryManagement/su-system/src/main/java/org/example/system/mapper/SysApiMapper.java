package org.example.system.mapper;

import org.example.system.domin.SysApi;

import java.util.List;

public interface SysApiMapper {


    List<SysApi> findAllApis();

    SysApi findApiById(Long id);

    int addApi(SysApi sysApi);

    int updateApi(SysApi sysApi);

    int deleteApi(Long id);
    
    
}
