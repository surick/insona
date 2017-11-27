package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;

@SuppressWarnings("unused")
@Entity(tableName = "insona_table")
public class Table {

    @Column(primaryKey = true)
    private int id;
    @Column(columnName = "city")
    private String city;
    @Column(columnName = "normal_product")
    private Integer normalProduct;
    @Column(columnName = "error_product")
    private Integer errorProduct;
    @Column(columnName = "user_number")
    private Integer userNumber;
    @Column(columnName = "online_product")
    private Integer onlineProduct;

    public Integer getOnlineProduct() {
        return onlineProduct;
    }

    public void setOnlineProduct(Integer onlineProduct) {
        this.onlineProduct = onlineProduct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getNormalProduct() {
        return normalProduct;
    }

    public void setNormalProduct(Integer normalProduct) {
        this.normalProduct = normalProduct;
    }

    public Integer getErrorProduct() {
        return errorProduct;
    }

    public void setErrorProduct(Integer errorProduct) {
        this.errorProduct = errorProduct;
    }

    public Integer getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(Integer userNumber) {
        this.userNumber = userNumber;
    }
}
