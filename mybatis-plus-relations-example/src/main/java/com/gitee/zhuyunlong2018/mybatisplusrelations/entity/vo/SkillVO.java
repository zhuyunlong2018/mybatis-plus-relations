package com.gitee.zhuyunlong2018.mybatisplusrelations.entity.vo;

import com.gitee.zhuyunlong2018.mybatisplusrelations.domain.Skill;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SkillVO extends Skill {

    private Integer score;
}
