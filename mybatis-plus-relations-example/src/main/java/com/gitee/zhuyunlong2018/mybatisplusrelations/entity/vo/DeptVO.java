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

package com.gitee.zhuyunlong2018.mybatisplusrelations.entity.vo;

import com.gitee.zhuyunlong2018.mybatisplusrelations.domain.Dept;
import com.gitee.zhuyunlong2018.mybatisplusrelations.domain.User;
import com.gitee.zhuyunlong2018.mybatisplusrelations.annotations.BindMany;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString(callSuper = true)
public class DeptVO extends Dept {

    @BindMany(localProperty = "id", foreignProperty = "deptId")
    private List<User> users;

    public DeptVO(Dept dept) {
        super(dept);
    }
}
