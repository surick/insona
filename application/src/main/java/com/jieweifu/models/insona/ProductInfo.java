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
    @Column(columnName = "insona_online")
    private String insonaOnline;
    @Column(columnName = "type")
    private String type;
    @Column(columnName = "dealer")
    private String dealer;
    @Column(columnName = "uid")
    private String uid;

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

    public String getInsonaOnline() {
        return insonaOnline;
    }

    public void setInsonaOnline(String insonaOnline) {
        this.insonaOnline = insonaOnline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "id=" + id +
                ", did='" + did + '\'' +
                ", name='" + name + '\'' +
                ", insonaOnline=" + insonaOnline +
                ", type='" + type + '\'' +
                ", dealer='" + dealer + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
