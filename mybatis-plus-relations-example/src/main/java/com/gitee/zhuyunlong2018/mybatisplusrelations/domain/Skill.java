package com.gitee.zhuyunlong2018.mybatisplusrelations.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("r_skill")
public class Skill extends Model<Skill> {

    @TableId()
    private Integer id;
    private String name;

    public Skill(Skill skill) {
        if (skill != null) {
            this.id = skill.getId();
            this.name = skill.getName();
        }
    }
}
