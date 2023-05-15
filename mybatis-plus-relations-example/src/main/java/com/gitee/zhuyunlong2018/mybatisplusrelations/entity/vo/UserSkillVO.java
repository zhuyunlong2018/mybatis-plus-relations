package com.gitee.zhuyunlong2018.mybatisplusrelations.entity.vo;

import com.gitee.zhuyunlong2018.mybatisplusrelations.domain.Skill;
import com.gitee.zhuyunlong2018.mybatisplusrelations.domain.UserSkillRelation;
import com.gitee.zhuyunlong2018.mybatisplusrelations.annotations.BindOne;
import lombok.Data;

@Data
public class UserSkillVO extends UserSkillRelation {

    @BindOne(localProperty = "skillId", foreignProperty = "id")
    private Skill skill;

}
