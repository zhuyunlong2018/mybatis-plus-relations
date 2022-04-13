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
import com.zyl.mybatisplus.relations.Relations;
import com.zyl.mybatisplus.service.IDeptService;
import com.zyl.mybatisplus.entity.vo.DeptVo;
import org.springframework.stereotype.Service;
import xin.altitude.cms.common.util.EntityUtils;

import java.util.List;

/**
 * @author explore
 * @since 2021/05/24 11:09
 **/
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {
    
    /**
     * 查询单个部门（其中一个部门有多个用户）
     */
    @Override
    public DeptVo getOneDept(Integer deptId) {
        // 查询部门基础信息
        LambdaQueryWrapper<Dept> wrapper = Wrappers.lambdaQuery(Dept.class).eq(Dept::getDeptId, deptId);
        DeptVo deptVo = EntityUtils.toObj(getOne(wrapper), DeptVo::new);
//        Relations.withMany(deptVo, DeptVo::getUsers);
        Relations.withMany(deptVo)
                .bindMany(DeptVo::getUsers, userWrapper -> userWrapper.eq(User::getUserId, 4));
        return deptVo;
    }
    
    /**
     * 查询多个部门（其中一个部门有多个用户）
     */
    @Override
    public List<DeptVo> getDeptByList() {
        // 按条件查询部门信息
        List<DeptVo> deptVos = EntityUtils.toList(list(Wrappers.emptyWrapper()), DeptVo::new);
//        Relations.withMany(deptVos, DeptVo::getUsers);
        Relations.withMany(deptVos).bindMany(DeptVo::getUsers, wrapper -> wrapper.eq(User::getUserId, 1));
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
            Relations.withMany(deptVoPage.getRecords(), DeptVo::getUsers);
        }
        return deptVoPage;
    }
}
