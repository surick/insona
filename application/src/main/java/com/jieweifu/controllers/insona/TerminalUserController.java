package com.jieweifu.controllers.insona;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.User;
import com.jieweifu.models.insona.*;
import com.jieweifu.services.admin.UserService;
import com.jieweifu.services.insona.ProductSaleService;
import com.jieweifu.services.insona.ProductService;
import com.jieweifu.services.insona.TerminalUserService;
import com.jieweifu.services.insona.UserProductService;
import com.jieweifu.services.main.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@SuppressWarnings("unused")
@RestController("InsonaTerminal")
@RequestMapping("insona/terminal")
public class TerminalUserController {

    private TerminalUserService terminalUserService;
    private ProductService productService;
    private UserService userService;
    private UserProductService userProductService;
    private ProductSaleService productSaleService;
    private AppUserService appUserService;

    @Autowired
    public TerminalUserController(TerminalUserService terminalUserService,
                                  ProductService productService,
                                  UserService userService,
                                  UserProductService userProductService,
                                  ProductSaleService productSaleService,
                                  AppUserService appUserService) {
        this.terminalUserService = terminalUserService;
        this.productService = productService;
        this.userService = userService;
        this.userProductService = userProductService;
        this.productSaleService = productSaleService;
        this.appUserService = appUserService;
    }

    /**
     * 分页查询用户
     */
    @GetMapping("listTerminalUser/{pageIndex}/{pageSize}")
    public Result listTerminalUser(@PathVariable("pageIndex") int pageIndex,
                                   @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 0) {
            return new Result().setError("页码或条目数不合法");
        }
        //得到登录用户
        User user = BaseContextHandler.getUser();
        //得到下级厂商
        List<User> userList = userService.getUserIds(user.getLabel());
        //将自身加入集合
        userList.add(user);
        //得到子厂商绑定的设备
        List<ProductSale> productList = new ArrayList<>();
        for (User user1 : userList) {
            System.out.println(user1.getId());
            try {
                productList.addAll(productSaleService.getList(user1.getName()));
            } catch (NullPointerException ignored) {
                productList.add(null);
            }
        }
        //得到登录设备的用户关系
        List<UserProduct> users = new ArrayList<>();
        for (ProductSale product : productList) {
            users.addAll(userProductService.getByDealer(product.getDealer()));
        }
        //取uid
        List<String> ids = new ArrayList<>();
        users.forEach(
                insonaProductUser ->
                        ids.add(insonaProductUser.getUid())
        );
        //去重
        HashSet<String> h = new HashSet<>(ids);
        ids.clear();
        ids.addAll(h);
        //去空
        for (int i = 0; i < ids.size(); i++) {
            if (users.get(i) == null) {
                ids.remove(i);
            }
        }
        //查用户 ->id
        List<InsonaUser> listUser = new ArrayList<>();
//        ids.forEach(
//                id -> {
//                    listUser.add(terminalUserService.getUserById(id));
//                }
//        );
        listUser.addAll(terminalUserService.getAllUser());
        //手动分页
        List<InsonaUser> list = new ArrayList<>();
        int total = listUser.size();
        for (int i = 0; i <= total; i++) {
            // 开始索引
            int fromIndex = pageIndex * pageSize;
            // 结束索引
            int toIndex = fromIndex + pageSize;
            // 如果结束索引大于集合的最大索引，那么规定结束索引=集合大小
            if (toIndex > total) {
                toIndex = total;
            }
            if (fromIndex <= total) {
                list = listUser.subList(fromIndex, toIndex);
                if (toIndex >= fromIndex) {
                    break;
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", listUser.size());
        return new Result().setData(map);
    }

    /**
     * 根据id查找用户
     */
    @GetMapping("getUserById/{id}")
    public Result getUserById(@PathVariable("id") String id) {
        if (id == null) {
            return new Result().setError("查询信息不能为空");
        }
        System.out.println(id);
        InsonaUser user = terminalUserService.getUserById(id);
        if (user == null) {
            return new Result().setError("用户不存在");
        }
        return new Result().setData(user);
    }

    /**
     * 根据id查找设备
     */
    @GetMapping("getProductById/{uid}")
    public Result getProductById(@PathVariable("uid") String uid) {
        if (uid == null) {
            return new Result().setError("查询信息不能为空");
        }
        System.out.println(uid);
        List<InsonaProductUser> productList = terminalUserService.listProducts(uid);
        List<Product> list = new ArrayList<>();
        for (InsonaProductUser product : productList) {
            Product product1 = productService.getByDid(product.getDid());
            list.add(product1);
        }
        if (productList.isEmpty()) {
            return new Result().setError("设备不存在");
        }
        return new Result().setData(list);
    }

    @PostMapping("addUser")
    public Result addUser(@Valid @RequestBody InsonaUser insonaUser, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        int i = appUserService.findByPhone(insonaUser.getPhone());
        if (i > 0) {
            return new Result().setError("号码已存在");
        }
        try {
            appUserService.addUser(insonaUser);
            return new Result().setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().setError("添加失败");
        }
    }
}
