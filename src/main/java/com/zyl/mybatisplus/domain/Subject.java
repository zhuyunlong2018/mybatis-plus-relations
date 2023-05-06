package com.zyl.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("tb_subject")
public class Subject extends Model<Subject> {

    @TableId()
    private Integer subId;
    private String subName;

    public Subject(Subject subject) {
        if (subject != null) {
            this.subId = subject.getSubId();
            this.subName = subject.getSubName();
        }
    }
}
