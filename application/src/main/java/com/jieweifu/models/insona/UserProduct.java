package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;
import org.hibernate.validator.constraints.NotBlank;

@SuppressWarnings("unused")
@Entity(tableName = "insona_user_product")
public class UserProduct {
    @Column(primaryKey = true)
    private int id;
    @NotBlank(message = "设备不允许为空")
    @Column(columnName = "did")
    private String did;
    @Column(columnName = "createtime")
    private String createtime;
    @NotBlank(message = "用户不允许为空")
    @Column(columnName = "uid")
    private String uid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
