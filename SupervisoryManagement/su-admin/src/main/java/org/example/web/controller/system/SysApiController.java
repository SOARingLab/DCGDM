package org.example.web.controller.system;

import org.example.common.response.ResponseResult;
import org.example.system.domin.SysApi;
import org.example.system.service.impl.SysApiServiceImpl;
import org.example.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SysApiController extends BaseController {

    @Autowired
    SysApiServiceImpl sysApiService;

    @GetMapping("/sysApi")
    public ResponseResult findAllApis() {
        startPage();
        List<SysApi> data = sysApiService.findAllApis();
        return getResult(data);
    }

    @GetMapping("/sysApi/{id}")
    public ResponseResult findApiById(@PathVariable Long id) {
        return getResult(sysApiService.findApiById(id));
    }

    @PostMapping("/sysApi")
    public ResponseResult addApi(@Validated @RequestBody SysApi sysApi) {
        return getResult(sysApiService.addApi(sysApi));
    }

    @PutMapping("/sysApi")
    public ResponseResult updateApi(@Validated @RequestBody SysApi sysApi) {
        return getResult(sysApiService.updateApi(sysApi));
    }

    @DeleteMapping("/sysApi/{ids}")
    public ResponseResult deleteApi(@PathVariable Long[] ids) {
        return getResult(sysApiService.deleteApi(ids));
    }



}
