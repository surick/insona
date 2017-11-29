package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;
import org.hibernate.validator.constraints.NotBlank;

@SuppressWarnings("unused")
@Entity(tableName = "insona_user_product")
public class InsonaProductUser {

    @Column(primaryKey = true)
    private int id;
    @NotBlank(message = "不允许为空")
    @Column(columnName = "did")
    private String did;
    @Column(columnName = "update_dt")
    private String updateDt;
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

    public String getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(String updateDt) {
        this.updateDt = updateDt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
