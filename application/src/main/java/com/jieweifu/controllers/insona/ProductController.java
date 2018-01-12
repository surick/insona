package com.jieweifu.controllers.insona;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.Role;
import com.jieweifu.models.admin.User;
import com.jieweifu.models.insona.Product;
import com.jieweifu.models.insona.ProductDealer;
import com.jieweifu.models.insona.Type;
import com.jieweifu.services.admin.RoleService;
import com.jieweifu.services.admin.RoleUserService;
import com.jieweifu.services.admin.UserService;
import com.jieweifu.services.insona.ProductDealerService;
import com.jieweifu.services.insona.ProductService;
import com.jieweifu.services.insona.TypeService;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController("InsonaProduct")
@RequestMapping("insona/product")
public class ProductController {
    private ProductService productService;
    private RoleUserService roleUserService;
    private RoleService roleService;
    private ProductDealerService productDealerService;
    private TypeService typeService;
    private UserService userService;

    @Autowired
    public ProductController(ProductService productService,
                             RoleUserService roleUserService,
                             RoleService roleService,
                             ProductDealerService productDealerService,
                             TypeService typeService) {
        this.productService = productService;
        this.roleUserService = roleUserService;
        this.roleService = roleService;
        this.productDealerService = productDealerService;
        this.typeService = typeService;
    }

    /**
     * 新增设备
     */
    @PostMapping("save")
    public Result saveProduct(@Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        try {
            productService.saveProduct(product);
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请刷新后重试");
        }
        return new Result().setMessage("新增设备成功");
    }

    /**
     * 删除设备
     */
    @DeleteMapping("remove")
    public Result removeProduct(@RequestBody List<String> ids) {
        if (ids.isEmpty()) {
            return new Result().setError("id不合法");
        }
        for (String id :
                ids) {
            if (StringUtils.isBlank(productService.getProductById(Integer.parseInt(id)).getDid())) {
                return new Result().setError("选中的id有误");
            }
        }
        for (String pid :
                ids) {
            productService.removeProduct(Integer.parseInt(pid));
        }
        return new Result().setMessage("删除成功");
    }

    /**
     * 修改
     */
    @PutMapping("update")
    public Result updateProduct(@Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        try {
            productService.updateProduct(product);
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请刷新后重试");
        }
        return new Result().setMessage("修改成功");
    }

    /**
     * 查询
     */
    @GetMapping("list/{pageIndex}/{pageSize}")
    public Result listProducts(@PathVariable("pageIndex") int pageIndex,
                               @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 0)
            return new Result().setError("页码或条目数不合法");
        List<Product> list = null;
        int total = 0;
        try {
            list = productService.getProducts(pageIndex, pageSize);
            total = productService.total();
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请刷新后重试");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return new Result().setData(map);
    }

    /**
     * 根据用户角色，显示对应的商品
     */
    @GetMapping("show/{pageIndex}/{pageSize}")
    public Result showProducts(@PathVariable("pageIndex") int pageIndex,
                               @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 0)
            return new Result().setError("页码或条目数不合法");
        int userId = BaseContextHandler.getUserId();
        Role role = roleUserService.getRoleByUserId(userId);
        List<Product> list = null;
        int total = 0;
        Map<String, Object> map = new HashMap<>();
        if (role.getRoleName().equals("生产商") || role.getRoleName().equals("管理员") || role.getRoleName().equals("超级管理员")) {
            try {
                list = productService.getProducts(pageIndex, pageSize);
                total = productService.total("0", "2");
                map.put("list", list);
                map.put("total", total);
                return new Result().setData(map);
            } catch (Exception e) {
                return new Result().setError("系统繁忙，请刷新后重试");
            }
        } else if (role.getRoleName().equals("经销商")) {
            try {
                list = productService.listProducts("1");
                total = productService.total("1");
                map.put("list", list);
                map.put("total", total);
                return new Result().setData(map);
            } catch (Exception e) {
                return new Result().setError("系统繁忙，请刷新后重试");
            }
        }
        return new Result().setError("角色权限不足");
    }

    /**
     * 改变设备状态Status
     */
    @PutMapping("change/{status}")
    public Result statusChange(@RequestBody Sale sale, @PathVariable("status") String status) {
        if (StringUtils.isBlank(status) || sale.getIds().isEmpty()) {
            return new Result().setError("参数有误");
        }
        try {
            for (String id : sale.getIds()) {
                productService.setStatus(Integer.parseInt(id), status, sale.getSub_sale());
                ProductDealer productDealer = new ProductDealer();
                productDealer.setDealer(sale.getSub_sale());
                productDealer.setProduct_id(id);
                productDealer.setCrt_time(String.valueOf(System.currentTimeMillis()));
                productDealerService.saveProductDealer(productDealer);
            }
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请刷新后重试");
        }
        return new Result().setMessage("更新成功");

    }

    /**
     * 获取所有经销商
     */
    @GetMapping("getDealers")
    public Result getDealers() {
        List<User> list = null;
        try {
            list = roleService.getProducerUser("经销商");
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请刷新后重试");
        }
        return new Result().setData(list);
    }

    /**
     * 设备类别
     */
    @GetMapping("type")
    public Result getTypes() {
        List<Type> list;
        try {
            list = typeService.types();
        } catch (Exception e) {
            return new Result().setError("获取类别出错");
        }
        return new Result().setData(list);
    }

    public static class Sale {
        private List<String> ids;

        private String sub_sale;

        public List<String> getIds() {
            return ids;
        }

        public void setIds(List<String> ids) {
            this.ids = ids;
        }

        public String getSub_sale() {
            return sub_sale;
        }

        public void setSub_sale(String sub_sale) {
            this.sub_sale = sub_sale;
        }
    }
}
