package com.kuyun.common.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuyun.common.bean.SysMenu;
import com.kuyun.common.dao.SysMenuDao;

@Service
public class SysMenuService {

    @Resource
    private SysMenuDao sysMenuDao;

    public SysMenu get(String id) {
        return sysMenuDao.get(id);
    }
}
