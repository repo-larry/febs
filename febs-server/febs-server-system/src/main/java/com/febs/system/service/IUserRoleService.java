package com.febs.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.febs.common.entity.system.UserRole;

/**
 * @description:
 * @date: 2022/9/18
 **/
public interface IUserRoleService extends IService<UserRole> {

    void deleteUserRolesByRoleId(String[] roleIds);

    void deleteUserRolesByUserId(String[] userIds);
}
