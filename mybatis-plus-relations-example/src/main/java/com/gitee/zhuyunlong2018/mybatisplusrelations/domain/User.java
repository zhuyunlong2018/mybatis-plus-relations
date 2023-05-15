/*
 *
 * Copyright (c) 2020-2022, Java知识图谱 (http://www.altitude.xin).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.gitee.zhuyunlong2018.mybatisplusrelations.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author explore
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("r_user")
public class User extends Model<User> {
    @TableId()
    private Integer id;
    private String name;
    private Integer deptId;
    
    public User(User user) {
        if (user != null) {
            this.id = user.getId();
            this.name = user.getName();
            this.deptId = user.getDeptId();
        }
    }
}