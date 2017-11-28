package com.jieweifu.controllers.insona;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.User;
import com.jieweifu.models.insona.Product;
import com.jieweifu.models.insona.ProductInfo;
import com.jieweifu.models.insona.UserProduct;
import com.jieweifu.services.admin.UserService;
import com.jieweifu.services.insona.ProductService;
import com.jieweifu.services.insona.UserProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController("InsonaUserProduct")
@RequestMapping("insona/userProduct")
@AdminAuthAnnotation
public class UserProductController {

    private UserProductService userProductService;
    private RedisUtil redisUtil;
    private ProductService productService;
    private UserService userService;

    @Autowired
    public UserProductController(UserProductService userProductService,
                                 RedisUtil redisUtil,
                                 ProductService productService,
                                 UserService userService) {
        this.userProductService = userProductService;
        this.redisUtil = redisUtil;
        this.productService = productService;
        this.userService = userService;
    }

    /**
     * 供应商绑定设备
     *
     * @param userProduct did,uid 可以指定uid进行绑定
     * @param errors      判断字段
     * @return message
     */
    @PostMapping("saveUserProduct")
    public Result saveUserProduct(@Valid @RequestBody UserProduct userProduct, Errors errors) {
        if (errors.hasErrors())
            return new Result().setError(ErrorUtil.getErrors(errors));
        if (userProduct.getBaseUserId() == null)
            return new Result().setError("信息不能为空");
        if (userProductService.getByDid(userProduct.getDid()) != null)
            return new Result().setError("该设备已绑定");
        Product product = null;
        product = productService.getByDid(userProduct.getDid());
        if (product == null)
            return new Result().setError("设备不存在");
        Result result = new Result();
        redisUtil.lock("saveUserProduct", 3,
                () -> {
                    userProduct.setUpdateDt(String.valueOf(System.currentTimeMillis()));
                    userProductService.saveUserProduct(userProduct);
                    result.setMessage("绑定成功");
                },
                () -> result.setError("绑定失败"));
        return result;
    }

    /**
     * 经销商绑定设备,只能绑定自己uid的设备
     *
     * @param userProduct 设备
     * @return message
     */
    @PostMapping("saveProduct")
    public Result saveProduct(@Valid @RequestBody UserProduct userProduct) {
        if (userProductService.getByDid(userProduct.getDid()) != null)
            return new Result().setError("该设备已绑定");
        Product product = null;
        product = productService.getByDid(userProduct.getDid());
        if (product == null)
            return new Result().setError("设备不存在");
        Result result = new Result();
        redisUtil.lock("saveUserProduct", 3,
                () -> {
                    userProduct.setBaseUserId(String.valueOf(BaseContextHandler.getUserId()));
                    userProduct.setUpdateDt(String.valueOf(System.currentTimeMillis()));
                    userProductService.saveUserProduct(userProduct);
                    result.setMessage("绑定成功");
                },
                () -> result.setError("绑定失败"));
        return result;
    }

    /**
     * 分页查找全部经销商和其绑定的设备
     *
     * @param pageIndex 页码
     * @param pageSize  页条目
     * @return map
     */
    @GetMapping("getUserProduct/{pageIndex}/{pageSize}")
    public Result getUserProduct(@PathVariable("pageIndex") int pageIndex,
                                 @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 0)
            return new Result().setError("页码或条目数不合法");
        int userId = BaseContextHandler.getUserId();
        User user = userService.getUserById(userId);
        List<User> userList = userService.getUserIds(user.getLabel());
        List<ProductInfo> infoList = new ArrayList<>();
        userList.forEach(
                user1 -> {
                    ProductInfo productInfo = userProductService.pageUserProduct(pageIndex, pageSize, String.valueOf(user1.getId()));
                    if (productInfo != null)
                        infoList.add(productInfo);
                }
        );
        int total = userProductService.getTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("list", infoList);
        map.put("total", total);
        return new Result().setData(map);
    }

    /**
     * 解绑设备(供应商,经销商)
     *
     * @param ids id数组
     * @return massage
     */
    @DeleteMapping("removeProduct")
    public Result removeProduct(@RequestBody List<String> ids) {
        for (String id : ids) {
            if (userProductService.getByid(Integer.valueOf(id)) == null)
                return new Result().setError("id无效");
            userProductService.removeUserProduct(Integer.parseInt(id));
        }
        return new Result().setMessage("删除成功");
    }

    /**
     * 经销商查找自己绑定的设备
     *
     * @param pageIndex 页码
     * @param pageSize  页条目
     * @return map
     */
    @GetMapping("getProduct/{pageIndex}/{pageSize}")
    public Result getByUid(@PathVariable("pageIndex") int pageIndex,
                           @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 0)
            return new Result().setError("页码或条目数不合法");
        String uid = String.valueOf(BaseContextHandler.getUserId());
        int total = userProductService.getTotal();
        List<ProductInfo> userProductList = userProductService.getByUid(uid, pageIndex, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("list", userProductList);
        map.put("total", total);
        return new Result().setData(map);
    }

    /**
     * 显示设备信息
     *
     * @param did 设备did
     * @return product
     */
    @GetMapping("getProductInfo/{did}")
    public Result getProductInfo(@PathVariable("did") String did) {
        Product product = productService.getByDid(did);
        if (product == null)
            return new Result().setError("did不合法");
        return new Result().setData(product);
    }

    @GetMapping("getProducts")
    public Result getProducts() {
        List<Product> productList = productService.getProducts();
        return new Result().setData(productList);
    }
}
