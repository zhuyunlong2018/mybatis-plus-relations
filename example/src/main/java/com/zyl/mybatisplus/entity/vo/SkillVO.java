package com.zyl.mybatisplus.entity.vo;

import com.zyl.mybatisplus.domain.Skill;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SkillVO extends Skill {

    private Integer score;
}
