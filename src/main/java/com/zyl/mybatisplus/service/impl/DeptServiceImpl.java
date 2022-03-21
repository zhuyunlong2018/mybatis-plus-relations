/*
 *
 * Copyright (c) 2020-2022, Java知识图谱 (http://www.altitude.xin).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.zyl.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyl.mybatisplus.domain.Dept;
import com.zyl.mybatisplus.domain.User;
import com.zyl.mybatisplus.mapper.DeptMapper;
import com.zyl.mybatisplus.mapper.UserMapper;
import com.zyl.mybatisplus.relations.Relations;
import com.zyl.mybatisplus.service.IDeptService;
import com.zyl.mybatisplus.service.IUserService;
import com.zyl.mybatisplus.entity.vo.DeptVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.altitude.cms.common.util.EntityUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

/**
 * @author explore
 * @since 2021/05/24 11:09
 **/
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {
    @Autowired
    private UserMapper userMapper;
    
    
    @Autowired
    private IUserService userService;
    
    /**
     * 查询单个部门（其中一个部门有多个用户）
     */
    @Override
    public DeptVo getOneDept(Integer deptId) {
        // 查询部门基础信息
        LambdaQueryWrapper<Dept> wrapper = Wrappers.lambdaQuery(Dept.class).eq(Dept::getDeptId, deptId);
        DeptVo deptVo = EntityUtils.toObj(getOne(wrapper), DeptVo::new);
//        Relations.with(deptVo, DeptVo::getUsers);
        Relations.with(deptVo)
                .bind(DeptVo::getUsers, userWrapper -> userWrapper.eq(User::getUserId, 4));
        return deptVo;
    }
    
    /**
     * 查询多个部门（其中一个部门有多个用户）
     */
    @Override
    public List<DeptVo> getDeptByList() {
        // 按条件查询部门信息
        List<DeptVo> deptVos = EntityUtils.toList(list(Wrappers.emptyWrapper()), DeptVo::new);
//        Relations.with(deptVos, DeptVo::getUsers);
        Relations.with(deptVos).bind(DeptVo::getUsers, wrapper -> wrapper.eq(User::getUserId, 1));
        return deptVos;
    }
    
    /**
     * 分页查询部门信息（其中一个部门有多个用户）
     */
    @Override
    public IPage<DeptVo> getDeptByPage(Page<Dept> page) {
        // 按条件查询部门信息
        IPage<DeptVo> deptVoPage = EntityUtils.toPage(page(page, Wrappers.emptyWrapper()), DeptVo::new);
        if (deptVoPage.getRecords().size() > 0) {
            addUserInfo(deptVoPage);
        }
        return deptVoPage;
    }
    
    private void addUserInfo(IPage<DeptVo> deptVoPage) {
        // 准备deptId方便批量查询用户信息
        Set<Integer> deptIds = EntityUtils.collectList(deptVoPage.getRecords(), Dept::getDeptId, toSet());
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class).in(User::getDeptId, deptIds);
        Map<Integer, List<User>> hashMap = userService.list(wrapper).stream().collect(groupingBy(User::getDeptId));
        // 合并结果，构造Vo，添加集合列表
        deptVoPage.convert(e -> e.setUsers(hashMap.get(e.getDeptId())));
    }
}
