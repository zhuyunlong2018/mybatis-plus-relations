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

package com.gitee.zhuyunlong2018.mybatisplusrelations.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.zhuyunlong2018.mybatisplusrelations.domain.Dept;
import com.gitee.zhuyunlong2018.mybatisplusrelations.mapper.DeptMapper;
import com.gitee.zhuyunlong2018.mybatisplusrelations.Relations;
import com.gitee.zhuyunlong2018.mybatisplusrelations.utils.EntityUtils;
import com.gitee.zhuyunlong2018.mybatisplusrelations.service.IDeptService;
import com.gitee.zhuyunlong2018.mybatisplusrelations.entity.vo.DeptVO;
import org.springframework.stereotype.Service;

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
    public DeptVO getOneDept(Integer deptId) {
        // 查询部门基础信息
        LambdaQueryWrapper<Dept> wrapper = Wrappers.lambdaQuery(Dept.class).eq(Dept::getId, deptId);
        DeptVO deptVo = EntityUtils.toObj(getOne(wrapper), DeptVO::new);
        Relations.with(deptVo)
                .bindMany(DeptVO::getUsers)
                .end();
        return deptVo;
    }

    /**
     * 查询多个部门（其中一个部门有多个用户）
     */
    @Override
    public List<DeptVO> getDeptByList() {
        // 按条件查询部门信息
        List<DeptVO> deptVos = EntityUtils.toList(list(Wrappers.emptyWrapper()), DeptVO::new);
        Relations.with(deptVos)
                .bindMany(DeptVO::getUsers)
                .end();
        return deptVos;
    }

    /**
     * 分页查询部门信息（其中一个部门有多个用户）
     */
    @Override
    public IPage<DeptVO> getDeptByPage(Page<Dept> page) {
        // 按条件查询部门信息
        IPage<DeptVO> deptVoPage = EntityUtils.toPage(page(page, Wrappers.emptyWrapper()), DeptVO::new);
        Relations.with(deptVoPage.getRecords())
                .bindMany(DeptVO::getUsers)
                .end();
        return deptVoPage;
    }
}
