package com.kidand.oauth2.server.service.impl;

import com.kidand.oauth2.server.domain.TbUser;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.kidand.oauth2.server.mapper.TbUserMapper;
import com.kidand.oauth2.server.service.TbUserService;
import tk.mybatis.mapper.entity.Example;

@Service
public class TbUserServiceImpl implements TbUserService{

    @Resource
    private TbUserMapper tbUserMapper;

    @Override
    public TbUser getByUsername(String username) {
        Example example = new Example(TbUser.class);
        example.createCriteria().andEqualTo("username", username);

        return tbUserMapper.selectOneByExample(example);
    }
}
