package com.zyl.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("tb_stu_sub_relation")
public class StuSubRelation extends Model<StuSubRelation> {

    @TableId()
    private Integer id;
    private Integer stuId;
    private Integer subId;
    private Integer score;

    public StuSubRelation(StuSubRelation relation) {
        if (relation != null) {
            this.stuId = relation.getStuId();
            this.subId = relation.getSubId();
            this.score = relation.getScore();
        }
    }
}
