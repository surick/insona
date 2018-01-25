package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;
import com.jieweifu.models.regex.Regex;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@SuppressWarnings("unused")
@Entity(tableName = "insona_product")
public class Product {
    @Column(primaryKey = true)
    private int id;
    @Pattern(regexp = Regex.DID,message = "设备did格式错误")
    @Column(columnName = "did")
    private String did;
    @NotBlank(message = "请将设备名填写完整")
    @Column(columnName = "name")
    private String name;
    @NotBlank(message = "请将序列号填写完整")
    @Column(columnName = "serial_code")
    private String serial_code;
    @Pattern(regexp = Regex.VERSION,message = "版本号格式错误")
    @Column(columnName = "version")
    private String version;
    @NotBlank(message = "请将集成商信息填写完整")
    @Column(columnName = "sub_inter")
    private String sub_inter;
    @NotBlank(message = "请将生产商信息填写完整")
    @Column(columnName = "sub_maker")
    private String sub_maker;
    @Column(columnName = "sort_no")
    private String sort_no;
    @NotBlank(message = "请将设备类别填写完整")
    @Column(columnName = "type")
    private String type;
    @Column(columnName = "status")
    private String status;
    @Column(columnName = "extract")
    private String extract;
    @Column(columnName = "reason")
    private String reason;
    @Column(columnName = "sale")
    private String sale;

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(String serial_code) {
        this.serial_code = serial_code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSub_inter() {
        return sub_inter;
    }

    public void setSub_inter(String sub_inter) {
        this.sub_inter = sub_inter;
    }

    public String getSub_maker() {
        return sub_maker;
    }

    public void setSub_maker(String sub_maker) {
        this.sub_maker = sub_maker;
    }

    public String getSort_no() {
        return sort_no;
    }

    public void setSort_no(String sort_no) {
        this.sort_no = sort_no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
