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

package com.gitee.zhuyunlong2018.mybatisplusrelations.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.zhuyunlong2018.mybatisplusrelations.domain.Dept;
import com.gitee.zhuyunlong2018.mybatisplusrelations.entity.vo.DeptVO;

import java.util.List;

/**
 * @author explore
 * @since 2021/12/01 18:09
 **/
public interface IDeptService extends IService<Dept> {
    /**
     * 查询单个部门（其中一个部门有多个用户）
     */
    DeptVO getOneDept(Integer deptId);
    
    /**
     * 查询多个部门（其中一个部门有多个用户）
     */
    List<DeptVO> getDeptByList();
    
    /**
     * 分页查询部门信息（其中一个部门有多个用户）
     */
    IPage<DeptVO> getDeptByPage(Page<Dept> page);
}
