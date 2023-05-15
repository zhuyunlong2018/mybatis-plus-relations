package com.gitee.zhuyunlong2018.mybatisplusrelations.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("r_user_skill_relation")
public class UserSkillRelation extends Model<UserSkillRelation> {

    @TableId()
    private Integer id;
    private Integer userId;
    private Integer skillId;
    private Integer score;
}
