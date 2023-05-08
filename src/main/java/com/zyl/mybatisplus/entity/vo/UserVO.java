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

package com.zyl.mybatisplus.entity.vo;

import com.zyl.mybatisplus.domain.Dept;
import com.zyl.mybatisplus.domain.Skill;
import com.zyl.mybatisplus.domain.User;
import com.zyl.mybatisplus.domain.UserSkillRelation;
import com.zyl.mybatisplus.relations.annotations.BindMany;
import com.zyl.mybatisplus.relations.annotations.BindOne;
import com.zyl.mybatisplus.relations.annotations.ManyBindMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @author explore
 */
@Data
public class UserVO extends User {

    @BindOne(localProperty = "deptId", foreignProperty = "id")
    private Dept dept;

    @ManyBindMany(
            localProperty = "id",
            foreignProperty = "id",
            linkModel = UserSkillRelation.class,
            linkLocalProperty = "userId",
            linkForeignProperty = "skillId",
            iterateLinkMethod = "setUserSkillScope"
    )
    private List<Skill> skills;

    @BindMany(localProperty = "id", foreignProperty = "userId")
    private List<UserSkillVO> userSkills;

    public UserVO(User user) {
        super(user);
    }

    /**
     * 中间表迭代器
     * @param relations
     */
    public void setUserSkillScope(List<UserSkillRelation> relations) {
        System.out.println("=======================");
        System.out.println(relations);
        System.out.println("=======================");
    }
}