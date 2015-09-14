/*
 * Copyright 2015 www.hyberbin.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Email:hyberbin@qq.com
 */
package org.jplus.model;

import java.sql.Date;
import java.sql.Types;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 *
 * @author hyberbin
 */
@Table(name = "JYC_YH")
public class User {
    /**
     * 用户id
     */
    @Column(name = "ID")
    private String id;
    
    @Column(name = "YHFID")
    private String userFId;
    /**
     * 学校信息表id
     */
    @Column(name = "XXJBSJZLID")
    private String schoolInfoId;
    /**
     * 用户类型id
帐号类型(对应用户类型表id
0：超级管理员，1：区级管理员 2：校级管理员，3：教师，4：学生，5：家长)
     */
 /*   @Cache()*/
    @Column(name = "YHLXID")
    private Integer userTypeId;
    
    /**
     * 登录帐号
     */
    @Column(name = "DLZH")
    private String account;
    /**
     * 帐号密码
     */
    @Column(name = "ZHMM")
    private String password;
    /**
     * 帐号昵称
     */
    @Column(name = "ZHNC")
    private String nickname;
    
    @Column(name = "cjsj",sqltype = Types.TIMESTAMP)
    private Date createDate;
     /**
     * 帐号状态(0:正常，1：禁用 默认0)
     */
    @Column(name = "ZHZT")
    private Integer status=0;
    @Column(name = "BBH")
    private Integer version=890720;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserFId() {
        return userFId;
    }

    public void setUserFId(String userFId) {
        this.userFId = userFId;
    }

    public String getSchoolInfoId() {
        return schoolInfoId;
    }

    public void setSchoolInfoId(String schoolInfoId) {
        this.schoolInfoId = schoolInfoId;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
}
