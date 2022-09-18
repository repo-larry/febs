package com.febs.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.febs.common.entity.system.Menu;

import java.util.List;

/**
 * @description:
 * @date: 2022/9/18
 **/
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> findUserPermissions(String username);
}
