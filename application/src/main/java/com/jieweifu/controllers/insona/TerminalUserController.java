package com.jieweifu.controllers.insona;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.User;
import com.jieweifu.models.insona.InsonaProductUser;
import com.jieweifu.models.insona.InsonaUser;
import com.jieweifu.models.insona.Product;
import com.jieweifu.models.insona.UserProduct;
import com.jieweifu.services.admin.UserService;
import com.jieweifu.services.insona.ProductService;
import com.jieweifu.services.insona.TerminalUserService;
import com.jieweifu.services.insona.UserProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@SuppressWarnings("unused")
@RestController("InsonaTerminal")
@RequestMapping("insona/terminal")
public class TerminalUserController {

    private TerminalUserService terminalUserService;
    private ProductService productService;
    private UserService userService;
    private UserProductService userProductService;

    @Autowired
    public TerminalUserController(TerminalUserService terminalUserService,
                                  ProductService productService,
                                  UserService userService,
                                  UserProductService userProductService) {
        this.terminalUserService = terminalUserService;
        this.productService = productService;
        this.userService = userService;
        this.userProductService = userProductService;
    }

    /**
     * 分页查询用户
     */
    @GetMapping("listTerminalUser/{pageIndex}/{pageSize}")
    public Result listTerminalUser(@PathVariable("pageIndex") int pageIndex,
                                   @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 0)
            return new Result().setError("页码或条目数不合法");
        //得到登录用户
        User user = BaseContextHandler.getUser();
        //得到子厂商
        List<User> userList = userService.getUserIds(user.getLabel());
        //得到子厂商绑定的设备
        List<UserProduct> productList = new ArrayList<>();
        for (User user1 : userList) {
            System.out.println(user1.getId());
            try {
                userProductService.listUserProduct(String.valueOf(user1.getId()));
                productList.addAll(userProductService.listUserProduct(String.valueOf(user1.getId())));
            } catch (NullPointerException ignored) {
                productList.add(null);
            }
        }
        //得到登录设备的用户关系
        List<InsonaProductUser> users = new ArrayList<>();
        for (UserProduct product : productList) {
            users.addAll(terminalUserService.listUser(product.getDid()));
        }
        //取uid
        List<String> uids = new ArrayList<>();
        users.forEach(
                insonaProductUser ->
                        uids.add(insonaProductUser.getUid())
        );
        //去重
        HashSet<String> h = new HashSet<>(uids);
        uids.clear();
        uids.addAll(h);
        //去空
        for (int i = 0; i < uids.size(); i++) {
            if (users.get(i) == null) {
                uids.remove(i);
            }
        }
        //查用户 ->id
        List<InsonaUser> listUser = new ArrayList<>();
        uids.forEach(
                uid -> {
                    listUser.add(terminalUserService.getUserById(uid));
                }
        );
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
        if (id == null)
            return new Result().setError("查询信息不能为空");
        System.out.println(id);
        InsonaUser user = terminalUserService.getUserById(id);
        if (user == null)
            return new Result().setError("用户不存在");
        return new Result().setData(user);
    }

    /**
     * 根据id查找设备
     */
    @GetMapping("getProductById/{uid}")
    public Result getProductById(@PathVariable("uid") String uid) {
        if (uid == null)
            return new Result().setError("查询信息不能为空");
        System.out.println(uid);
        List<InsonaProductUser> productList = terminalUserService.listProducts(uid);
        List<Product> list = new ArrayList<>();
        for (InsonaProductUser product : productList) {
            Product product1 = productService.getByDid(product.getDid());
            list.add(product1);
        }
        if (productList.isEmpty())
            return new Result().setError("设备不存在");
        return new Result().setData(list);
    }
}
