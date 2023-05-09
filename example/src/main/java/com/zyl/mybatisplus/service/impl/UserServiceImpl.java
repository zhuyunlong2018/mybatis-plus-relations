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
import com.zyl.mybatisplus.domain.User;
import com.zyl.mybatisplus.domain.UserSkillRelation;
import com.zyl.mybatisplus.entity.vo.UserSkillVO;
import com.zyl.mybatisplus.entity.vo.UserVO;
import com.zyl.mybatisplus.mapper.UserMapper;
import com.zyl.mybatisplus.relations.Relations;
import com.zyl.mybatisplus.relations.utils.EntityUtils;
import com.zyl.mybatisplus.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * 一个用户对应一个部门
 *
 * @author explore
 * @since 2021/05/24 11:09
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 查询单个用户信息（一个用户对应一个部门）
     */
    @Override
    public UserVO getOneUser(Integer userId) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getId, userId);
        UserVO userVo = EntityUtils.toObj(getOne(wrapper), UserVO::new);
        Relations.with(userVo).bindOne(UserVO::getDept)
                .binder()
                .manyBindMany(UserVO::getSkills)
                .binder()
                .bindMany(UserVO::getUserSkills)
                .deepWith(binder -> {
                    binder.bindOne(UserSkillVO::getSkill)
                            .end();
                })
                .end();
        return userVo;
    }

    /**
     * 批量查询用户信息（一个用户对应一个部门）
     */
    @Override
    public List<UserVO> getUserByList() {
        // 先查询用户信息（表现形式为列表）
        List<User> user = list(Wrappers.emptyWrapper());
        List<UserVO> userVos = user.stream().map(UserVO::new).collect(toList());
        // 此步骤可以有多个
        Relations.with(userVos)
                .bindOne(UserVO::getDept)
                .binder()
                .manyBindMany(UserVO::getSkills)
                .linkQuery((LambdaQueryWrapper<UserSkillRelation> wrapper) -> {
                    wrapper.gt(UserSkillRelation::getScore, 90);
                })
                .end();
        return userVos;
    }

    /**
     * 分页查询用户信息（一个用户对应一个部门）
     */
    @Override
    public IPage<UserVO> getUserByPage(Page<User> page) {
        // 先查询用户信息
        IPage<User> xUserPage = page(page, Wrappers.emptyWrapper());
        // 初始化Vo
        IPage<UserVO> userVoPage = xUserPage.convert(UserVO::new);
        Relations.with(userVoPage.getRecords()).bindOne(UserVO::getDept).end();
        return userVoPage;
    }


}
