package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;
import org.hibernate.validator.constraints.NotBlank;

@SuppressWarnings("unused")
@Entity(tableName = "insona_product_dealer")
public class ProductDealer {
    @Column(primaryKey = true)
    private int id;
    @NotBlank(message = "经销商不允许为空")
    @Column(columnName = "dealer")
    private String dealer;
    @NotBlank(message = "设备不允许为空")
    @Column(columnName = "product_id")
    private String product_id;
    @Column(columnName = "crt_time")
    private String crt_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCrt_time() {
        return crt_time;
    }

    public void setCrt_time(String crt_time) {
        this.crt_time = crt_time;
    }
}
