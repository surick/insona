package com.jieweifu.models.insona;


import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

@Entity
public class ProductInfo {
    @Column(columnName = "id")
    private Integer id;
    @Column(columnName = "did")
    private String did;
    @Column(columnName = "name")
    private String name;
    @Column(columnName = "gizwit_info")
    private String gizwitInfo;
    @Column(columnName = "insona_online")
    private int insonaOnline;
    @Column(columnName = "serial_code")
    private String serialCode;
    @Column(columnName = "last_online")
    private String lastOnline;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGizwitInfo() {
        return gizwitInfo;
    }

    public void setGizwitInfo(String gizwitInfo) {
        this.gizwitInfo = gizwitInfo;
    }

    public int getInsonaOnline() {
        return insonaOnline;
    }

    public void setInsonaOnline(int insonaOnline) {
        this.insonaOnline = insonaOnline;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Column(columnName = "version")
    private String version;
    @Column(columnName = "base_user_id")
    private String uid;

}
