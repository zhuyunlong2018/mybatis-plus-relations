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

package com.gitee.zhuyunlong2018.mybatisplusrelations.entity.vo;

import com.gitee.zhuyunlong2018.mybatisplusrelations.domain.Dept;
import com.gitee.zhuyunlong2018.mybatisplusrelations.domain.UserSkillRelation;
import com.gitee.zhuyunlong2018.mybatisplusrelations.domain.User;
import com.gitee.zhuyunlong2018.mybatisplusrelations.annotations.BindMany;
import com.gitee.zhuyunlong2018.mybatisplusrelations.annotations.BindOne;
import com.gitee.zhuyunlong2018.mybatisplusrelations.annotations.ManyBindMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @author explore
 */
@Data
@NoArgsConstructor
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
    private List<SkillVO> skills;

    @BindMany(localProperty = "id", foreignProperty = "userId")
    private List<UserSkillVO> userSkills;

    public UserVO(User user) {
        super(user);
    }

    /**
     * 中间表迭代器 relations的size总是保持和skills的size一样
     * @param relations
     */
    public void setUserSkillScope(List<UserSkillRelation> relations) {
        for (int i = 0; i < relations.size(); i++) {
            this.skills.get(i).setScore(relations.get(i).getScore());
        }
    }
}