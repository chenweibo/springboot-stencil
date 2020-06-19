package com.chen.stencil.service.impl;

import com.chen.stencil.entity.Users;
import com.chen.stencil.mapper.UsersMapper;
import com.chen.stencil.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenweibo
 * @since 2020-06-17
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {


}
