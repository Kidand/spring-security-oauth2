package com.kidand.oauth2.server.service;

import com.kidand.oauth2.server.domain.TbUser;

public interface TbUserService{

    TbUser getByUsername(String username);

}
