package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;
import org.hibernate.validator.constraints.NotBlank;

@SuppressWarnings("unused")
@Entity(tableName = "insona_user")
public class InsonaUser {
    @Column(primaryKey = true)
    private int id =1;
    @NotBlank(message = "uid不能为空")
    @Column(columnName = "uid")
    private String uid;
    @NotBlank(message = "用户名不能为空")
    @Column(columnName = "username")
    private String username;
    @Column(columnName = "phone")
    private String phone;
    @Column(columnName = "email")
    private String email;
    @Column(columnName = "name")
    private String name;
    @Column(columnName = "gender")
    private String gender;
    @Column(columnName = "birthday")
    private String birthday;
    @Column(columnName = "address")
    private String address;
    @Column(columnName = "lang")
    private String lang;
    @Column(columnName = "remar")
    private String remar;
    @Column(columnName = "is_anonymous")
    private int isAnonymous;

    @Column(select = false)
    private String password;

    @Column(select = false)
    private String salt;

    @Column(columnName = "head_img_url")
    private String headImgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRemar() {
        return remar;
    }

    public void setRemar(String remar) {
        this.remar = remar;
    }

    public int getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(int isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}
