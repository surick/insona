package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

@SuppressWarnings("unused")
@Entity(tableName = "insona_product")
public class Product {
    @Column(columnName = "did")
    private String did;
    @Column(columnName = "product_key")
    private String productKey;
    @Column(columnName = "mac")
    private String mac;
    @Column(columnName = "insona_online")
    private Integer insonaOnlin;
    @Column(columnName = "passcode")
    private String passCode;
    @Column(columnName = "host")
    private String host;
    @Column(columnName = "remark")
    private String remark;
    @Column(columnName = "is_disabled")
    private Integer isDisabled;
    @Column(columnName = "type")
    private String type;
    @Column(columnName = "dev_alias")
    private String devAlias;
    @Column(columnName = "dev_label")
    private String devLabel;
    @Column(columnName = "role")
    private String role;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getInsonaOnlin() {
        return insonaOnlin;
    }

    public void setInsonaOnlin(Integer insonaOnlin) {
        this.insonaOnlin = insonaOnlin;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Integer isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDevAlias() {
        return devAlias;
    }

    public void setDevAlias(String devAlias) {
        this.devAlias = devAlias;
    }

    public String getDevLabel() {
        return devLabel;
    }

    public void setDevLabel(String devLabel) {
        this.devLabel = devLabel;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
