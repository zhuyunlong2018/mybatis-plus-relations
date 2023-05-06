package com.zyl.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("tb_student")
public class Student extends Model<Student> {

    @TableId()
    private Integer stuId;
    private String stuName;

    public Student(Student student) {
        if (student != null) {
            this.stuId = student.getStuId();
            this.stuName = student.getStuName();
        }
    }
}
