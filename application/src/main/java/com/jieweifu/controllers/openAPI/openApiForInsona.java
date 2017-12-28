package com.jieweifu.controllers.openAPI;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.User;
import com.jieweifu.models.insona.Product;
import com.jieweifu.models.insona.Type;
import com.jieweifu.services.admin.RoleService;
import com.jieweifu.services.insona.ProductService;
import com.jieweifu.services.insona.TypeService;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * token验证
 * MD5加密（配置文件读取salt和key值）
 */
@SuppressWarnings("unused")
@RestController("INS")
@RequestMapping("ins/api")
public class openApiForInsona {

    @Value("${custom.key}")
    private String key;
    @Value("${custom.salt}")
    private String salt;
    private String auth;

    private ProductService productService;
    private TypeService typeService;
    private RoleService roleService;

    @Autowired
    public openApiForInsona(ProductService productService, TypeService typeService, RoleService roleService) {
        this.productService = productService;
        this.typeService = typeService;
        this.roleService = roleService;
        auth = DigestUtils.md5Hex(salt + key);
        System.out.println("===auth:" + auth + "===");
    }

    /**
     * 新增设备API
     */
    @PostMapping("saveProduct")
    public Result saveProduct(@Valid @RequestBody Product product, @RequestBody String token, Errors errors) {
        if (!token.equals(auth)) {
            return new Result().setError(401, "权限验证失败");
        }
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        try {
            productService.saveProduct(product);
        } catch (Exception e) {
            return new Result().setError(500, "系统繁忙，请刷新后重试");
        }
        return new Result().setData(JSONObject.fromObject(productService.getByDid(product.getDid())));
    }

    /**
     * 新增设备类型API
     */
    @PostMapping("saveType")
    public Result saveType(@Valid @RequestBody Type type, @RequestBody String token, Errors errors) {
        if (!token.equals(auth)) {
            return new Result().setError(401, "权限验证失败");
        }
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        try {
            typeService.saveType(type);
        } catch (Exception e) {
            return new Result().setError(500, "系统繁忙，请刷新后重试");
        }
        return new Result().setData(JSONObject.fromObject(typeService.getTypeById(type.getType_id())));
    }

    /**
     * 获取所有生产商API
     */
    @GetMapping("getProducer")
    public Result getProducer(@RequestBody String token) {
        if (!token.equals(auth)) {
            return new Result().setError(401, "权限验证失败");
        }
        List<User> list = null;
        try {
            list = roleService.getProducerUser();
        } catch (Exception e) {
            return new Result().setError(500, "系统繁忙，请刷新后重试");
        }
        return new Result().setData(JSONObject.fromObject(list));
    }
}
