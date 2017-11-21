package com.jieweifu.models.insona;

import com.jieweifu.common.dbservice.Column;
import com.jieweifu.common.dbservice.Entity;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

@SuppressWarnings("unused")
@Entity(tableName = "insona_material")
public class Material {

    @Column(primaryKey = true)
    private Integer id;
    @NotBlank(message = "title不能为空")
    @Column(columnName = "title")
    private String title;
    @Column(columnName = "img_url")
    private String imgUrl;
    @Column(columnName = "content")
    private String content;
    @Min(value = 0, message = "类型不合法")
    @Column(columnName = "type")
    private Integer type;
    @Range(min = 0, max = 1, message = "状态不合法")
    @Column(columnName = "enabled")
    private Integer enabled;
    @Range(min = 0, max = 1, message = "参数错误")
    @Column(columnName = "is_deleted")
    private Integer isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
