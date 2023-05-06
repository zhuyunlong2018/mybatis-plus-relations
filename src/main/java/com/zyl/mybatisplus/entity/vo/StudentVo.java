package com.zyl.mybatisplus.entity.vo;

import com.zyl.mybatisplus.domain.StuSubRelation;
import com.zyl.mybatisplus.domain.Student;
import com.zyl.mybatisplus.domain.Subject;
import com.zyl.mybatisplus.relations.annotations.ManyBindMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class StudentVo extends Student {

    @ManyBindMany(
            localProperty = "stuId",
            foreignProperty = "subId",
            linkModel = StuSubRelation.class
    )
    private List<Subject> subjects;

    public StudentVo(Student student) {
        super(student);
    }
}
