package org.example.system.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.system.domin.SysApi;
import org.example.system.mapper.SysApiMapper;
import org.example.system.service.SysApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class SysApiServiceImpl implements SysApiService {

    @Autowired
    private SysApiMapper sysApiMapper;

    @Override
    public List<SysApi> findAllApis() {
        return sysApiMapper.findAllApis();
    }

    @Override
    public SysApi findApiById(Long id) {
        return sysApiMapper.findApiById(id);
    }

    @Override
    public int addApi(SysApi sysApi) {
        return sysApiMapper.addApi(sysApi);

    }

    @Override
    public int updateApi(SysApi sysApi) {
        return sysApiMapper.updateApi(sysApi);
    }

    @Override
    public int deleteApi(Long[] ids) {

        int rows = 0;
        for (Long id : ids) {
            rows += sysApiMapper.deleteApi(id);
        }
        return rows;

    }
}
