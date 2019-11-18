package com.kidand.oauth2.server.service;

import com.kidand.oauth2.server.domain.TbPermission;

import java.util.List;

public interface TbPermissionService{
    List<TbPermission> selectByUserId(Long userId);

}
