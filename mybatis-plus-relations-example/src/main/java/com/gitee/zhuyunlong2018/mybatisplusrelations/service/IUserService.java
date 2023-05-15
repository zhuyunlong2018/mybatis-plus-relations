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
import com.gitee.zhuyunlong2018.mybatisplusrelations.entity.vo.UserVO;
import com.gitee.zhuyunlong2018.mybatisplusrelations.domain.User;

import java.util.List;

/**
 * @author explore
 * @since 2021/12/01 18:13
 **/
public interface IUserService extends IService<User> {
    /**
     * 查询单个学生信息（一个学生对应一个部门）
     */
    UserVO getOneUser(Integer userId);

    /**
     * 批量查询学生信息（一个学生对应一个部门）
     */
    List<UserVO> getUserByList();

    /**
     * 分页查询学生信息（一个学生对应一个部门）
     */
    IPage<UserVO> getUserByPage(Page<User> page);
}
