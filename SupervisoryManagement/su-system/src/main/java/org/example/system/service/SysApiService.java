package org.example.system.service;

import org.example.system.domin.SysApi;

import java.util.List;

public interface SysApiService {

    List<SysApi> findAllApis();

    SysApi findApiById(Long id);

    int addApi(SysApi sysApi);

    int updateApi(SysApi sysApi);

    int deleteApi(Long[] ids);

}
