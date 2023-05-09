package com.zyl.mybatisplus.entity.vo;

import com.zyl.mybatisplus.domain.Skill;
import com.zyl.mybatisplus.domain.UserSkillRelation;
import com.zyl.mybatisplus.relations.annotations.BindOne;
import lombok.Data;

@Data
public class UserSkillVO extends UserSkillRelation {

    @BindOne(localProperty = "skillId", foreignProperty = "id")
    private Skill skill;

}
